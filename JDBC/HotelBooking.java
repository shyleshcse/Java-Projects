import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * PROJECT 2: Hotel Room Booking System
 *
 * Tables  : rooms, customers, bookings   (created from Java)
 * Concepts: Date operations, Transactions (commit / rollback), Joins, GROUP BY,
 *           LIKE search, PreparedStatement, Validation, ONE shared connection.
 
 */
public class HotelBooking {

    // ------------------------------------------------------------------
    // 1. CONNECTION DETAILS (change PASSWORD to your MySQL password)
    // ------------------------------------------------------------------
    private static final String URL = "jdbc:mysql://localhost:3306/";   // server only, no database yet
    private static final String DB_NAME = "hotel_booking_jdbc_db";            // change this name if you want
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static Connection con;                       // opened ONCE, reused everywhere
    private static final Scanner sc = new Scanner(System.in);

    // ------------------------------------------------------------------
    // 2. MAIN
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASSWORD);   // ONE connection for the whole program

            resetDatabase();      // create the database if it does not already exist and keep existing data
            createTables();
            seedIfEmpty();

            int choice;
            do {
                printMenu();
                choice = readInt("Enter choice: ");
                try {
                    switch (choice) {
                        case 1:  addRoom(); break;
                        case 2:  registerCustomer(); break;
                        case 3:  viewRooms(); break;
                        case 4:  availableRooms(); break;
                        case 5:  bookRoom(); break;
                        case 6:  checkIn(); break;
                        case 7:  checkOut(); break;
                        case 8:  cancelBooking(); break;
                        case 9:  viewBookings(); break;
                        case 10: searchCustomerByName(); break;
                        case 11: bookingsOfCustomer(); break;
                        case 12: todaysActivity(); break;
                        case 13: revenueReport(); break;
                        case 14: System.out.println("Thank you. Bye!"); break;
                        default: System.out.println("Invalid choice, try 1 to 14.");
                    }
                } catch (SQLException e) {
                    System.out.println("Database error: " + e.getMessage());
                }
            } while (choice != 14);

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Could not start: " + e.getMessage());
        } finally {
            try {
                if (con != null) con.close();            // closed ONCE
            } catch (SQLException e) {
                System.out.println("Close failed: " + e.getMessage());
            }
            sc.close();
        }
    }

    static void printMenu() {
        System.out.println("""

                ===== HOTEL ROOM BOOKING =====
                 1. Add a room
                 2. Register customer
                 3. View all rooms
                 4. Check available rooms for dates
                 5. Book a room (transaction)
                 6. Check-in
                 7. Check-out and bill (transaction)
                 8. Cancel a booking
                 9. View all bookings (join)
                10. Search customer by name (LIKE)
                11. Bookings of a customer by phone (join)
                12. Today's check-ins and check-outs (date operations)
                13. Revenue report by room type (GROUP BY)
                14. Exit""");
    }

    // ------------------------------------------------------------------
    // 3A. KEEP EXISTING DATABASE
    // We do not delete the old database on every run.
    // CREATE DATABASE IF NOT EXISTS creates the database only if it does not exist.
    // If the database already exists, all existing data is preserved.
    // Then setCatalog() switches the SAME connection to the database.
    // ------------------------------------------------------------------
    static void resetDatabase() throws SQLException {
        try (Statement st = con.createStatement()) {
            // st.execute("DROP DATABASE IF EXISTS " + DB_NAME);
            st.execute("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        }
        con.setCatalog(DB_NAME);                             // same as: USE DB_NAME
        System.out.println("Fresh database '" + DB_NAME + "' created.");
    }

    // ------------------------------------------------------------------
    // 3. TABLE CREATION WITH CONSTRAINTS
    // ------------------------------------------------------------------
    static void createTables() throws SQLException {
        String rooms = """
                CREATE TABLE IF NOT EXISTS rooms (
                    room_no         INT PRIMARY KEY,
                    room_type       VARCHAR(10) NOT NULL
                                    CHECK (room_type IN ('Single', 'Double', 'Deluxe', 'Suite')),
                    price_per_night DECIMAL(10,2) NOT NULL CHECK (price_per_night > 0),
                    capacity        INT NOT NULL CHECK (capacity BETWEEN 1 AND 10)
                )""";

        String customers = """
                CREATE TABLE IF NOT EXISTS customers (
                    customer_id INT PRIMARY KEY AUTO_INCREMENT,
                    name        VARCHAR(60)  NOT NULL,
                    phone       VARCHAR(10)  NOT NULL UNIQUE,
                    email       VARCHAR(100) NOT NULL UNIQUE
                )""";

        String bookings = """
                CREATE TABLE IF NOT EXISTS bookings (
                    booking_id        INT PRIMARY KEY AUTO_INCREMENT,
                    room_no           INT NOT NULL,
                    customer_id       INT NOT NULL,
                    check_in_date     DATE NOT NULL,
                    check_out_date    DATE NOT NULL,
                    status            VARCHAR(12) NOT NULL DEFAULT 'Booked'
                                      CHECK (status IN ('Booked', 'Checked-In',
                                                        'Checked-Out', 'Cancelled')),
                    actual_check_in   DATE NULL,
                    actual_check_out  DATE NULL,
                    total_amount      DECIMAL(10,2) NULL,
                    CHECK (check_out_date > check_in_date),
                    FOREIGN KEY (room_no)     REFERENCES rooms(room_no),
                    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
                )""";

        try (Statement st = con.createStatement()) {
            st.execute(rooms);
            st.execute(customers);
            st.execute(bookings);
        }
        System.out.println("Tables are ready (created with constraints if they did not exist).");
    }

    static void seedIfEmpty() throws SQLException {
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM rooms")) {
            rs.next();
            if (rs.getInt(1) > 0) return;
        }

        con.setAutoCommit(false);
        try {
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO rooms (room_no, room_type, price_per_night, capacity) VALUES (?, ?, ?, ?)")) {
                Object[][] data = {
                    {101, "Single", 1500, 1}, {102, "Single", 1500, 1},
                    {201, "Double", 2500, 2}, {202, "Double", 2500, 2},
                    {301, "Deluxe", 4000, 3}, {401, "Suite",  7000, 4}};
                for (Object[] r : data) {
                    ps.setInt(1, (Integer) r[0]);
                    ps.setString(2, (String) r[1]);
                    ps.setBigDecimal(3, BigDecimal.valueOf((Integer) r[2]));
                    ps.setInt(4, (Integer) r[3]);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO customers (name, phone, email) VALUES (?, ?, ?)")) {
                String[][] data = {
                    {"Arun Kumar",   "9876543210", "arun@gmail.com"},
                    {"Priya Sharma", "9123456780", "priya@gmail.com"},
                    {"Ravi Teja",    "9988776655", "ravi@gmail.com"}};
                for (String[] r : data) {
                    ps.setString(1, r[0]); ps.setString(2, r[1]); ps.setString(3, r[2]);
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            LocalDate today = LocalDate.now();
            String b = "INSERT INTO bookings (room_no, customer_id, check_in_date, check_out_date, status, "
                     + "actual_check_in, actual_check_out, total_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(b)) {
                // a finished stay in the past (already billed)
                addSeedBooking(ps, 201, 1, today.minusDays(10), today.minusDays(7), "Checked-Out",
                        today.minusDays(10), today.minusDays(7), new BigDecimal("7500"));
                // a guest staying now (checked in yesterday, leaving tomorrow)
                addSeedBooking(ps, 301, 2, today.minusDays(1), today.plusDays(1), "Checked-In",
                        today.minusDays(1), null, null);
                // a booking that arrives today (can be checked in)
                addSeedBooking(ps, 101, 3, today, today.plusDays(2), "Booked", null, null, null);
                // a future booking
                addSeedBooking(ps, 401, 1, today.plusDays(5), today.plusDays(8), "Booked", null, null, null);
                ps.executeBatch();
            }
            con.commit();
            System.out.println("Demo data added (6 rooms, 3 customers, 4 bookings).");
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }

    private static void addSeedBooking(PreparedStatement ps, int room, int cust, LocalDate in, LocalDate out,
                                       String status, LocalDate actIn, LocalDate actOut, BigDecimal total)
            throws SQLException {
        ps.setInt(1, room);
        ps.setInt(2, cust);
        ps.setDate(3, Date.valueOf(in));
        ps.setDate(4, Date.valueOf(out));
        ps.setString(5, status);
        ps.setDate(6, actIn == null ? null : Date.valueOf(actIn));
        ps.setDate(7, actOut == null ? null : Date.valueOf(actOut));
        ps.setBigDecimal(8, total);
        ps.addBatch();
    }

    // ------------------------------------------------------------------
    // 4. VALIDATION METHODS
    // ------------------------------------------------------------------
    static boolean isValidPhone(String phone) {
        return phone.matches("[6-9]\\d{9}");
    }

    static boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    static boolean isValidRoomType(String type) {
        return type.equalsIgnoreCase("Single") || type.equalsIgnoreCase("Double")
            || type.equalsIgnoreCase("Deluxe") || type.equalsIgnoreCase("Suite");
    }

    // ------------------------------------------------------------------
    // 5. INPUT HELPERS
    // ------------------------------------------------------------------
    static String readText(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readText(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    static BigDecimal readPrice(String prompt) {
        while (true) {
            try {
                BigDecimal value = new BigDecimal(readText(prompt));
                if (value.signum() > 0) return value;
                System.out.println("Price must be greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid price.");
            }
        }
    }

    /** Returns null when the text is not a valid yyyy-mm-dd date. */
    static LocalDate readDate(String prompt) {
        try {
            return LocalDate.parse(readText(prompt));
        } catch (DateTimeParseException e) {
            System.out.println("Date must be in yyyy-mm-dd format (example 2026-10-25).");
            return null;
        }
    }

    // ------------------------------------------------------------------
    // 6. ONE PRINT METHOD USED BY EVERY SELECT
    // ------------------------------------------------------------------
    static boolean printResults(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int cols = md.getColumnCount();
        int[] width = new int[cols];
        for (int i = 0; i < cols; i++) {
            width[i] = md.getColumnLabel(i + 1).length();
        }
        List<String[]> rows = new ArrayList<>();
        while (rs.next()) {
            String[] row = new String[cols];
            for (int i = 0; i < cols; i++) {
                String value = rs.getString(i + 1);
                row[i] = (value == null) ? "-" : value;
                width[i] = Math.max(width[i], row[i].length());
            }
            rows.add(row);
        }
        if (rows.isEmpty()) {
            System.out.println("No records found.");
            return false;
        }
        StringBuilder border = new StringBuilder("+");
        for (int w : width) border.append("-".repeat(w + 2)).append("+");
        System.out.println(border);
        StringBuilder head = new StringBuilder("|");
        for (int i = 0; i < cols; i++) {
            head.append(String.format(" %-" + width[i] + "s |", md.getColumnLabel(i + 1)));
        }
        System.out.println(head);
        System.out.println(border);
        for (String[] row : rows) {
            StringBuilder line = new StringBuilder("|");
            for (int i = 0; i < cols; i++) {
                line.append(String.format(" %-" + width[i] + "s |", row[i]));
            }
            System.out.println(line);
        }
        System.out.println(border);
        System.out.println(rows.size() + " record(s).");
        return true;
    }

    static void runQuery(String sql, String... params) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setString(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                printResults(rs);
            }
        }
    }

    // ------------------------------------------------------------------
    // 7. ROOM AND CUSTOMER OPERATIONS
    // ------------------------------------------------------------------
    static void addRoom() throws SQLException {
        int roomNo = readInt("Room number: ");
        String type = readText("Room type (Single/Double/Deluxe/Suite): ");
        BigDecimal price = readPrice("Price per night: ");
        int capacity = readInt("Capacity (1 to 10): ");

        if (roomNo <= 0) {
            System.out.println("Room number must be positive.");
            return;
        }
        if (!isValidRoomType(type)) {
            System.out.println("Room type must be Single, Double, Deluxe or Suite.");
            return;
        }
        if (capacity < 1 || capacity > 10) {
            System.out.println("Capacity must be between 1 and 10.");
            return;
        }
        type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();

        String sql = "INSERT INTO rooms (room_no, room_type, price_per_night, capacity) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, roomNo);
            ps.setString(2, type);
            ps.setBigDecimal(3, price);
            ps.setInt(4, capacity);
            if (ps.executeUpdate() > 0) {
                System.out.println("Room added successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Could not add room (room number may already exist): " + e.getMessage());
        }
    }

    static void viewRooms() throws SQLException {
        runQuery("SELECT room_no, room_type, price_per_night, capacity FROM rooms ORDER BY room_no");
    }

    static void registerCustomer() throws SQLException {
        String name = readText("Name: ");
        String phone = readText("Phone (10 digits): ");
        String email = readText("Email: ");

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty.");
            return;
        }
        if (!isValidPhone(phone)) {
            System.out.println("Phone must be 10 digits and start with 6, 7, 8 or 9.");
            return;
        }
        if (!isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return;
        }
        String sql = "INSERT INTO customers (name, phone, email) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            if (ps.executeUpdate() > 0) {
                System.out.println("Customer registered successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Could not register (phone or email may already exist): " + e.getMessage());
        }
    }

    static void searchCustomerByName() throws SQLException {
        String word = readText("Enter part of the name: ");
        runQuery("SELECT customer_id, name, phone, email FROM customers WHERE name LIKE ?",
                 "%" + word + "%");
    }

    static int findCustomerIdByPhone(String phone) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("SELECT customer_id FROM customers WHERE phone = ?")) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        }
    }

    // ------------------------------------------------------------------
    // 8. AVAILABILITY (date overlap logic)
    //    Two stays overlap when  existingIn < newOut  AND  existingOut > newIn.
    //    The check-out day itself is free for the next guest.
    // ------------------------------------------------------------------
    private static final String OVERLAP_CONDITION = """
            EXISTS (SELECT 1 FROM bookings b
                    WHERE b.room_no = r.room_no
                      AND b.status IN ('Booked', 'Checked-In')
                      AND b.check_in_date < ? AND b.check_out_date > ?)""";

    static void availableRooms() throws SQLException {
        LocalDate in = readDate("Check-in date (yyyy-mm-dd): ");
        if (in == null) return;
        LocalDate out = readDate("Check-out date (yyyy-mm-dd): ");
        if (out == null) return;
        if (!out.isAfter(in)) {
            System.out.println("Check-out must be after check-in.");
            return;
        }
        String sql = "SELECT r.room_no, r.room_type, r.price_per_night, r.capacity FROM rooms r "
                   + "WHERE NOT " + OVERLAP_CONDITION + " ORDER BY r.room_no";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(out));
            ps.setDate(2, Date.valueOf(in));
            try (ResultSet rs = ps.executeQuery()) {
                printResults(rs);
            }
        }
        long nights = ChronoUnit.DAYS.between(in, out);
        System.out.println("Stay length: " + nights + " night(s).");
    }

    // ------------------------------------------------------------------
    // 9. BOOKING (TRANSACTION)
    // ------------------------------------------------------------------
    static void bookRoom() {
        int roomNo = readInt("Room number: ");
        String phone = readText("Customer phone: ");
        LocalDate in = readDate("Check-in date (yyyy-mm-dd): ");
        if (in == null) return;
        LocalDate out = readDate("Check-out date (yyyy-mm-dd): ");
        if (out == null) return;

        if (in.isBefore(LocalDate.now())) {
            System.out.println("Check-in date cannot be in the past.");
            return;
        }
        if (!out.isAfter(in)) {
            System.out.println("Check-out must be after check-in.");
            return;
        }

        try {
            int customerId = findCustomerIdByPhone(phone);
            if (customerId == -1) {
                System.out.println("No customer with this phone. Register the customer first (option 2).");
                return;
            }

            con.setAutoCommit(false);                        // start transaction

            // Lock the room row so two people cannot book the same room at the same time
            BigDecimal price;
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT price_per_night FROM rooms WHERE room_no = ? FOR UPDATE")) {
                ps.setInt(1, roomNo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Room " + roomNo + " does not exist.");
                    }
                    price = rs.getBigDecimal(1);
                }
            }

            // Is the room free for these dates?
            String check = "SELECT COUNT(*) FROM bookings b WHERE b.room_no = ? "
                         + "AND b.status IN ('Booked', 'Checked-In') "
                         + "AND b.check_in_date < ? AND b.check_out_date > ?";
            try (PreparedStatement ps = con.prepareStatement(check)) {
                ps.setInt(1, roomNo);
                ps.setDate(2, Date.valueOf(out));
                ps.setDate(3, Date.valueOf(in));
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    if (rs.getInt(1) > 0) {
                        throw new SQLException("Room " + roomNo + " is already booked for these dates.");
                    }
                }
            }

            String insert = "INSERT INTO bookings (room_no, customer_id, check_in_date, check_out_date) "
                          + "VALUES (?, ?, ?, ?)";
            int bookingId;
            try (PreparedStatement ps = con.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, roomNo);
                ps.setInt(2, customerId);
                ps.setDate(3, Date.valueOf(in));
                ps.setDate(4, Date.valueOf(out));
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    keys.next();
                    bookingId = keys.getInt(1);
                }
            }

            con.commit();
            long nights = ChronoUnit.DAYS.between(in, out);
            System.out.println("Booking confirmed. Booking id: " + bookingId);
            System.out.println("Estimated bill: " + nights + " night(s) x " + price + " = "
                    + price.multiply(BigDecimal.valueOf(nights)));
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                System.out.println("Rollback failed: " + ex.getMessage());
            }
            System.out.println("Booking failed (rolled back): " + e.getMessage());
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Could not reset auto-commit: " + e.getMessage());
            }
        }
    }

    // ------------------------------------------------------------------
    // 10. CHECK-IN, CHECK-OUT (BILLING), CANCEL
    // ------------------------------------------------------------------
    static void checkIn() throws SQLException {
        int id = readInt("Booking id: ");
        LocalDate today = LocalDate.now();
        // Allowed only when the booking is 'Booked' and the stay includes today
        String sql = "UPDATE bookings SET status = 'Checked-In', actual_check_in = ? "
                   + "WHERE booking_id = ? AND status = 'Booked' "
                   + "AND check_in_date <= ? AND check_out_date > ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(today));
            ps.setInt(2, id);
            ps.setDate(3, Date.valueOf(today));
            ps.setDate(4, Date.valueOf(today));
            System.out.println(ps.executeUpdate() > 0 ? "Checked in on " + today + "."
                    : "Cannot check in. The booking must exist, be in Booked status and cover today's date.");
        }
    }

    /** TRANSACTION: calculate the bill and close the booking together. */
    static void checkOut() {
        int id = readInt("Booking id: ");
        LocalDate today = LocalDate.now();

        try {
            con.setAutoCommit(false);

            LocalDate actualIn;
            BigDecimal price;
            String guest;
            int roomNo;
            String sql = """
                    SELECT b.actual_check_in, r.price_per_night, r.room_no, c.name
                    FROM bookings b
                    JOIN rooms r     ON b.room_no = r.room_no
                    JOIN customers c ON b.customer_id = c.customer_id
                    WHERE b.booking_id = ? AND b.status = 'Checked-In'
                    FOR UPDATE""";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("No checked-in booking with id " + id + ".");
                    }
                    actualIn = rs.getDate(1).toLocalDate();
                    price = rs.getBigDecimal(2);
                    roomNo = rs.getInt(3);
                    guest = rs.getString(4);
                }
            }

            long nights = Math.max(1, ChronoUnit.DAYS.between(actualIn, today));   // minimum 1 night
            BigDecimal total = price.multiply(BigDecimal.valueOf(nights));

            String update = "UPDATE bookings SET status = 'Checked-Out', actual_check_out = ?, "
                          + "total_amount = ? WHERE booking_id = ?";
            try (PreparedStatement ps = con.prepareStatement(update)) {
                ps.setDate(1, Date.valueOf(today));
                ps.setBigDecimal(2, total);
                ps.setInt(3, id);
                ps.executeUpdate();
            }
            con.commit();

            System.out.println("-------------- BILL --------------");
            System.out.println("Guest        : " + guest);
            System.out.println("Room         : " + roomNo);
            System.out.println("Stayed from  : " + actualIn + " to " + today);
            System.out.println("Nights       : " + nights + " x " + price);
            System.out.println("TOTAL AMOUNT : " + total);
            System.out.println("----------------------------------");
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                System.out.println("Rollback failed: " + ex.getMessage());
            }
            System.out.println("Check-out failed (rolled back): " + e.getMessage());
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Could not reset auto-commit: " + e.getMessage());
            }
        }
    }

    static void cancelBooking() throws SQLException {
        int id = readInt("Booking id to cancel: ");
        String sql = "UPDATE bookings SET status = 'Cancelled' WHERE booking_id = ? AND status = 'Booked'";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            System.out.println(ps.executeUpdate() > 0 ? "Booking cancelled."
                    : "Cannot cancel. The booking must exist and still be in Booked status.");
        }
    }

    // ------------------------------------------------------------------
    // 11. QUERIES: JOIN, DATE FUNCTIONS, GROUP BY
    // ------------------------------------------------------------------
    static void viewBookings() throws SQLException {
        runQuery("""
                SELECT b.booking_id, c.name AS guest, b.room_no, r.room_type,
                       b.check_in_date, b.check_out_date,
                       DATEDIFF(b.check_out_date, b.check_in_date) AS nights,
                       b.status, b.total_amount
                FROM bookings b
                JOIN customers c ON b.customer_id = c.customer_id
                JOIN rooms r     ON b.room_no = r.room_no
                ORDER BY b.booking_id""");
    }

    static void bookingsOfCustomer() throws SQLException {
        String phone = readText("Customer phone: ");
        runQuery("""
                SELECT b.booking_id, c.name AS guest, b.room_no, b.check_in_date,
                       b.check_out_date, b.status, b.total_amount
                FROM bookings b
                JOIN customers c ON b.customer_id = c.customer_id
                WHERE c.phone = ?
                ORDER BY b.check_in_date DESC""", phone);
    }

    static void todaysActivity() throws SQLException {
        System.out.println("Guests arriving today (Booked, check-in date = today):");
        runQuery("""
                SELECT b.booking_id, c.name AS guest, b.room_no, b.check_out_date
                FROM bookings b
                JOIN customers c ON b.customer_id = c.customer_id
                WHERE b.status = 'Booked' AND b.check_in_date = CURDATE()""");

        System.out.println("\nGuests due to leave today (Checked-In, check-out date <= today):");
        runQuery("""
                SELECT b.booking_id, c.name AS guest, b.room_no, b.check_out_date
                FROM bookings b
                JOIN customers c ON b.customer_id = c.customer_id
                WHERE b.status = 'Checked-In' AND b.check_out_date <= CURDATE()""");

        System.out.println("\nGuests staying in the hotel right now:");
        runQuery("""
                SELECT b.booking_id, c.name AS guest, b.room_no, b.actual_check_in,
                       DATEDIFF(CURDATE(), b.actual_check_in) AS nights_so_far
                FROM bookings b
                JOIN customers c ON b.customer_id = c.customer_id
                WHERE b.status = 'Checked-In'""");
    }

    static void revenueReport() throws SQLException {
        runQuery("""
                SELECT r.room_type,
                       COUNT(*)            AS completed_stays,
                       SUM(b.total_amount) AS total_revenue,
                       AVG(b.total_amount) AS average_bill
                FROM bookings b
                JOIN rooms r ON b.room_no = r.room_no
                WHERE b.status = 'Checked-Out'
                GROUP BY r.room_type
                ORDER BY total_revenue DESC""");
    }
}
