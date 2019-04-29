package com.example.tongan.myapplication.Classes;

import java.util.ArrayList;

public class RequestService extends Service{

    private String buyNumber;
    private ArrayList<String> imageURL;

    public RequestService(String publisher, String serviceTitle, double price, String category, String description, String address, String publishTime, String profileImage) {
        super(publisher, serviceTitle, price, category, description, address, publishTime, profileImage);
    }
}
