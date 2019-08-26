package com.example.tongan.myapplication.Classes;

import com.google.firebase.Timestamp;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Service {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String publisherEmail;
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
    private String profileImage;
    private ArrayList<String> serviceImages;

    public Service() {
    }

    public Service(String id, String publisherEmail, String serviceTitle, double price, String category, String description, String address, String publishTime, String profileImage, ArrayList<String> serviceImages) {
        this.id = id;
        this.publisherEmail = publisherEmail;
        this.serviceTitle = serviceTitle;
        this.price = price;
        this.category = category;
        this.description = description;
        this.address = address;
        this.publishTime = publishTime;
        this.profileImage = profileImage;
        this.serviceImages = serviceImages;
    }

    public Service(String publisher, String serviceTitle, double price, String category,
                   String description, String address, int bought_times, double rating,
                   String[] buyers, String service_image, String order_time) {
        this.publisherEmail = publisher;
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

    public ArrayList<String> getServiceImages() {
        return serviceImages;
    }

    public void setServiceImages(ArrayList<String> serviceImages) {
        this.serviceImages = serviceImages;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

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

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getpublisherEmail() {
        return publisherEmail;
    }

    public void setpublisherEmail(String publisherEmail) {
        this.publisherEmail = publisherEmail;
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

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }
}


