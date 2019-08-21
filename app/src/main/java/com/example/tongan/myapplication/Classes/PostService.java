package com.example.tongan.myapplication.Classes;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class PostService extends Service {

    private String sellNumber;
    private ArrayList<String> imageURL;

    public PostService(){

    }

    public PostService(User user, String id, String serviceTitle, double price, String category, String description, String address, String publishTime, ArrayList<String> serviceImages) {
        super(user, id, serviceTitle, price, category, description, address, publishTime, serviceImages);
    }

}
