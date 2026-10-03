package studentmanagement;

public class Student {
    private int id;
    private String name;
    private String email;
    private double marks;
    private int attendance;
    private StudentStatus status;

    public Student(int id, String name, String email, double marks, int attendance, StudentStatus status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.marks = marks;
        this.attendance = attendance;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public double getMarks() {
        return marks;
    }

    public int getAttendance() {
        return attendance;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    public String calculateGrade() {
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 70) return "B";
        if (marks >= 60) return "C";
        if (marks >= 50) return "D";
        return "F";
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Name: " + name +
                " | Email: " + email +
                " | Marks: " + marks +
                " | Grade: " + calculateGrade() +
                " | Attendance: " + attendance + "%" +
                " | Status: " + status;
    }

    public String toFileString() {
        return id + "|" + name + "|" + email + "|" + marks + "|" + attendance + "|" + status;
    }

    public static Student fromFileString(String line) {
        String[] parts = line.split("\\|");

        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid student data in file.");
        }

        int id = Integer.parseInt(parts[0]);
        String name = parts[1];
        String email = parts[2];
        double marks = Double.parseDouble(parts[3]);
        int attendance = Integer.parseInt(parts[4]);
        StudentStatus status = StudentStatus.valueOf(parts[5]);

        return new Student(id, name, email, marks, attendance, status);
    }
}
