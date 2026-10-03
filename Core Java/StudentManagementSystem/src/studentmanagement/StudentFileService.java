package studentmanagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentFileService {
    private final String filePath;

    public StudentFileService(String filePath) {
        this.filePath = filePath;
    }

    public void saveStudents(List<Student> students) throws IOException {
        File file = new File(filePath);

        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Student student : students) {
                writer.write(student.toFileString());
                writer.newLine();
            }
        }
    }

    public List<Student> loadStudents() throws IOException {
        List<Student> students = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return students;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                try {
                    students.add(Student.fromFileString(line));
                } catch (RuntimeException e) {
                    System.out.println("Skipping invalid record: " + line);
                }
            }
        }

        return students;
    }
}
