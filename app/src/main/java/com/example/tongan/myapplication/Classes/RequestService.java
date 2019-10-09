package com.example.tongan.myapplication.Classes;

import java.util.ArrayList;

public class RequestService extends Service{

    private String buyNumber;
    private ArrayList<String> imageURL;

    public RequestService() {

    }

    public RequestService(String id, String publisherEmail, String serviceTitle, double price, String category, String description, String address, String publishTime, String profileImage, boolean isAccepted, ArrayList<String> serviceImages, int maxAcceptor) {
        super(id, publisherEmail, serviceTitle, price, category, description, address, publishTime, profileImage, isAccepted, serviceImages, maxAcceptor);
    }
}
