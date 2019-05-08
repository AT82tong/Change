package com.example.tongan.myapplication.Classes;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class PostService extends Service {

    private String sellNumber;
    private ArrayList<String> imageURL;

    public PostService(String email, String publisher, String serviceTitle, double price, String category, String description, String address, String publishTime, String profileImage) {
        super(email, publisher, serviceTitle, price, category, description, address, publishTime, profileImage);
    }

}
