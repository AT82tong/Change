package com.example.tongan.myapplication.Classes;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class PostService extends Service {

    private String sellNumber;
    private ArrayList<String> imageURL;

    public PostService(){

    }

    public PostService(String publisherEmail, String serviceTitle, double price, String category, String description, String address, String publishTime, String profileImage, ArrayList<String> serviceImages) {
        super(publisherEmail, serviceTitle, price, category, description, address, publishTime, profileImage, serviceImages);
    }

}
