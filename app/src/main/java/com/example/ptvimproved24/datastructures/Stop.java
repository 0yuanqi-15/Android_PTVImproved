package com.example.ptvimproved24.datastructures;

import java.util.ArrayList;

public class Stop {

    private String stop_suburb;
    private String stop_name;
    private int stop_id;
    private ArrayList<String> routes;
    private ArrayList<String> times;
    private ArrayList<Departure> departuresObj;
    private ArrayList<Route> routesObj;
    private int stop_distance;
    private int routeType;
    private double stop_latitude;
    private double stop_longitude;
    private String stop_landmark;
    private int stop_sequence;
    private String stop_time;

    public Stop (int stopId, int routeType) {
        this.stop_id = stopId;
        this.routeType = routeType;
    }

    public Stop (int stopId, String stop_name) {
        this.stop_id = stopId;
        this.stop_name = stop_name;
    }

    public Stop(String stop_suburb, String stop_name, int stop_id, int routeType, double stop_latitude, double stop_longitude, String stop_time) {
        this.stop_suburb = stop_suburb;
        this.stop_name = stop_name;
        this.stop_id = stop_id;
        this.routeType = routeType;
        this.stop_latitude = stop_latitude;
        this.stop_longitude = stop_longitude;
        this.stop_time = stop_time;
    }

    public Stop(int stop_distance, String stop_suburb, String stop_name, int stop_id, int routeType, double stop_latitude, double stop_longitude, String stop_landmark, int stop_sequence){
        this.stop_distance = stop_distance;
        this.stop_suburb = stop_suburb;
        this.stop_name = stop_name;
        this.stop_id = stop_id;
        this.routeType = routeType;
        this.stop_latitude = stop_latitude;
        this.stop_longitude = stop_longitude;
        this.stop_landmark = stop_landmark;
        this.stop_sequence = stop_sequence;
    }


    public Stop(String stop_suburb, int stop_id, String stop_name, ArrayList<String> routes, ArrayList<String> times) {
        this.stop_suburb = stop_suburb;
        this.stop_name = stop_name;
        this.stop_id = stop_id;
        this.routes = routes;
        this.times = times;
    }

    public Stop(String stop_suburb, String stop_name, int stop_distance, ArrayList<String> routes, ArrayList<String> times) {
        this.stop_suburb = stop_suburb;
        this.stop_name = stop_name;
        this.routes = routes;
        this.times = times;
        this.stop_distance = stop_distance;
    }

    public Stop(String stop_suburb, String stop_name, int stop_id) {
        this.stop_suburb = stop_suburb;
        this.stop_name = stop_name;
        this.stop_id = stop_id;
    }

    public Stop (int stopId, int routeType, float stop_latitude, float stop_longitude, String stop_name) {
        this.stop_id = stopId;
        this.routeType = routeType;
        this.stop_latitude = stop_latitude;
        this.stop_longitude = stop_longitude;
        this.stop_name = stop_name;
    }

    public Stop(String stop_suburb, int routeType, float stop_latitude, float stop_longitude,int stop_id, String stop_name) {
        this.stop_suburb = stop_suburb;
        this.stop_name = stop_name;
        this.routeType = routeType;
        this.stop_id = stop_id;
        this.stop_latitude = stop_latitude;
        this.stop_longitude = stop_longitude;
    }

    public String getStop_suburb() {
        return stop_suburb;
    }

    public void setStop_suburb(String stop_suburb) {
        this.stop_suburb = stop_suburb;
    }

    public String getStop_name() {
        return stop_name;
    }

    public void setStop_name(String stop_name) {
        this.stop_name = stop_name;
    }

    public int getStop_id() { return stop_id; }

    public void setStop_id(int stop_id) {
        this.stop_id = stop_id;
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

    public int getStop_distance() {
        return stop_distance;
    }

    public void setStop_distance(int stop_distance) {
        this.stop_distance = stop_distance;
    }

    public int getRouteType() { return routeType; }

    public void setRouteType(int routeType) {
        this.routeType = routeType;
    }

    public ArrayList<Departure> getDeparturesObj() {
        return departuresObj;
    }

    public void setDeparturesObj(ArrayList<Departure> departuresObj) {
        this.departuresObj = departuresObj;
    }

    public ArrayList<Route> getRoutesObj() {
        return routesObj;
    }

    public void setRoutesObj(ArrayList<Route> routesObj) {
        this.routesObj = routesObj;
    }

    public double getStop_latitude() {
        return stop_latitude;
    }

    public void setStop_latitude(double stop_latitude) {
        this.stop_latitude = stop_latitude;
    }

    public double getStop_longitude() {
        return stop_longitude;
    }

    public void setStop_longitude(double stop_longitude) {
        this.stop_longitude = stop_longitude;
    }

    public String getStop_landmark() {
        return stop_landmark;
    }

    public void setStop_landmark(String stop_landmark) {
        this.stop_landmark = stop_landmark;
    }

    public int getStop_sequence() {
        return stop_sequence;
    }

    public void setStop_sequence(int stop_sequence) {
        this.stop_sequence = stop_sequence;
    }

    public String getStop_time() {
        return stop_time;
    }

    public void setStop_time(String stop_time) {
        this.stop_time = stop_time;
    }
}
