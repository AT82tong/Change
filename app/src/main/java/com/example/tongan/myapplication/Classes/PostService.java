package com.example.tongan.myapplication.Classes;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class PostService extends Service {

    public PostService(){

    }

    public PostService(String id, String publisherEmail, String serviceTitle, double price, String category, String description, String address, String publishTime, String profileImage, boolean isAccepted, ArrayList<String> serviceImages, ArrayList<String> acceptorList) {
        super(id, publisherEmail, serviceTitle, price, category, description, address, publishTime, profileImage, isAccepted, serviceImages, acceptorList);
    }

}
