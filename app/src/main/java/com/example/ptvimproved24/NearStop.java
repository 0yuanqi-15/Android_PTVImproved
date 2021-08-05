package com.example.ptvimproved24;

import java.util.ArrayList;

public class NearStop {

    private String suburb;
    private String stopname;
    private String stopid;
    private ArrayList<String> routes;
    private ArrayList<String> times;
    private int distance;

    public NearStop(String suburb, String stopname, String stopid, ArrayList<String> routes, ArrayList<String> times) {
        this.suburb = suburb;
        this.stopname = stopname;
        this.stopid = stopid;
        this.routes = routes;
        this.times = times;
    }

    public NearStop(String suburb, String stopname, int distance, ArrayList<String> routes, ArrayList<String> times) {
        this.suburb = suburb;
        this.stopname = stopname;
        this.routes = routes;
        this.times = times;
        this.distance = distance;
    }

    public NearStop(String suburb, String stopname, String stopid) {
        this.suburb = suburb;
        this.stopname = stopname;
        this.stopid = stopid;
    }

    public NearStop(String suburb, String stopname) {
        this.suburb = suburb;
        this.stopname = stopname;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getStopname() {
        return stopname;
    }

    public void setStopname(String stopname) {
        this.stopname = stopname;
    }

    public String getStopid() {
        return stopid;
    }

    public void setStopid(String stopid) {
        this.stopid = stopid;
    }

    public ArrayList<String> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<String> routes) {
        this.routes = routes;
    }

    public ArrayList<String> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<String> times) {
        this.times = times;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
