package view;

import controller.Controller;
import exceptions.ValidatorException;
import model.Course;
import model.Student;
import model.Teacher;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;


/**
 * UI Klasse, die das Menu enthalt und ausfuhrt
 */
public class Console {

    private Controller ctrl;

    public Console(Controller ctrl) {
        this.ctrl = ctrl;
    }

    /**
     * Funktion, die das Menu der Anwendung ausfuhrt
     */
    public void runConsole() throws ValidatorException, FileNotFoundException {
        runMenu();
    }

    /**
     * Funktion, die das Menu der Anwendung anzeigt
     */
    private void runMenu() throws ValidatorException, FileNotFoundException {
        while (true) {
            System.out.print("\n1 - STUDENTS OPERATIONS\n2 - COURSES OPERATIONS\n3 - TEACHER OPERATIONS\n" +
                    "4 - REGISTRATION SYSTEM OPERATIONS\n0 - EXIT\n");
            Scanner sc = new Scanner(System.in);
            int cmd = sc.nextInt();
            switch (cmd) {
                case 0: {
                    System.exit(0);
                }
                case 1:
                    while (true) {
                        System.out.print("\n1 - Add new student\n" +
                                "2 - Update student\n" +
                                "3 - Delete student\n" +
                                "4 - View student\n" +
                                "5 - Display all students\n" +
                                "6 - Filter students by name\n" +
                                "0 - Back\n");
                        int cmd2 = sc.nextInt();
                        if (cmd2 == 0)
                            break;
                        switch (cmd2) {
                            case 1:
                                addStudent();
                                break;
                            case 2:
                                updateStudent();
                                break;
                            case 3:
                                deleteStudent();
                                break;
                            case 4:
                                viewStudent();
                                break;
                            case 5:
                                viewAllStudents();
                                break;
                            case 6:
                                filterStudents();
                                break;
                        }
                    }
                    break;
                case 2:
                    while (true) {
                        System.out.print("\n1 - Add new course\n" +
                                "2 - Update course\n" +
                                "3 - Delete course\n" +
                                "4 - View course\n" +
                                "5 - Display courses\n" +
                                "6 - Sort courses by credits\n" +
                                "0 - Back\n");
                        int cmd2 = sc.nextInt();
                        if (cmd2 == 0)
                            break;
                        switch (cmd2) {
                            case 1:
                                addCourse();
                                break;
                            case 2:
                                updateCourse();
                                break;
                            case 3:
                                deleteCourse();
                                break;
                            case 4:
                                viewCourse();
                                break;
                            case 5:
                                viewCourses();
                                break;
                            case 6:
                                sortCourses();
                                break;
                        }
                    }
                    break;
                case 3:
                    while (true) {
                        System.out.print("\n1 - View teacher\n" +
                                "2 - Display teachers\n" +
                                "0 - Back\n");
                        int cmd3 = sc.nextInt();
                        if (cmd3 == 0)
                            break;
                        switch (cmd3) {
                            case 1:
                                viewTeacher();
                                break;
                            case 2:
                                viewTeachers();
                                break;
                        }
                    }
                    break;
                case 4:
                    while (true) {
                        System.out.print("\n1 - Register\n" +
                                "2 - Show courses with available places\n" +
                                "3 - Show students enrolled for a course\n" +
                                "4 - Display all courses\n" +
                                "0 - Back\n");
                        int cmd4 = sc.nextInt();
                        if (cmd4 == 0)
                            break;
                        switch (cmd4) {
                            case 1:
                                register();
                                break;
                            case 2:
                                retrieveCoursesWithFreePlaces();
                                break;
                            case 3:
                                retrieveStudentsEnrolledForACourse();
                                break;
                            case 4:
                                viewCourses();
                                break;
                        }
                    }
                    break;
                default: {
                    System.out.println("Invalid command!\n");
                }
            }
        }
    }


    /**
     * Funktion, die Daten fur einen neuen Student liest, der gespeichert werden soll
     */
    private void addStudent() throws FileNotFoundException, ValidatorException {
        //Lesen wir die Daten
        Scanner sc = new Scanner(System.in);
        System.out.println("Read student information");
        System.out.print("ID: ");
        long studentId = sc.nextLong();
        System.out.print("First name: ");
        String firstName = sc.next();
        System.out.print("Last Name: ");
        String lastName = sc.next();

        //Wir rufen die addStudent Funktion vom dem Controller mit den gelesenen Daten auf
        if (ctrl.addStudent(studentId, firstName, lastName)) {
            System.out.println("Please add the courses later from the Registration System menu!");
            System.out.println("Student saved with success!");
        } else
            System.out.println("Student with this ID already exists!");
    }

    /**
     * Funktion, die neue Daten liest, um einen Student mit einer bestimmten ID zu aktualisieren
     */
    private void updateStudent() throws ValidatorException, FileNotFoundException {
        //Lesen wir die Daten
        Scanner sc = new Scanner(System.in);
        System.out.print("ID of the student to be updated: ");
        long ID = sc.nextLong();
        System.out.print("New First Name: ");
        String newFirstName = sc.next();
        System.out.print("New Last Name: ");
        String newLastName = sc.next();

        //Wir rufen die updateStudent Funktion vom dem Controller mit den gelesenen Daten auf
        if (ctrl.updateStudent(ID, newFirstName, newLastName)) {
            System.out.println("Courses can be updated only from the Registration System menu!");
            System.out.println("Student updated with success!");
        } else
            System.out.println("Student with this ID doesn't exist!");
    }

    /**
     * Funktion, die einen Student mit einer bestimmten ID loscht
     */
    private void deleteStudent() throws FileNotFoundException {
        //Lesen wir die ID
        Scanner sc = new Scanner(System.in);
        System.out.print("ID of the student to be deleted: ");
        long ID = sc.nextLong();

        //Wir rufen die deleteStudent Funktion vom dem Controller mit den gelesenen Daten auf
        if(ctrl.deleteStudent(ID))
            System.out.println("Student deleted with success!");
        else
            System.out.println("Student with this ID doesn't exist!");
    }

    /**
     * Funktion, die Daten für einen neuen Kurs liest, der gespeichert werden soll
     */
    private void addCourse() throws ValidatorException, FileNotFoundException {
        //Lesen wir die Daten
        Scanner sc = new Scanner(System.in);
        Teacher t;
        System.out.println("Read course information");
        System.out.print("Course Id: ");
        Long id = Long.parseLong(sc.next());
        System.out.print("Name: ");
        String courseName = sc.next();
        while (true) { //Wir konnen nur einen Lehrer auswahlen, der bereits existiert
            System.out.print("Teacher ID: ");
            long ID = sc.nextLong();
            t = ctrl.getTeacherRepo().findOne(ID);
            if (t == null)
                System.out.println("Teacher with this ID doesn't exist! Please try again!");
            else
                break;
        }
        System.out.print("Maximum nr. of students: ");
        int maxNr = sc.nextInt();
        System.out.print("Credits: ");
        int credits = sc.nextInt();

        //Wir rufen die addCourse Funktion vom dem Controller mit den gelesenen Daten auf
        if(ctrl.addCourse(id, courseName, t, maxNr, credits))
            System.out.println("Course saved with success!");
        else
            System.out.println("Course with this id already exists!");
    }

    /**
     * Funktion, die neue Daten liest, um einen Kurs mit einer bestimmten Name zu aktualisieren
     */
    private void updateCourse() throws ValidatorException, FileNotFoundException {
        //Lesen wir die Daten
        Scanner sc = new Scanner(System.in);
        Teacher newTeacher;
        int newMaxNr;
        System.out.print("Course Id: ");
        Long id = Long.parseLong(sc.next());
        System.out.print("New name: ");
        String name = sc.next();
        //Wir konnen nur einen neuer Lehrer auswahlen, der bereits existiert
        while (true) {
            System.out.print("New teacher ID: ");
            long ID = sc.nextLong();
            newTeacher = ctrl.getTeacherRepo().findOne(ID);
            if (newTeacher == null)
                System.out.println("Teacher with this ID doesn't exist! Please try again!");
            else
                break;
        }
        int nrStudentEnrolledAlready = ctrl.getCourse(id).getStudentsEnrolled().size();
        /*
        Wir konnen die maximale Anzahl von Studenten nur auf eine Zahl andern, die großer als die aktuelle Anzahl
        von eingeschreibenen Studenten ist
         */
        while (true) {
            System.out.print("New maximum number of students: ");
            newMaxNr = sc.nextInt();
            if (newMaxNr < nrStudentEnrolledAlready)
                System.out.println("There are more than " + newMaxNr + " students already taking this course. ("
                        + nrStudentEnrolledAlready + ") Try again!");
            else
                break;
        }
        System.out.print("New number of credits: ");
        int newNrCredits = sc.nextInt();

        //Wir rufen die updateCourse Funktion vom dem Controller mit den gelesenen Daten auf
        if(ctrl.updateCourse(id, name, newTeacher, newMaxNr, newNrCredits))
            System.out.println("Course updated with success!");
         else
            System.out.println("Course with this ID doesn't exist!");
    }

    /**
     * Funktion, die einen Kurs mit einer bestimmten Name loscht
     */
    private void deleteCourse() throws FileNotFoundException, ValidatorException {
        //Lesen wir die Name
        Scanner sc = new Scanner(System.in);
        System.out.print("Course id: ");
        Long id = Long.parseLong(sc.next());

        //Wir rufen die deleteCourse Funktion vom dem Controller mit den gelesenen Daten auf
        if(ctrl.deleteCourse(id))
            System.out.println("Course deleted with success!");
        else
            System.out.println("Course with this id doesn't exist!");
    }

    /**
     * Funktion in die wir Daten lesen fur ein Studenten, sich fur einen bestimmten Kurs anzumelden
     */
    private void register() throws FileNotFoundException, ValidatorException {
        //Lesen wir die Daten
        Scanner sc = new Scanner(System.in);
        Student s;
        Course c;
        //Der Studenten ID muss gültig sein
        while (true) {
            System.out.print("Student ID: ");
            long ID = sc.nextLong();
            s = ctrl.getStudent(ID);
            if (s == null)
                System.out.println("Student with this ID doesn't exist! Please try again!");
            else
                break;
        }
        //Der Kursname muss vorhanden sein
        while (true) {
            System.out.print("Course ID: ");
            Long id = Long.parseLong(sc.next());
            c = ctrl.getCourse(id);
            if (c == null)
                System.out.println("Course with this ID  doesn't exist! Please try again!");
            else
                break;
        }

        //Wir rufen die resgister Funktion vom dem Controller mit den gelesenen Daten auf
        int res=ctrl.register(s, c);

        if (res==3)
            System.out.println("Student registred with success!");
        else
            if(res==0)
                System.out.println("You can't choose this course. Course doesn't have any free places!");
            else
                if(res==1)
                    System.out.println("You can't choose this course. Maximum number of credits exceeded!");
                else
                    System.out.println("Student already takes this course!");
    }

    /**
     * Funktion anzeigt die Kurse, für die Platze verfugbar sind, und deren Anzahl
     */
    private void retrieveCoursesWithFreePlaces() {
        //Wir rufen die coursesWithFreePlaces Funktion von dem Controller auf
        System.out.println("Courses with free places:");
        ctrl.retrieveCoursesWithFreePlaces();
    }

    /**
     * Funktion zeigt die Studenten, die sich fur einen bestimmten Kurs angemeldet haben
     */
    private void retrieveStudentsEnrolledForACourse() {
        Scanner sc = new Scanner(System.in);
        Course c;
        while (true) {//Der Kursname sollte existieren
            System.out.print("Course ID: ");
            Long id = Long.parseLong(sc.next());
            c = ctrl.getCourse(id);
            if (c == null)
                System.out.println("Course with this ID doesn't exist! Please try again!");
            else
                break;
        }

        //Wir rufen die eingeschriebenen Studenten Funktion von dem Controller
        System.out.println("Students enrolled for the course: ");
        ctrl.retrieveStudentsEnrolledForACourse(c);
    }

    /**
     * Funktion zum Anzeigen die Student mit der angegebenen ID
     */
    private void viewStudent() {
        //Lesen wir die ID
        Scanner sc = new Scanner(System.in);
        System.out.print("ID of the student to be viewed: ");
        long ID = sc.nextLong();

        Student s = ctrl.getStudent(ID);
        if (s != null)
            System.out.println(s.toString()); //wir zeigen die Student
        else
            System.out.println("Student with this ID doesn't exist!");
    }

    /**
     * Funktion zum Anzeigen aller Studenten
     */
    private void viewAllStudents() {
        Iterable<Student> students = ctrl.getStudentRepo().findAll();
        students.forEach(System.out::println);
    }

    /**
     * Funktion zum Anzeigen die Kurs mit der angegebenen Name
     */
    private void viewCourse() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID of the course to be viewed: ");
        Long id = Long.parseLong(sc.next());

        Course c = ctrl.getCourse(id);
        if (c != null)
            System.out.println(c.toString());
        else
            System.out.println("Course with this ID doesn't exist!");
    }

    /**
     * Funktion zum Anzeigen aller Kurse
     */
    private void viewCourses() {
        Iterable<Course> courses = ctrl.getCourseRepo().findAll();
        courses.forEach(System.out::println);
    }

    /**
     * Funktion zum Anzeigen die Lehrer mit der angegebenen ID
     */
    private void viewTeacher() {
        //Lesen wir die ID
        Scanner sc = new Scanner(System.in);
        System.out.print("ID of the teacher to be viewed: ");
        long ID = sc.nextLong();

        Teacher t = ctrl.getTeacherRepo().findOne(ID);
        if (t != null)
            System.out.println(t.toString());
        else
            System.out.println("Teacher with this ID doesn't exist!");
    }

    /**
     * Funktion zum Anzeigen aller Lehrern
     */
    private void viewTeachers() {
        Iterable<Teacher> teachers = ctrl.getTeacherRepo().findAll();
        teachers.forEach(System.out::println);
    }

    /**
     * Function that gets an input string and uses it to call the filter students method from the Controller
     */
    private void filterStudents() {
        Scanner sc = new Scanner(System.in);
        System.out.print("The string with which you want the student first name to start: ");
        String anAwesomeString = sc.next();

        System.out.println("Filtered students (the ones whose first name start with " + anAwesomeString + "):");
        Set<Student> students = ctrl.filterStudentsByFirstName(anAwesomeString);
        if (students.isEmpty())
            System.out.println("There isnt't any student whose name starts with the given string!");
        else
            students.forEach(System.out::println);
    }

    /**
     * Function that displays the sorted courses
     */
    private void sortCourses() {
        ctrl.sortCourses().forEach(System.out::println);
    }

}
