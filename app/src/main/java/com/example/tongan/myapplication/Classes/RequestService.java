package com.example.tongan.myapplication.Classes;

import java.util.ArrayList;

public class RequestService extends Service{

    private String buyNumber;
    private ArrayList<String> imageURL;

    public RequestService() {

    }

    public RequestService(User user, String id, String serviceTitle, double price, String category, String description, String address, String publishTime, ArrayList<String> serviceImages) {
        super(user, id, serviceTitle, price, category, description, address, publishTime, serviceImages);
    }
}
