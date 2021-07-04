package it.pala.demo.beans;

public class User {

    private final String username;
    private final String name;
    private final String surname;

    public User(String username, String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
