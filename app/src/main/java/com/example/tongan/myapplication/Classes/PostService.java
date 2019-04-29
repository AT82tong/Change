package com.example.tongan.myapplication.Classes;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class PostService extends Service {

    private String sellNumber;
    private ArrayList<String> imageURL;

    public PostService(String publisher, String serviceTitle, double price, String category, String description, String address, String publishTime, String profileImage) {
        super(publisher, serviceTitle, price, category, description, address, publishTime, profileImage);
    }



        // CAN BE COMBINE OR EXTENDS `SERVICE`


}
