package com.example.goodlife;

public class HelperClass {
    String name, password;
    String date_of_birth;

    public HelperClass(String name, String password) {
        this.name = name;
        this.password = password;
    }

//    public HelperClass(String name, String password, String date_of_birth) {
//        this.name = name;
//        this.password = password;
//        this.date_of_birth = date_of_birth;
//    }

    public HelperClass() {
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
