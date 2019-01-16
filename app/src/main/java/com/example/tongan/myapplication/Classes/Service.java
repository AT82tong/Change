package com.example.tongan.myapplication.Classes;

import com.google.firebase.Timestamp;

public class Service {
    private String publisher;
    private String service_name;
    private double price;
    private String category;
    private String description;
    private String address;
    private int bought_times;
    private double rating;
    private String[] buyers;
    private String service_image;
    private Timestamp order_time;

    public Service(){}

    public Service(String publisher, String service_name, double price, String category,
                   String description, String address, int bought_times, double rating,
                   String[] buyers, String service_image, Timestamp order_time){
        this.publisher = publisher;
        this.service_name = service_name;
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

    public double getRating() {
        return rating;
    }

    public int getBought_times() {
        return bought_times;
    }

    public String getAddress() {
        return address;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getService_image() {
        return service_image;
    }

    public String getService_name() {
        return service_name;
    }

    public String[] getBuyers() {
        return buyers;
    }

    public Timestamp getOrder_time() {
        return order_time;
    }

    /*
     * Setters
     */

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBought_times(int bought_times) {
        this.bought_times = bought_times;
    }

    public void setBuyers(String[] buyers) {
        this.buyers = buyers;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOrder_time(Timestamp order_time) {
        this.order_time = order_time;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setService_image(String service_image) {
        this.service_image = service_image;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    /*
     * Other methods
     */

}


