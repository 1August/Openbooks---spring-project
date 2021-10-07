package com.example.openbooksspring;

import java.util.Objects;

public class Book {
    private int id;
    private String name;
    private String authorLogin;
    private int year;
    private String description;

    public Book(int id, String name, String authorLogin, int year, String description) {
        this.id = id;
        this.name = name;
        this.authorLogin = authorLogin;
        this.year = year;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return id == book.id && year == book.year && Objects.equals(name, book.name) && Objects.equals(authorLogin, book.authorLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, authorLogin, year, description);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", authorLogin='" + authorLogin + '\'' +
                ", year=" + year +
                ", description='" + description + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getAuthorLogin() {
        return authorLogin;
    }
    public int getYear() {
        return year;
    }
    public String getDescription() {
        return description;
    }
}
