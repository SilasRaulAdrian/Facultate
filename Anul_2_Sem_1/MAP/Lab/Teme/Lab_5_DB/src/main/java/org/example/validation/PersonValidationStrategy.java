package org.example.validation;

import org.example.model.Person;

import java.time.format.DateTimeFormatter;

/**
 * Clasa PersonValidationStrategy care implementeaza interfata ValidationStrategy
 * pentru validarea obiectelor de tip Person
 */
public class PersonValidationStrategy implements ValidationStrategy {
    /**
     * Valideaza un obiect de tip Person
     * @param args parametrii necesari pentru validare
     * @throws IllegalArgumentException daca validarea esueaza
     */
    @Override
    public void validate(Object... args) throws IllegalArgumentException {
        if (args.length == 0 || !(args[0] instanceof Person person)) {
            throw new IllegalArgumentException("Invalid user type. Expected Person.");
        }

        if (person.getUsername() == null || person.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        if (person.getEmail() == null || person.getEmail().isBlank() ||
                !person.getEmail().contains("@") || !person.getEmail().contains(".")) {
            throw new IllegalArgumentException("Email address cannot be empty or it is invalid.");
        }

        if (person.getPassword() == null || person.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (person.getNivelEmpatie() < 0 || person.getNivelEmpatie() > 10) {
            throw new IllegalArgumentException("Empathy level must be between 0 and 10.");
        }

        if (person.getOcupatie() == null || person.getOcupatie().isBlank()) {
            throw new IllegalArgumentException("Occupation cannot be empty.");
        }

        if (person.getNume() == null || person.getNume().isBlank()) {
            throw new IllegalArgumentException("Last name cannot be empty.");
        }

        if (person.getPrenume() == null || person.getPrenume().isBlank()) {
            throw new IllegalArgumentException("First name cannot be empty.");
        }

        if (person.getDataNasterii() == null) {
            throw new IllegalArgumentException("Date of birth cannot be null.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            formatter.format(person.getDataNasterii());
        } catch (Exception e) {
            throw new IllegalArgumentException("Date of birth must be in the format yyyy-MM-dd.");
        }
    }
}
