package com.example.tongan.myapplication.Classes;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class User {

    private String firstName;
    private String lastName;
    private String Email;
    private String Password;
    private int Followers;
    private double Rating;
    private String publicName;
    private String profileImageURL;
    private ArrayList<String> sellNumbers;
    private ArrayList<String> buyNumbers;
    private ArrayList<String> additionalImages;

    public User (){}

    public User(String firstName, String lastName, String Email, String Password, int followers, double ratings, String publicName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.Email = Email;
        this.Password = Password;
        this.Followers = followers;
        this.Rating = ratings;
        this.publicName = publicName;

    }

    public ArrayList<String> getAdditionalImages() {
        return additionalImages;
    }

    public void setAdditionalImages(ArrayList<String> additionalImages) {
        this.additionalImages = additionalImages;
    }

    public ArrayList<String> getSellNumbers() {
        return sellNumbers;
    }

    public void setSellNumbers(ArrayList<String> sellNumbers) {
        this.sellNumbers = sellNumbers;
    }

    public ArrayList<String> getBuyNumbers() {
        return buyNumbers;
    }

    public void setBuyNumbers(ArrayList<String> buyNumbers) {
        this.buyNumbers = buyNumbers;
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
        return Followers;
    }

    public void setFollower(int follower) {
        Followers = follower;
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

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
}
