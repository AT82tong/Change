package com.example.tongan.myapplication.Classes;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class User {

    private String displayName;
    private String email;
    private String password;
    private int followers;
    private double rating;
    private String profileImage;
    private ArrayList<String> sellNumbers;
    private ArrayList<String> buyNumbers;
    private ArrayList<String> images;
    public User (){}
    public User(String displayName, String Email, String Password, int followers, double ratings) {
        this.displayName = displayName;
        this.email = Email;
        this.password = Password;
        this.followers = followers;
        this.rating = ratings;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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
        return email;
    }

    public void setEmail(String email) {
        email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        password = password;
    }

    public int getFollower() {
        return followers;
    }

    public void setFollower(int follower) {
        followers = follower;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        rating = rating;
    }

}
