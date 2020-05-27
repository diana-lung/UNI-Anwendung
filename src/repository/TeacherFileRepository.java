package repository;

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

public class TeacherFileRepository implements ICrudRepository<Teacher> {
    private String fileName;
    private List<Teacher> teachers;


    public TeacherFileRepository(String fileName) throws IOException {
        this.fileName = fileName;
        this.teachers = new ArrayList<>();

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

                String almostCoursesString = items.get(3);
                String coursesString = almostCoursesString.substring(1, almostCoursesString.length()-1);
                List<Long> courses = Arrays.stream(coursesString.split(",")).map(Long::parseLong).collect(Collectors.toList());

                Teacher t = new Teacher(ID, firstName, lastName, courses);
                try {
                    save(t);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveToFile(Teacher teacher) {
        Path path = Paths.get(fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {

            String result = teacher.getCourses().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",", "[", "]"));

            String studStr = teacher.getId().toString() + ", " + teacher.getFirstName()
                    + ", " + teacher.getLastName() + ", "
                    + result ;

            bufferedWriter.write(studStr);
            bufferedWriter.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void rewriteFile() throws FileNotFoundException {
        FileOutputStream writer = new FileOutputStream(fileName);
        teachers.forEach(this::saveToFile);
    }

    @Override
    public Teacher findOne(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null!\n");
        }

        return teachers.stream().filter(teacher -> id.equals(teacher.getId())).findAny().orElse(null);
    }

    @Override
    public Iterable<Teacher> findAll() {
        return teachers;
    }

    @Override
    public Teacher save(Teacher teacher) throws FileNotFoundException {
        if (teacher==null) {
            throw new IllegalArgumentException("Entity doesn't exist!\n");
        }
        if (findOne(teacher.getId()) != null)
            return teacher;
        else {

            teachers.add(teacher);
            rewriteFile();
            return null;
        }
    }

    @Override
    public Teacher delete(Long id) throws FileNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("Entity doesn't exist!\n");
        }
        Teacher teacher = findOne(id);
        if (teacher != null) {
            teachers.remove(teacher);
            rewriteFile();
            return teacher;
        }
        return null;
    }

    @Override
    public Teacher update(Teacher teacher) throws FileNotFoundException {
        if (teacher == null) {
            throw new IllegalArgumentException("Entity doesn't exist!");
        }
        Teacher teacher1 = findOne(teacher.getId());
        if (teacher1 != null) {
            teachers.set(teachers.indexOf(teacher1), teacher);
            rewriteFile();
            return null;
        }
        return teacher;
    }
}
