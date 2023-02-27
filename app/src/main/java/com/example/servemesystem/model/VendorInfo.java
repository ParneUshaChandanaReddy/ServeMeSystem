package com.example.servemesystem.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class VendorInfo {
    String name, emailId, phoneNo, address, password;
    boolean isApprovedVendor;
    int noOfRatings;
    double lat, lang;
    int rewardPoints;
    Float rating;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isApprovedVendor() {
        return isApprovedVendor;
    }

    public void setApprovedVendor(boolean approvedVendor) {
        isApprovedVendor = approvedVendor;
    }

    public int getNoOfRatings() {
        return noOfRatings;
    }

    public void setNoOfRatings(int noOfRatings) {
        this.noOfRatings = noOfRatings;
    }

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

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
