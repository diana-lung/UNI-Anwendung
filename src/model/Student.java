package model;

import java.util.List;

/**
 * Studentklasse
 */
public class Student extends Person {
    private long studentId;
    private int totalCredits;
    private List<Long> enrolledCourses; //we will only keep the courses names

    /**
     * Konstruktor
     * @param studentId Id der Student
     * @param firstName Name
     * @param lastName Vorname
     * @param totalCredits Anzahl von Credits fur ein Student
     * @param enrolledCourses Liste der Kurse Namen fur die Studenten
     */
    public Student(long studentId, String firstName, String lastName, int totalCredits, List<Long> enrolledCourses) {
        super(firstName, lastName);
        this.studentId = studentId;
        this.totalCredits = totalCredits;
        this.enrolledCourses = enrolledCourses;
    }

    /**
     * Konstruktor ohne Liste von Kurse
     */
    public Student(long studentId, String firstName, String lastName, int totalCredits) {
        super(firstName, lastName);
        this.studentId = studentId;
        this.totalCredits = totalCredits;
    }

    public Long getId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    public List<Long> getEnrolledCourses() {
        return enrolledCourses;
    }

    public void setEnrolledCourses(List<Long> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }


    /**
     * Gibt die Namen der Kurse aus der Kurseliste als Zeichenfolge zuruck
     * @return String von Namen
     */
    private String coursesNamesToString() {
        if (enrolledCourses.isEmpty())
            return "No existent courses yet!";
        else {
            StringBuilder sb = new StringBuilder();
            enrolledCourses.forEach(id->sb.append(id).append(", "));
            String s = sb.toString();
            return s.substring(0, s.length() - 2);
        }
    }

    /**
     * Anzeige Funktion
     * @return String von studentId, firstName, lastName, totalCredits, courses names
     */
    @Override
    public String toString() {
        return "ID: " + studentId + ", " + super.toString() + ", Total Credits: " + totalCredits + ", Courses: " + coursesNamesToString();
    }
}
