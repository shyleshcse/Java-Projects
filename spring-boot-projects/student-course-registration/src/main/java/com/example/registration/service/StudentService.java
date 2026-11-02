package com.example.registration.service;

import com.example.registration.exception.DuplicateRegistrationException;
import com.example.registration.exception.StudentNotFoundException;
import com.example.registration.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// All the business logic. Data lives in an ArrayList (no database yet).
@Service
public class StudentService {

    private final List<Student> students = new ArrayList<>();
    private int nextId = 1;

    // A few sample students so you can test GET requests immediately
    public StudentService() {
        students.add(new Student(nextId++, "Arun Kumar", "Java Full Stack", "arun@gmail.com", 22));
        students.add(new Student(nextId++, "Priya Sharma", "Python", "priya@gmail.com", 25));
        students.add(new Student(nextId++, "Karthik Raj", "Java Full Stack", "karthik@gmail.com", 30));
    }

    // ---------- CRUD ----------

    public Student registerStudent(Student student) {
        checkDuplicateEmail(student.getEmail(), null);
        student.setStudentId(nextId++);
        student.setStudentName(student.getStudentName().trim());
        student.setCourseName(student.getCourseName().trim());
        student.setEmail(student.getEmail().trim());
        students.add(student);
        return student;
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public Student getStudentById(Integer id) {
        return students.stream()
                .filter(student -> student.getStudentId().equals(id))
                .findFirst()
                .orElseThrow(() -> new StudentNotFoundException("Student not found with ID: " + id));
    }

    public Student updateStudent(Integer id, Student updated) {
        Student existing = getStudentById(id);
        checkDuplicateEmail(updated.getEmail(), id);
        existing.setStudentName(updated.getStudentName().trim());
        existing.setCourseName(updated.getCourseName().trim());
        existing.setEmail(updated.getEmail().trim());
        existing.setAge(updated.getAge());
        return existing;
    }

    // Partial update: only the course changes
    public Student changeCourse(Integer id, String courseName) {
        if (courseName == null || courseName.isBlank()) {
            throw new IllegalArgumentException("Course name must not be blank");
        }
        Student existing = getStudentById(id);
        existing.setCourseName(courseName.trim());
        return existing;
    }

    public void deleteRegistration(Integer id) {
        boolean removed = students.removeIf(student -> student.getStudentId().equals(id));
        if (!removed) {
            throw new StudentNotFoundException("Student not found with ID: " + id);
        }
    }

    // ---------- Custom queries ----------

    // 1. Partial match: name contains the keyword (ignore case)
    public List<Student> searchByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Search keyword must not be blank");
        }
        String text = keyword.trim().toLowerCase();
        return students.stream()
                .filter(student -> student.getStudentName().toLowerCase().contains(text))
                .toList();
    }

    // 2. Full match: course equals (ignore case)
    public List<Student> getByCourse(String course) {
        if (course == null || course.isBlank()) {
            throw new IllegalArgumentException("Course name must not be blank");
        }
        return students.stream()
                .filter(student -> student.getCourseName().equalsIgnoreCase(course.trim()))
                .toList();
    }

    // 3. Full match: find one student by email
    public Student getByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be blank");
        }
        return students.stream()
                .filter(student -> student.getEmail().equalsIgnoreCase(email.trim()))
                .findFirst()
                .orElseThrow(() -> new StudentNotFoundException("No student registered with email: " + email));
    }

    // 4. Numeric filter: age greater than the given value
    public List<Student> getOlderThan(Integer age) {
        if (age == null || age < 0) {
            throw new IllegalArgumentException("Age must be zero or more");
        }
        return students.stream()
                .filter(student -> student.getAge() > age)
                .toList();
    }

    // 5. Numeric range: min <= age <= max
    public List<Student> getByAgeRange(Integer min, Integer max) {
        if (min == null || max == null || min < 0 || max < 0) {
            throw new IllegalArgumentException("Minimum and maximum age must be provided and not negative");
        }
        if (min > max) {
            throw new IllegalArgumentException("Minimum age cannot be greater than maximum age");
        }
        return students.stream()
                .filter(student -> student.getAge() >= min && student.getAge() <= max)
                .toList();
    }

    // ---------- helper ----------

    // ignoreId = the student being updated (so it does not clash with itself)
    private void checkDuplicateEmail(String email, Integer ignoreId) {
        boolean exists = students.stream()
                .filter(student -> ignoreId == null || !student.getStudentId().equals(ignoreId))
                .anyMatch(student -> student.getEmail().equalsIgnoreCase(email.trim()));
        if (exists) {
            throw new DuplicateRegistrationException("Email already registered: " + email.trim());
        }
    }
}
