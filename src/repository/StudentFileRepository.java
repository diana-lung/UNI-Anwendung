package repository;

import exceptions.Validator;
import exceptions.ValidatorException;
import model.Student;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StudentFileRepository implements ICrudRepository<Student> {
    private String fileName;
    private List<Student> students;
    private Validator<Student> validatorStudent;


    public StudentFileRepository(String fileName) throws IOException {
        this.fileName = fileName;
        this.students = new ArrayList<>();

        validatorStudent = new Validator<Student>() {
            @Override
            public void validate(Student student) throws ValidatorException {
            }
        };

        loadData();

    }


    private void loadData() throws IOException {
        Path path = Paths.get(fileName);

        try {
            Files.lines(path).forEach(line ->
            {
                List<String> items = Arrays.asList(line.split(", "));

                int ID = Integer.parseInt(items.get(0));
                String firstName = items.get(1);
                String lastName = items.get(2);
                int credits = Integer.parseInt(items.get(3));

                String almostCoursesString = items.get(4);
                String coursesString = almostCoursesString.substring(1, almostCoursesString.length()-1);
                List<Long> courses = Arrays.stream(coursesString.split(",")).map(Long::parseLong).collect(Collectors.toList());

                Student s = new Student(ID, firstName, lastName, credits, courses);
                try {
                    save(s);
                } catch ( FileNotFoundException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveToFile(Student student) {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {

            String result = student.getEnrolledCourses().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",", "[", "]"));
            String studStr = student.getId().toString() + ", " + student.getFirstName()
                    + ", " + student.getLastName() + ", " + Integer.toString(student.getTotalCredits())
                    + ", " + result;

            bufferedWriter.write(studStr);
            bufferedWriter.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void rewriteFile() throws FileNotFoundException {
        FileOutputStream writer = new FileOutputStream(fileName);
        students.forEach(this::saveToFile);
    }

    @Override
    public Student findOne(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null!\n");
        }

        return students.stream().filter(student -> id.equals(student.getId())).findAny().orElse(null);
    }

    @Override
    public Iterable<Student> findAll() {
        return students;
    }

    @Override
    public Student save(Student student) throws FileNotFoundException {
        if (student==null) {
            throw new IllegalArgumentException("Entity doesn't exist!\n");
        }
        if (findOne(student.getId()) != null)
            return student;
        else {
            students.add(student);
            rewriteFile();
            return null;
        }
    }

    @Override
    public Student delete(Long id) throws FileNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("Entity doesn't exist!\n");
        }
        Student student = findOne(id);
        if (student != null) {
            students.remove(student);
            rewriteFile();
            return student;
        }
        return null;
    }

    @Override
    public Student update(Student student) throws FileNotFoundException {
        if (student == null) {
            throw new IllegalArgumentException("Entity doesn't exist!");
        }
        Student student1 = findOne(student.getId());
        if (student1 != null) {
            students.set(students.indexOf(student1), student);
            rewriteFile();
            return null;
        }
        return student;
    }
}
