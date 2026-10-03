package studentmanagement;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentService studentService = new StudentService();
    private static final StudentFileService fileService =
            new StudentFileService("data/students.txt");

    public static void main(String[] args) {
        loadStudentsAtStartup();

        boolean running = true;

        while (running) {
            displayMenu();

            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1 -> addStudent();
                    case 2 -> displayAllStudents();
                    case 3 -> searchById();
                    case 4 -> searchByName();
                    case 5 -> updateMarks();
                    case 6 -> updateAttendance();
                    case 7 -> calculateGrade();
                    case 8 -> removeStudent();
                    case 9 -> displayTopper();
                    case 10 -> saveStudents();
                    case 11 -> loadStudents();
                    case 0 -> {
                        saveStudents();
                        System.out.println("Thank you. Program closed.");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (IllegalArgumentException | IOException e) {
                System.out.println("Error: " + e.getMessage());
            }

            System.out.println();
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("======================================");
        System.out.println("     STUDENT MANAGEMENT SYSTEM");
        System.out.println("======================================");
        System.out.println("1. Add Student");
        System.out.println("2. Display All Students");
        System.out.println("3. Search Student by ID");
        System.out.println("4. Search Student by Name");
        System.out.println("5. Update Student Marks");
        System.out.println("6. Update Student Attendance");
        System.out.println("7. Calculate Student Grade");
        System.out.println("8. Remove Student");
        System.out.println("9. Display Topper");
        System.out.println("10. Save Students to File");
        System.out.println("11. Load Students from File");
        System.out.println("0. Exit");
        System.out.println("======================================");
    }

    private static void addStudent() {
        int id = readInt("Enter student ID: ");

        System.out.print("Enter student name: ");
        String name = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        double marks = readDouble("Enter marks (0-100): ");
        int attendance = readInt("Enter attendance (0-100): ");

        StudentStatus status = readStatus();

        Student student = new Student(
                id,
                name,
                email,
                marks,
                attendance,
                status
        );

        studentService.addStudent(student);

        System.out.println("Student added successfully.");
    }

    private static void displayAllStudents() {
        List<Student> students = studentService.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("--------------- STUDENTS ---------------");

        for (Student student : students) {
            System.out.println(student);
        }
    }

    private static void searchById() {
        int id = readInt("Enter student ID: ");

        Student student = studentService.searchById(id);

        if (student == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println(student);
        }
    }

    private static void searchByName() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();

        List<Student> students = studentService.searchByName(name);

        if (students.isEmpty()) {
            System.out.println("No matching students found.");
            return;
        }

        for (Student student : students) {
            System.out.println(student);
        }
    }

    private static void updateMarks() {
        int id = readInt("Enter student ID: ");
        double marks = readDouble("Enter new marks (0-100): ");

        studentService.updateMarks(id, marks);

        System.out.println("Marks updated successfully.");
    }

    private static void updateAttendance() {
        int id = readInt("Enter student ID: ");
        int attendance = readInt("Enter new attendance (0-100): ");

        studentService.updateAttendance(id, attendance);

        System.out.println("Attendance updated successfully.");
    }

    private static void calculateGrade() {
        int id = readInt("Enter student ID: ");

        String grade = studentService.calculateGrade(id);

        System.out.println("Grade: " + grade);
    }

    private static void removeStudent() {
        int id = readInt("Enter student ID: ");

        studentService.removeStudent(id);

        System.out.println("Student removed successfully.");
    }

    private static void displayTopper() {
        Student topper = studentService.getTopper();

        if (topper == null) {
            System.out.println("No students available.");
        } else {
            System.out.println("--------------- TOPPER ---------------");
            System.out.println(topper);
        }
    }

    private static void saveStudents() throws IOException {
        fileService.saveStudents(studentService.getAllStudents());
        System.out.println("Student details saved successfully.");
    }

    private static void loadStudents() throws IOException {
        List<Student> students = fileService.loadStudents();
        studentService.loadStudents(students);

        System.out.println(students.size() + " student(s) loaded successfully.");
    }

    private static void loadStudentsAtStartup() {
        try {
            List<Student> students = fileService.loadStudents();
            studentService.loadStudents(students);

            if (!students.isEmpty()) {
                System.out.println(students.size() + " student(s) loaded from file.");
            }
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Could not load existing student data: " + e.getMessage());
        }
    }

    private static StudentStatus readStatus() {
        while (true) {
            System.out.println("Select status:");
            System.out.println("1. ACTIVE");
            System.out.println("2. INACTIVE");
            System.out.println("3. COMPLETED");

            int choice = readInt("Enter status: ");

            switch (choice) {
                case 1:
                    return StudentStatus.ACTIVE;
                case 2:
                    return StudentStatus.INACTIVE;
                case 3:
                    return StudentStatus.COMPLETED;
                default:
                    System.out.println("Invalid status. Try again.");
            }
        }
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);

            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(String message) {
        while (true) {
            System.out.print(message);

            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
