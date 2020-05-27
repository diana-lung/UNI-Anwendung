package model;

import java.util.List;

/**
 * LehrerKlasse
 */
public class Teacher extends Person {
    private List<Long> courses;
    private long ID;

    /**
     * Konstruktor
     * @param ID id
     * @param firstName Name der Lehrer
     * @param lastName Vorname
     * @param courses Liste von Kurse
     */
    public Teacher(long ID, String firstName, String lastName, List<Long> courses) {
        super(firstName, lastName);
        this.courses = courses;
        this.ID = ID;
    }

    /**
     * Konstruktor ohne Liste von Kurse
     */
    public Teacher(long ID, String firstName, String lastName) {
        super(firstName, lastName);
        this.ID = ID;
    }

    public List<Long> getCourses() {
        return courses;
    }

    public void setCourses(List<Long> courses) {
        this.courses = courses;
    }

    public Long getId() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    /**
     * Gibt die Namen der Kurse aus der Kurseliste als Zeichenfolge zuruck
     * @return String von Namen
     */
    private String coursesNamesToString() {
        if (courses.isEmpty())
            return "Courses: No existent courses yet!";
        else {
            StringBuilder sb = new StringBuilder();
            courses.forEach(id->sb.append(id).append(", "));
            String s = sb.toString();
            return s.substring(0, s.length() - 2);
        }
    }

    /**
     * Anzeige Funktion
     * @return String von firstName,lastName,ID, Name von Kurse
     */
    @Override
    public String toString() {
        return super.toString() + ", ID: " +ID+ ", Courses: " + coursesNamesToString();
    }
}
