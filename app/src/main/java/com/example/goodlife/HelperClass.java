package com.example.goodlife;

public class HelperClass {
    String name, password, date_of_birth, gender, signUpDate, email;

    public HelperClass(String name, String password, String email, String date_of_birth, String gender, String signUpDate) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.date_of_birth = date_of_birth;
        this.gender = gender;
        this.signUpDate = signUpDate;
    }

    public HelperClass() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
