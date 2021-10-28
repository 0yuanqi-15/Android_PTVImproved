package com.example.ptvimproved24.datastructures;

public class VehiclePosition {
    double latitude; // May be null. Only available for some bus runs. ,
    double longitude; // Only available for some bus runs. ,
    double easting; //CIS - Metro Train Vehicle Location Easting coordinate ,
    double northing; // CIS - Metro Train Vehicle Location Northing coordinate ,
    String direction; // CIS - Metro Train Vehicle Location Direction ,
    int bearing; //Compass bearing of the vehicle when known, clockwise from True North, i.e., 0 is North and 90 is East. May be null. Only available for some bus runs. ,
    String supplier; //Supplier of vehicle position data. ,
    String datetime_utc; //Date and time that the vehicle position data was supplied. ,
    String expiry_time; //CIS - Metro Train Vehicle Location data expiry time

    public VehiclePosition(double latitude, double longitude, String datetime_utc) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.datetime_utc = datetime_utc;
    }

    public VehiclePosition(double easting, double northing, String direction, int bearing, String supplier, String datetime_utc, String expiry_time) {
        this.easting = easting;
        this.northing = northing;
        this.direction = direction;
        this.bearing = bearing;
        this.supplier = supplier;
        this.datetime_utc = datetime_utc;
        this.expiry_time = expiry_time;
    }

    public VehiclePosition(double latitude, double longitude, double easting, double northing, String direction, int bearing, String supplier, String datetime_utc, String expiry_time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.easting = easting;
        this.northing = northing;
        this.direction = direction;
        this.bearing = bearing;
        this.supplier = supplier;
        this.datetime_utc = datetime_utc;
        this.expiry_time = expiry_time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getEasting() {
        return easting;
    }

    public void setEasting(double easting) {
        this.easting = easting;
    }

    public double getNorthing() {
        return northing;
    }

    public void setNorthing(double northing) {
        this.northing = northing;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getBearing() {
        return bearing;
    }

    public void setBearing(int bearing) {
        this.bearing = bearing;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getDatetime_utc() {
        return datetime_utc;
    }

    public void setDatetime_utc(String datetime_utc) {
        this.datetime_utc = datetime_utc;
    }

    public String getExpiry_time() {
        return expiry_time;
    }

    public void setExpiry_time(String expiry_time) {
        this.expiry_time = expiry_time;
    }
}
