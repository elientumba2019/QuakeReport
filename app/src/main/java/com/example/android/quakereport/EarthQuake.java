package com.example.android.quakereport;

/**
 * Created by ntumba on 17-12-28.
 */

public class EarthQuake {


    private String magnitude;
    private String city;
    private String date;
    private String webAddress;

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public EarthQuake(String magnitude, String city, String date) {
        this.magnitude = magnitude;
        this.city = city;
        this.date = date;
    }


    public String getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(String magnitude) {
        this.magnitude = magnitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "Magnitude : " + magnitude + "\nLocation : " + city + "\nDate : " + date;
    }
}
