package com.example.ptvimproved24.datastructures;

public class Outlet {
    double outlet_distance; //Distance of outlet from input location (in metres); returns 0 if no location is input
    String outlet_slid_spid; //The SLID / SPID ,
    String outlet_name; //The location name of the outlet ,
    String outlet_business; //The business name of the outlet ,
    int outlet_latitude; //Geographic coordinate of latitude at outlet ,
    int outlet_longitude; //Geographic coordinate of longitude at outlet ,
    String outlet_suburb; //The city/municipality the outlet is in ,
    int outlet_postcode; //The postcode for the outlet ,

    // The business hours on Weeks
    String outlet_business_hour_mon;
    String outlet_business_hour_tue;
    String outlet_business_hour_wed;
    String outlet_business_hour_thur;
    String outlet_business_hour_fri;
    String outlet_business_hour_sat;
    String outlet_business_hour_sun;

    String outlet_notes; // Any additional notes for the outlet such as 'Buy pre-loaded myki cards only'. May be null/empty.

    public Outlet(double outlet_distance, String outlet_slid_spid, String outlet_name, String outlet_business, int outlet_latitude, int outlet_longitude, String outlet_suburb, int outlet_postcode, String outlet_business_hour_mon, String outlet_business_hour_tue, String outlet_business_hour_wed, String outlet_business_hour_thur, String outlet_business_hour_fri, String outlet_business_hour_sat, String outlet_business_hour_sun, String outlet_notes) {
        this.outlet_distance = outlet_distance;
        this.outlet_slid_spid = outlet_slid_spid;
        this.outlet_name = outlet_name;
        this.outlet_business = outlet_business;
        this.outlet_latitude = outlet_latitude;
        this.outlet_longitude = outlet_longitude;
        this.outlet_suburb = outlet_suburb;
        this.outlet_postcode = outlet_postcode;
        this.outlet_business_hour_mon = outlet_business_hour_mon;
        this.outlet_business_hour_tue = outlet_business_hour_tue;
        this.outlet_business_hour_wed = outlet_business_hour_wed;
        this.outlet_business_hour_thur = outlet_business_hour_thur;
        this.outlet_business_hour_fri = outlet_business_hour_fri;
        this.outlet_business_hour_sat = outlet_business_hour_sat;
        this.outlet_business_hour_sun = outlet_business_hour_sun;
        this.outlet_notes = outlet_notes;
    }

    public Outlet(double outlet_distance, String outlet_slid_spid, String outlet_name, String outlet_business, int outlet_latitude, int outlet_longitude, String outlet_suburb, int outlet_postcode, String outlet_business_hour_mon, String outlet_business_hour_tue, String outlet_business_hour_wed, String outlet_business_hour_thur, String outlet_business_hour_fri, String outlet_business_hour_sat, String outlet_business_hour_sun) {
        this.outlet_distance = outlet_distance;
        this.outlet_slid_spid = outlet_slid_spid;
        this.outlet_name = outlet_name;
        this.outlet_business = outlet_business;
        this.outlet_latitude = outlet_latitude;
        this.outlet_longitude = outlet_longitude;
        this.outlet_suburb = outlet_suburb;
        this.outlet_postcode = outlet_postcode;
        this.outlet_business_hour_mon = outlet_business_hour_mon;
        this.outlet_business_hour_tue = outlet_business_hour_tue;
        this.outlet_business_hour_wed = outlet_business_hour_wed;
        this.outlet_business_hour_thur = outlet_business_hour_thur;
        this.outlet_business_hour_fri = outlet_business_hour_fri;
        this.outlet_business_hour_sat = outlet_business_hour_sat;
        this.outlet_business_hour_sun = outlet_business_hour_sun;
    }

    public Outlet(double outlet_distance, String outlet_slid_spid, String outlet_name, String outlet_business, int outlet_latitude, int outlet_longitude, String outlet_suburb, int outlet_postcode) {
        this.outlet_distance = outlet_distance;
        this.outlet_slid_spid = outlet_slid_spid;
        this.outlet_name = outlet_name;
        this.outlet_business = outlet_business;
        this.outlet_latitude = outlet_latitude;
        this.outlet_longitude = outlet_longitude;
        this.outlet_suburb = outlet_suburb;
        this.outlet_postcode = outlet_postcode;
    }

    public double getOutlet_distance() {
        return outlet_distance;
    }

    public void setOutlet_distance(double outlet_distance) {
        this.outlet_distance = outlet_distance;
    }

    public String getOutlet_slid_spid() {
        return outlet_slid_spid;
    }

    public void setOutlet_slid_spid(String outlet_slid_spid) {
        this.outlet_slid_spid = outlet_slid_spid;
    }

    public String getOutlet_name() {
        return outlet_name;
    }

    public void setOutlet_name(String outlet_name) {
        this.outlet_name = outlet_name;
    }

    public String getOutlet_business() {
        return outlet_business;
    }

    public void setOutlet_business(String outlet_business) {
        this.outlet_business = outlet_business;
    }

    public int getOutlet_latitude() {
        return outlet_latitude;
    }

    public void setOutlet_latitude(int outlet_latitude) {
        this.outlet_latitude = outlet_latitude;
    }

    public int getOutlet_longitude() {
        return outlet_longitude;
    }

    public void setOutlet_longitude(int outlet_longitude) {
        this.outlet_longitude = outlet_longitude;
    }

    public String getOutlet_suburb() {
        return outlet_suburb;
    }

    public void setOutlet_suburb(String outlet_suburb) {
        this.outlet_suburb = outlet_suburb;
    }

    public int getOutlet_postcode() {
        return outlet_postcode;
    }

    public void setOutlet_postcode(int outlet_postcode) {
        this.outlet_postcode = outlet_postcode;
    }

    public String getOutlet_business_hour_mon() {
        return outlet_business_hour_mon;
    }

    public void setOutlet_business_hour_mon(String outlet_business_hour_mon) {
        this.outlet_business_hour_mon = outlet_business_hour_mon;
    }

    public String getOutlet_business_hour_tue() {
        return outlet_business_hour_tue;
    }

    public void setOutlet_business_hour_tue(String outlet_business_hour_tue) {
        this.outlet_business_hour_tue = outlet_business_hour_tue;
    }

    public String getOutlet_business_hour_wed() {
        return outlet_business_hour_wed;
    }

    public void setOutlet_business_hour_wed(String outlet_business_hour_wed) {
        this.outlet_business_hour_wed = outlet_business_hour_wed;
    }

    public String getOutlet_business_hour_thur() {
        return outlet_business_hour_thur;
    }

    public void setOutlet_business_hour_thur(String outlet_business_hour_thur) {
        this.outlet_business_hour_thur = outlet_business_hour_thur;
    }

    public String getOutlet_business_hour_fri() {
        return outlet_business_hour_fri;
    }

    public void setOutlet_business_hour_fri(String outlet_business_hour_fri) {
        this.outlet_business_hour_fri = outlet_business_hour_fri;
    }

    public String getOutlet_business_hour_sat() {
        return outlet_business_hour_sat;
    }

    public void setOutlet_business_hour_sat(String outlet_business_hour_sat) {
        this.outlet_business_hour_sat = outlet_business_hour_sat;
    }

    public String getOutlet_business_hour_sun() {
        return outlet_business_hour_sun;
    }

    public void setOutlet_business_hour_sun(String outlet_business_hour_sun) {
        this.outlet_business_hour_sun = outlet_business_hour_sun;
    }

    public String getOutlet_notes() {
        return outlet_notes;
    }

    public void setOutlet_notes(String outlet_notes) {
        this.outlet_notes = outlet_notes;
    }
}
