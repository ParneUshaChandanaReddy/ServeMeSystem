package com.example.servemesystem.model;

public class ServiceInfo {
    String serviceName;
    String desc;
    String vendorName;
    String vendorPhone;
    String bidCustomerNo;
    String bidCustomerName;
    String serviceStatus;
    String paymentStatus;
    int noOfRatings;
    double lat;
    double lang;
    double cusLat;

    public double getCusLat() {
        return cusLat;
    }

    public void setCusLat(double cusLat) {
        this.cusLat = cusLat;
    }

    public double getCusLang() {
        return cusLang;
    }

    public void setCusLang(double cusLang) {
        this.cusLang = cusLang;
    }

    double cusLang;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLang() {
        return lang;
    }

    public void setLang(double lang) {
        this.lang = lang;
    }

    public long getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(long serviceDate) {
        this.serviceDate = serviceDate;
    }

    long serviceDate;

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    float rating;

    public int getNoOfRatings() {
        return noOfRatings;
    }

    public void setNoOfRatings(int noOfRatings) {
        this.noOfRatings = noOfRatings;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    String vendorAddress;
    int duration, cost, bidAmount;

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getBidAccepted() {
        return bidAccepted;
    }

    public int getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(int bidAmount) {
        this.bidAmount = bidAmount;
    }

    public void setBidAccepted(String bidAccepted) {
        this.bidAccepted = bidAccepted;
    }

    boolean isServiceAccepted;
    String bidAccepted = "No Action";

    public String getVendorName() {
        return vendorName;
    }


    public boolean isServiceAccepted() {
        return isServiceAccepted;
    }

    public void setServiceAccepted(boolean serviceAccepted) {
        isServiceAccepted = serviceAccepted;
    }

    public String getBidCustomerName() {
        return bidCustomerName;
    }

    public void setBidCustomerName(String bidCustomerName) {
        this.bidCustomerName = bidCustomerName;
    }

    public String getBidCustomerNo() {
        return bidCustomerNo;
    }

    public void setBidCustomerNo(String bidCustomerNo) {
        this.bidCustomerNo = bidCustomerNo;
    }

    public String getVendorPhone() {
        return vendorPhone;
    }

    public void setVendorPhone(String vendorPhone) {
        this.vendorPhone = vendorPhone;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

}
