package model;

import java.util.List;

/**
 * Kursklasse mit einer String-ID
 */
public class Course {
    private Long id;
    private String name;
    private Teacher teacher;
    private int maxEnrollment;
    private int credits;
    private List<Student> studentsEnrolled;

    /**
     * Konstruktor
     * @param id id
     * @param name name
     * @param teacher den der Kurs vorstellt
     * @param maxEnrollment maximale Anzahl der eingeschriebenen Studenten
     * @param credits Anzahl der credits
     * @param studentsEnrolled Liste der Studenten fur die Kurs
     */
    public Course(Long id, String name, Teacher teacher, int maxEnrollment, int credits, List<Student> studentsEnrolled) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.maxEnrollment = maxEnrollment;
        this.studentsEnrolled = studentsEnrolled;
        this.credits = credits;
    }

    /**
     * Konstruktor ohne Liste von Studenten
     */
    public Course(Long id, String name, Teacher teacher, int maxEnrollment, int credits) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.maxEnrollment = maxEnrollment;
        this.credits = credits;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getMaxEnrollment() {
        return maxEnrollment;
    }

    public void setMaxEnrollment(int maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    public List<Student> getStudentsEnrolled() {
        return studentsEnrolled;
    }

    public void setStudentsEnrolled(List<Student> studentsEnrolled) {
        this.studentsEnrolled = studentsEnrolled;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    /**
     * Gibt die IDs der Studenten aus der Studentenliste als Zeichenfolge zuruck
     * @return String von IDs
     */
    public String studentsIDToString() {
        if (studentsEnrolled.isEmpty())
            return "No existent students yet!";
        else {
            StringBuilder sb = new StringBuilder();
            studentsEnrolled.forEach(student->sb.append(student.getId()).append(", "));
            String s = sb.toString();
            return s.substring(0, s.length() - 2);
        }
    }

    /**
     * Anzeige Funktion
     * @return String von Name, Teacher, MaxEnrollment, credits and IDs der Studenten
     */
    @Override
    public String toString() {
        return "ID: "+id+", Name: " + name + ", Teacher: " + teacher.getFirstName() + " " + teacher.getLastName() + ", Max Nr: " + maxEnrollment +
                ", Nr. Credits: " + credits + ", Students Enrolled: " + studentsIDToString();
    }
}
