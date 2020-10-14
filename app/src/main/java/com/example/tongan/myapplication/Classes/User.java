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
    private ArrayList<String> serviceNumbers;
    private ArrayList<String> images;
    private ArrayList<String> acceptNumbers;

    public User(){}

    public User(String displayName, String email) {
        this.displayName = displayName;
        this.email = email;
    }

    public User(String displayName, String email, String password, int followers, double rating) {
        this.displayName = displayName;
        this.email = email;
        this.password = password;
        this.followers = followers;
        this.rating = rating;
    }

    public ArrayList<String> getAcceptNumbers() {
        return acceptNumbers;
    }

    public void setAcceptNumbers(ArrayList<String> acceptNumbers) {
        this.acceptNumbers = acceptNumbers;
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



    public ArrayList<String> getServiceNumbers() {
        return serviceNumbers;
    }

    public void setServiceNumbers(ArrayList<String> serviceNumbers) {
        this.serviceNumbers = serviceNumbers;
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
