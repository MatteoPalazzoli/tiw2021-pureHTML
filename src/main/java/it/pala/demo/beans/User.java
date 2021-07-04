package it.pala.demo.beans;

import java.util.Objects;

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

    @Override
    public String toString(){
        return username+" ("+name+" "+surname+")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return username.equals(that.username)
                && name.equals(that.name)
                && surname.equals(that.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name, surname);
    }
}
