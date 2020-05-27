package model;

/**
 * Personklasse mit einer Long Id
 */
public class Person{
    private String firstName;
    private String lastName;

    /**
     * Konstruktor
     * @param firstName Name der Person
     * @param lastName Vorname
     */
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Anzeige Funktion
     * @return String von firstName, lastName
     */
    @Override
    public String toString() {
        return "First Name: " + firstName + ", Last Name: " + lastName;
    }
}

