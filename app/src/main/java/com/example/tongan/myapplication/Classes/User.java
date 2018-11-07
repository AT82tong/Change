package com.example.tongan.myapplication.Classes;

public class User {

    private String UserName;
    private String Email;
    private String Password;

    public User (){}

    public User(String UserName, String Password) {
        this.UserName = UserName;
        this.Password = Password;
    }

    public User(String UserName, String Email, String Password) {
        this.UserName = UserName;
        this.Email = Email;
        this.Password = Password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

}
