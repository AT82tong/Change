package com.example.tongan.myapplication.Classes;

public class User {

    private String firstName;
    private String lastName;
    private String Email;
    private String Password;
    private int Follower;
    private double Rating;
    private String publicName;

    public User (){}

    public User(String firstName, String lastName, String Email, String Password, int followers, double ratings, String publicName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.Email = Email;
        this.Password = Password;
        this.Follower = followers;
        this.Rating = ratings;
        this.publicName = publicName;
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

    public int getFollower() {
        return Follower;
    }

    public void setFollower(int follower) {
        Follower = follower;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
    }

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }
}
