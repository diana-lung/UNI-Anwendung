package repository;

import model.Course;
import model.Student;
import model.Teacher;

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

public class CourseFileRepository implements ICrudRepository<Course> {
    private String fileName;
    private List<Course> courses;

    private ICrudRepository<Student> studentRepository;
    private ICrudRepository<Teacher> teacherRepository;


    public CourseFileRepository(String fileName, ICrudRepository<Student> studentICrudRepository, ICrudRepository<Teacher> teacherICrudRepository) throws IOException {
        this.fileName = fileName;
        this.courses = new ArrayList<>();
        this.studentRepository = studentICrudRepository;
        this.teacherRepository = teacherICrudRepository;

        loadData();
    }

    private void loadData() throws IOException {
        Path path = Paths.get(fileName);

        try {
            Files.lines(path).forEach(line ->
            {
                List<String> items = Arrays.asList(line.split(", "));

                Long courseId = Long.parseLong(items.get(0));
                String courseName = items.get(1);

                Long teacherID = Long.parseLong(items.get(2));
                Teacher teacher = teacherRepository.findOne(teacherID);

                int maxEnrollment = Integer.parseInt(items.get(3));
                int credits = Integer.parseInt(items.get(4));

                String almostStudentsEnrolled = items.get(5);
                String studentsString = almostStudentsEnrolled.substring(1, almostStudentsEnrolled.length()-1);
                List<Student> students = Arrays.stream(studentsString.split(","))
                        .map(Long::parseLong).map(sid->studentRepository.findOne(sid)).collect(Collectors.toList());

                Course course = new Course(courseId, courseName, teacher, maxEnrollment, credits, students);
                try {
                    save(course);
                } catch ( FileNotFoundException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveToFile(Course course) {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {

            String studStr = Long.toString(course.getId()) + ", " + course.getName() + ", " + course.getTeacher().getId()
                    + ", " + Integer.toString(course.getMaxEnrollment()) + ", " + Integer.toString(course.getCredits())
                    + ", [" + String.join(",", course.getStudentsEnrolled().stream()
                    .map(student -> Long.toString(student.getId())).collect(Collectors.toList()))+"]";

            bufferedWriter.write(studStr);
            bufferedWriter.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rewriteFile() throws FileNotFoundException {
        FileOutputStream writer = new FileOutputStream(fileName);
        courses.forEach(this::saveToFile);
    }

    @Override
    public Course findOne(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null!\n");
        }

        return courses.stream().filter(course -> id.equals(course.getId())).findAny().orElse(null);
    }

    @Override
    public Iterable<Course> findAll() {
        return courses;
    }

    @Override
    public Course save(Course course) throws FileNotFoundException {
        if (course==null) {
            throw new IllegalArgumentException("Entity doesn't exist!\n");
        }
        if (findOne(course.getId()) != null)
            return course;
        else {
            courses.add(course);
            rewriteFile();
            return null;
        }
    }

    @Override
    public Course delete(Long id) throws FileNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("Entity doesn't exist!\n");
        }
        Course course = findOne(id);
        if (course != null) {
            courses.remove(course);
            rewriteFile();
            return course;
        }
        return null;
    }

    @Override
    public Course update(Course course) throws FileNotFoundException {
        if (course == null) {
            throw new IllegalArgumentException("Entity doesn't exist!");
        }
        Course course1 = findOne(course.getId());
        if (course1 != null) {
            courses.set(courses.indexOf(course1), course);
            rewriteFile();
            return null;
        }
        return course;
    }

}
