package com.example.udemy.course.dto;

public class Student {

    private long id;
    private String firstName;
    private String lastName;
    private String Street;
    private String City;

    public Student(long id, String firstName, String lastName, String street, String city) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        Street = street;
        City = city;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
