package com.example.tongan.myapplication.Classes;

import com.google.firebase.Timestamp;

public class Service {
    private String publisher;
    private String serviceTitle;
    private double price;
    private String category;
    private String description;
    private String address;
    private int bought_times;
    private double rating;
    private String[] buyers;
    private String service_image;
    private String order_time;
    private String publishTime;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    private String profileImage;

    public Service() {
    }

    public Service(String publisher, String serviceTitle, double price, String category, String description, String address, String publishTime, String profileImage) {
        this.publisher = publisher;
        this.serviceTitle = serviceTitle;
        this.price = price;
        this.category = category;
        this.description = description;
        this.address = address;
        this.publishTime = publishTime;
        this.profileImage = profileImage;
    }

    public Service(String publisher, String serviceTitle, double price, String category,
                   String description, String address, int bought_times, double rating,
                   String[] buyers, String service_image, String order_time) {
        this.publisher = publisher;
        this.serviceTitle = serviceTitle;
        this.price = price;
        this.category = category;
        this.description = description;
        this.address = address;
        this.bought_times = bought_times;
        this.rating = rating;
        this.buyers = buyers;
        this.service_image = service_image;
        this.order_time = order_time;
    }

    /*
     * Getters
     */

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getBought_times() {
        return bought_times;
    }

    public void setBought_times(int bought_times) {
        this.bought_times = bought_times;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getPublishTime() {
        return publishTime;
    }

    /*
     * Setters
     */

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getService_image() {
        return service_image;
    }

    public void setService_image(String service_image) {
        this.service_image = service_image;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public String[] getBuyers() {
        return buyers;
    }

    public void setBuyers(String[] buyers) {
        this.buyers = buyers;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
    /*
     * Other methods
     */

}


