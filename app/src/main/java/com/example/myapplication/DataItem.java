package com.example.myapplication;

import android.widget.Button;

import com.firebase.geofire.GeoFire;

public class DataItem {
    public final String Name;
    public final String Mobile;
    public String latitude;
    public String longitude;
    public final String driverName;
    public final String driverMobile;


    DataItem(String Name,String Mobile , String latitude , String longitude ,String driverName , String driverMobile){
        this.Name = Name;
        this.Mobile = Mobile;
        this.latitude = latitude;
        this.longitude = longitude;
        this.driverName = driverName;
        this.driverMobile = driverMobile;
    }
}
