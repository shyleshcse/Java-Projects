package studentmanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentService {
    private final Map<Integer, Student> students = new HashMap<>();

    public void addStudent(Student student) {
        validateStudent(student);

        if (students.containsKey(student.getId())) {
            throw new IllegalArgumentException("Student ID already exists.");
        }

        students.put(student.getId(), student);
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    public Student searchById(int id) {
        return students.get(id);
    }

    public List<Student> searchByName(String name) {
        List<Student> result = new ArrayList<>();

        for (Student student : students.values()) {
            if (student.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(student);
            }
        }

        return result;
    }

    public void updateMarks(int id, double marks) {
        validateMarks(marks);

        Student student = getStudentOrThrow(id);
        student.setMarks(marks);
    }

    public void updateAttendance(int id, int attendance) {
        validateAttendance(attendance);

        Student student = getStudentOrThrow(id);
        student.setAttendance(attendance);
    }

    public String calculateGrade(int id) {
        return getStudentOrThrow(id).calculateGrade();
    }

    public void removeStudent(int id) {
        if (students.remove(id) == null) {
            throw new IllegalArgumentException("Student not found.");
        }
    }

    public Student getTopper() {
        if (students.isEmpty()) {
            return null;
        }

        Student topper = null;

        for (Student student : students.values()) {
            if (topper == null || student.getMarks() > topper.getMarks()) {
                topper = student;
            }
        }

        return topper;
    }

    public void clearStudents() {
        students.clear();
    }

    public void loadStudents(List<Student> loadedStudents) {
        students.clear();

        for (Student student : loadedStudents) {
            addStudent(student);
        }
    }

    private Student getStudentOrThrow(int id) {
        Student student = students.get(id);

        if (student == null) {
            throw new IllegalArgumentException("Student not found.");
        }

        return student;
    }

    private void validateStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null.");
        }

        if (student.getId() <= 0) {
            throw new IllegalArgumentException("Student ID must be positive.");
        }

        if (student.getName() == null || student.getName().isBlank()) {
            throw new IllegalArgumentException("Student name cannot be empty.");
        }

        if (student.getEmail() == null || student.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }

        validateMarks(student.getMarks());
        validateAttendance(student.getAttendance());
    }

    private void validateMarks(double marks) {
        if (marks < 0 || marks > 100) {
            throw new IllegalArgumentException("Marks must be between 0 and 100.");
        }
    }

    private void validateAttendance(int attendance) {
        if (attendance < 0 || attendance > 100) {
            throw new IllegalArgumentException("Attendance must be between 0 and 100.");
        }
    }
}
