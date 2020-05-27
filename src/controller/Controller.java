package controller;

import exceptions.Validator;
import exceptions.ValidatorException;
import model.Course;
import model.Student;
import model.Teacher;
import repository.ICrudRepository;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Controller {
    private ICrudRepository<Course> courseRepo;
    private ICrudRepository<Student> studentRepo;
    private ICrudRepository<Teacher> teacherRepo;
    private Validator<Student> validatorStudent;
    private Validator<Course> validatorCourse;
    private Validator<Teacher> validatorTeacher;

    /**
     * Konstruktor mit den 3 Repositories als Parameter
     *
     * @param courseRepo  die Kursen Liste
     * @param studentRepo die Studenten Liste
     * @param teacherRepo die Lehrern Liste
     */
    public Controller(ICrudRepository<Course> courseRepo, ICrudRepository<Student> studentRepo,
                      ICrudRepository<Teacher> teacherRepo) {
        this.courseRepo = courseRepo;
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;

        validatorStudent = new Validator<Student>() {
            @Override
            public void validate(Student student) throws ValidatorException {
            }
        };

        validatorCourse = new Validator<Course>() {
            @Override
            public void validate(Course course) throws ValidatorException {
            }
        };

        validatorTeacher = new Validator<Teacher>() {
            @Override
            public void validate(Teacher teacher) throws ValidatorException {
            }
        };

    }

    public ICrudRepository<Course> getCourseRepo() {
        return courseRepo;
    }

    public void setCourseRepo(ICrudRepository<Course> courseRepo) {
        this.courseRepo = courseRepo;
    }

    public ICrudRepository<Student> getStudentRepo() {
        return studentRepo;
    }

    public void setStudentRepo(ICrudRepository<Student> studentRepo) {
        this.studentRepo = studentRepo;
    }

    public ICrudRepository<Teacher> getTeacherRepo() {
        return teacherRepo;
    }

    public void setTeacherRepo(ICrudRepository<Teacher> teacherRepo) {
        this.teacherRepo = teacherRepo;
    }

    /**
     * Funktion, um einen Kurs zu finden
     *
     * @param id die ID des Kurses
     * @return der gefundene Kurs
     */
    public Course getCourse(Long id) {
        return courseRepo.findOne(id);
    }

    /**
     * Funktion, um einen Student zu finden
     *
     * @param ID die ID des Studenten
     * @return der gefundene Student
     */
    public Student getStudent(long ID) {
        return studentRepo.findOne(ID);
    }

    /**
     * Funktion, die versucht, einen neuen Student zu speichern
     *
     * @param studentId die ID des Studenten
     * @param firstName die first Name des Studenten
     * @param lastName  die last Name des Studenten
     */
    public boolean addStudent(long studentId, String firstName, String lastName) throws FileNotFoundException, ValidatorException {
        List<Long> courses = new ArrayList<>();
        //Wir erstellen einen neuen Student mit den Daten und versuchen, sie zu speichern
        Student s = new Student(studentId, firstName, lastName, 0, courses);

        validatorStudent.validate(s);
        return studentRepo.save(s) == null;
    }

    /**
     * Funktion, die versucht, einen Student zu aktualisieren
     *
     * @param ID           die ID des Studenten
     * @param newFirstName die neue first Name des Studenten
     * @param newLastName  die neue last Name des Studenten
     */
    public boolean updateStudent(long ID, String newFirstName, String newLastName) throws ValidatorException, FileNotFoundException {
        //Wir aktualisieren die Studentendaten, wenn der Student mit dem angegebenen ID existiert
        Student initial = getStudent(ID);
        if (initial != null) {
            Student s = new Student(ID, newFirstName, newLastName, initial.getTotalCredits(), initial.getEnrolledCourses());
            validatorStudent.validate(s);
            return true;
        } else
            return false;
    }

    /**
     * Funktion, die versucht, einen Student zu löschen
     *
     * @param ID die ID des Studenten
     * @return
     */
    public boolean deleteStudent(long ID) throws FileNotFoundException {
        //Wir versuchen das Element zu loschen
        Student s = studentRepo.delete(ID);
        if (s != null) {
            //Wenn der Student geloscht wurde, wird er auch aus allen Kursen entfernt, an denen er teilgenommen hat
            s.getEnrolledCourses().forEach(id ->{
                Course c=getCourse(id);
                c.getStudentsEnrolled().remove(s);
                try {
                    courseRepo.update(c);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
            return true;
        } else
            return false;
    }

    /**
     * Funktion, die versucht, einen neuen Kurs hinzuzufügen
     *
     * @param courseId   id des Kurses
     * @param courseName name des Kurses
     * @param t          teacher fur die Kurse
     * @param maxNr      maxim Anzahl von Studenten fur die Kurse
     * @param credits    die Anzahl von credits fur die Kurse
     */
    public boolean addCourse(Long courseId, String courseName, Teacher t, int maxNr, int credits) throws ValidatorException, FileNotFoundException {
        //Wir erstellen einen neuen Kurs mit den Daten und versuchen, sie zu speichern
        List<Student> students = new ArrayList<>();
        Course c = new Course(courseId, courseName, t, maxNr, credits, students);
        validatorCourse.validate(c);
        if (courseRepo.save(c) == null) {
            List<Long> newCourses = new ArrayList<>(t.getCourses());
            newCourses.add(c.getId());
            t.setCourses(newCourses); //Wir fügen den neuen Kurs der Lehrerliste hinzu

            teacherRepo.update(t);
            return true;
        } else
            return false;
    }

    /**
     * Funktion, die versucht, einen Kurs zu aktualisieren
     *  @param id           die Id des Kurs
     * @param name         die Name des Kurs
     * @param newTeacher   neue teacher fur die Kurse
     * @param newMaxNr     neue maxim Anzahl von Studenten fur die Kurse
     * @param newNrCredits die neue Anzahl von credits fur die Kurse
     * @return
     */
    public boolean updateCourse(Long id, String name, Teacher newTeacher, int newMaxNr, int newNrCredits) throws ValidatorException, FileNotFoundException {
        //Wir aktualisieren die Kursdaten
        Course initial = getCourse(id); //speichern wir die initiale Kurs
        Course c = new Course(id, name, newTeacher, newMaxNr, newNrCredits, initial.getStudentsEnrolled());
        validatorCourse.validate(c);
        List<Student> students = new ArrayList<>(c.getStudentsEnrolled());
        if (courseRepo.update(c) == null) {
            //Wir andern die Anzahl der Credits der Studenten, die diesen Kurs belegen
            students.forEach(student ->{ student.setTotalCredits(student.getTotalCredits() - initial.getCredits() + newNrCredits);
                try {
                    studentRepo.update(student);
                } catch ( FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
            //Wir uberprufen für jeden Studenten, der den aktualisierten Kurs besucht, ob der neue Credits-Betrag großer als 30 ist
            students.stream().filter(student -> student.getTotalCredits() > 30).forEach(student -> {
                student.getEnrolledCourses().remove(c.getId()); //Wenn ja, loschen wir den Kurs vom die Student KursList
                c.getStudentsEnrolled().remove(student); //und der Student aus der Liste der Kursteilnehmer
                student.setTotalCredits(student.getTotalCredits() - newNrCredits);//Wir andern die Anzahl der Credits
                try {
                    studentRepo.update(student);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    courseRepo.update(c);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });

            initial.getTeacher().getCourses().remove(initial.getId()); //Wir loschen den Kurs von der vorherige LehrerListe hinzu
            newTeacher.getCourses().add(c.getId()); //Wir fugen den updated Kurs Name der neuer LehrerListe hinzu
            return true;
        } else
            return false;
    }

    /**
     * Funktion, die versucht, einen Kurs zu löschen
     * @param id ID des Kurses
     */
    public boolean deleteCourse(Long id) throws FileNotFoundException {
        //Wir versuchen das Element zu loschen
        Course c = courseRepo.delete(id);
        if (c != null) {
            c.getTeacher().getCourses().remove(c.getId()); //Wir loschen den Kurs name aus der Liste der Lehrer auch
            getTeacherRepo().update(c.getTeacher());
            //wir loschen den Kurs name aus der kursteilnehmerliste und andern die anzahl der credits
            c.getStudentsEnrolled().forEach(s -> {
                s.getEnrolledCourses().remove(c.getId());
                s.setTotalCredits(s.getTotalCredits() - c.getCredits());
                try {
                    studentRepo.update(s);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
            return true;
        } else
            return false;
    }

    /**
     * Findet die Anzahl der freien Platze von eine Kurs
     * @param c gegebene Kurs
     * @return die Anzahl der freien Platze
     */
    public int freePlaces(Course c) {
        if (c.getStudentsEnrolled().isEmpty())
            return c.getMaxEnrollment();
        return c.getMaxEnrollment() - c.getStudentsEnrolled().size();
    }

    /**
     * Funktion in die wir Daten lesen fur ein Studenten, sich fur einen bestimmten Kurs anzumelden
     * @param student Stundet zu registrieren
     * @param course Kurs fur was zu registrieren
     */
    public int register(Student student, Course course) throws FileNotFoundException, ValidatorException {
        if (freePlaces(course) <= 0) {//Der Kurs hat keine freien Plätze
            return 0;
        }
        if (student.getTotalCredits() + course.getCredits() > 30) {//Maximale Anzahl Credits überschritten
            return 1;
        }
        if (student.getEnrolledCourses().stream().filter(courseId -> courseId.equals(course.getId())).findAny().
                orElse(null) != null) {
            return 2;
        }

        //Wir fugen den Kurs Name in die Studenten Kursliste und die Student in die Kurs Teilnehmerliste ein
        course.getStudentsEnrolled().add(student);
        student.getEnrolledCourses().add(course.getId());
        student.setTotalCredits(student.getTotalCredits() + course.getCredits()); //Wir aktualisieren die Anzahl der Credits
        courseRepo.update(course);
        studentRepo.update(student);
        return 3;
    }

    /**
     * Funktion anzeigt die Kurse, für die Platze verfugbar sind, und deren Anzahl
     */
    public List<Course> retrieveCoursesWithFreePlaces() {
        List<Course> courses = (List<Course>) courseRepo.findAll();
        //Halten Sie nur Kurse mit freien Platzen
        return courses.stream().filter(course -> freePlaces(course) > 0).collect(Collectors.toList());
    }
    /**
     * Funktion zeigt die Studenten, die sich fur einen bestimmten Kurs angemeldet haben
     */
    public void retrieveStudentsEnrolledForACourse(Course course) {
        course.getStudentsEnrolled().forEach(student -> System.out.print(student.getId() + ", "));
    }


    /**
     * Filter students who's first name start with the given string
     * @param s the string
     * @return the filtered students
     */
    public Set<Student> filterStudentsByFirstName(String s) {
        Iterable<Student> students = studentRepo.findAll();
        return StreamSupport.stream(students.spliterator(), false)
                .filter(student -> student.getFirstName().startsWith(s)).collect(Collectors.toSet());
    }

    public List<Student> filterStudents1() {
        Iterable<Student> students = studentRepo.findAll();
        return StreamSupport.stream(students.spliterator(), false)
                .filter(c -> c.getTotalCredits()<16).collect(Collectors.toList());
    }

    public List<Student> filterStudents2() {
        Iterable<Student> students = studentRepo.findAll();
        return StreamSupport.stream(students.spliterator(), false)
                .filter(c -> c.getTotalCredits()<31 && c.getTotalCredits()>15).collect(Collectors.toList());
    }

    public String courseToString(List<Course> l){
        if (l.isEmpty())
            return "No existent courses!";
        else {
            StringBuilder sb = new StringBuilder();
            l.forEach(c->sb.append(c.getName()).append(", "));
            String s = sb.toString();
            return s.substring(0, s.length() - 2);
        }

    }

    public String placesToString(List<Course> l){
        if (l.isEmpty())
            return "No existent courses!";
        else {
            StringBuilder sb = new StringBuilder();
            l.forEach(c->sb.append(freePlaces(c)).append(", "));
            String s = sb.toString();
            return s.substring(0, s.length() - 2);
        }

    }

    public List<Course> retrieveCoursesWithoutStud(){
        List<Course> courses = (List<Course>) courseRepo.findAll();
        //Halten Sie nur Kurse mit freien Platzen
        return courses.stream().filter(course -> course.getStudentsEnrolled().size() <= 0).collect(Collectors.toList());
    }

    /**
     * Sorts courses descending by their number of credits
     * @return the sorted courses
     */
    public List<Course> sortCourses(){
        return StreamSupport.stream(courseRepo.findAll().spliterator(),false)
                .sorted((course1,course2)->course2.getCredits()-course1.getCredits())
                .collect(Collectors.toList());
    }

    public String studToString(List<Student> l){
        if (l.isEmpty())
            return "No existent students!";
        else {
            StringBuilder sb = new StringBuilder();
            l.forEach(c->sb.append(c.getFirstName()).append(" ").append(c.getLastName()).append(", "));
            String s = sb.toString();
            return s.substring(0, s.length() - 2);
        }

    }

    //sort by first name
    public List<Student> sortStudents(){
        return StreamSupport.stream(studentRepo.findAll().spliterator(),false)
                .sorted((stud1,stud2)->stud1.getFirstName().compareTo(stud2.getFirstName()))
                .collect(Collectors.toList());
    }

   /* public List<Course> sortCoursesString(){
        return StreamSupport.stream(courseRepo.findAll().spliterator(),false)
                .sorted((course1,course2)->course2.getName().compareTo(course1.getName()))
                .collect(Collectors.toList());
    }
   */

}

