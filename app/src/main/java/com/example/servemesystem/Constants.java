package com.example.servemesystem;

import com.example.servemesystem.model.VendorInfo;
import com.google.android.gms.maps.model.LatLng;

public class Constants {
    public static String vendorKey = "vendor";
    public static String customerKey = "customer";
    public static String serviceKey = "service";
    public static String customerBid = "customerBid";
    public static String vendorBid = "vendorBid";
    public static String customerService = "customerService";
    public static String vendorServiceRequest = "vendorServiceRequest";
    public static String customerServiceRequest = "customerServiceRequest";
    public static VendorInfo loggedInUser;
    public static boolean isCustomer;
    public static boolean isGuest;

    public static String[] search_by = {"Name", "Address"};
    public static LatLng latLng;
    public static boolean locationSelected;
}
