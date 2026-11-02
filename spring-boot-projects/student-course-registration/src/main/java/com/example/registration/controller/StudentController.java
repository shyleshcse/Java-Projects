package com.example.registration.controller;

import com.example.registration.model.Student;
import com.example.registration.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    // Constructor injection: Spring gives us the StudentService object
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ---------- CRUD ----------

    @PostMapping("/register")
    public ResponseEntity<Student> registerStudent(@Valid @RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.registerStudent(student));
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Integer id) {
        return studentService.getStudentById(id);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Integer id, @Valid @RequestBody Student student) {
        return studentService.updateStudent(id, student);
    }

    // PATCH = change only the course: PATCH /api/students/1/course?name=Python
    @PatchMapping("/{id}/course")
    public Student changeCourse(@PathVariable Integer id, @RequestParam("name") String courseName) {
        return studentService.changeCourse(id, courseName);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteRegistration(@PathVariable Integer id) {
        studentService.deleteRegistration(id);
        return Map.of("message", "Registration of student ID " + id + " deleted successfully");
    }

    // ---------- Custom queries ----------

    // GET /api/students/search?keyword=ar
    @GetMapping("/search")
    public List<Student> searchByName(@RequestParam String keyword) {
        return studentService.searchByName(keyword);
    }

    // GET /api/students/course?name=Java Full Stack
    @GetMapping("/course")
    public List<Student> getByCourse(@RequestParam("name") String course) {
        return studentService.getByCourse(course);
    }

    // GET /api/students/email?value=arun@gmail.com
    @GetMapping("/email")
    public Student getByEmail(@RequestParam("value") String email) {
        return studentService.getByEmail(email);
    }

    // GET /api/students/age/above?value=23
    @GetMapping("/age/above")
    public List<Student> getOlderThan(@RequestParam("value") Integer age) {
        return studentService.getOlderThan(age);
    }

    // GET /api/students/age/range?min=20&max=26
    @GetMapping("/age/range")
    public List<Student> getByAgeRange(@RequestParam Integer min, @RequestParam Integer max) {
        return studentService.getByAgeRange(min, max);
    }
}
