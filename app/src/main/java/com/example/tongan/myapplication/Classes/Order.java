package com.example.tongan.myapplication.Classes;


import java.util.ArrayList;

public class Order {

    private String serviceId;
    private String serviceType;
    private String originalPoster;
    private String acceptor;
    private String status;
    private String paymentType;
    private String acceptanceTime;
    private String completionTime;
    private double totalPrice;

    public Order(String serviceId, String serviceType, String originalPoster, String acceptor, String status, String acceptanceTime, double totalPrice) {
        this.serviceId = serviceId;
        this.serviceType = serviceType;
        this.originalPoster = originalPoster;
        this.acceptor = acceptor;
        this.status = status;
        this.acceptanceTime = acceptanceTime;
        this.totalPrice = totalPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAcceptanceTime() {
        return acceptanceTime;
    }

    public void setAcceptanceTime(String acceptanceTime) {
        this.acceptanceTime = acceptanceTime;
    }

    public String getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(String completionTime) {
        this.completionTime = completionTime;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getOriginalPoster() {
        return originalPoster;
    }

    public void setOriginalPoster(String originalPoster) {
        this.originalPoster = originalPoster;
    }

    public String getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

}
