package com.example.tongan.myapplication.Classes;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class User {

    private String displayName;
    private String Email;
    private String Password;
    private int Followers;
    private double Rating;
    private ArrayList<String> sellNumbers;
    private ArrayList<String> buyNumbers;
    private ArrayList<String> images;
    public User (){}
    public User(String displayName, String Email, String Password, int followers, double ratings) {
        this.displayName = displayName;
        this.Email = Email;
        this.Password = Password;
        this.Followers = followers;
        this.Rating = ratings;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

}
