package com.example.ptvimproved24;

import com.example.ptvimproved24.datastructures.RouteGeopath;

import java.util.ArrayList;

public class Route {
    private int route_type;
    private int route_id;
    private String route_name;
    private String route_number;
    private String route_gtfs_id;
    private String direction;
    private ArrayList<RouteGeopath> geopath;

    public Route(int route_type, int route_id, String route_name, String route_number, String route_gtfs_id, String direction) {
        this.route_type = route_type;
        this.route_id = route_id;
        this.route_name = route_name;
        this.route_number = route_number;
        this.route_gtfs_id = route_gtfs_id;
        this.direction = direction;
    }

    public Route(int route_type, int route_id, String route_name, String route_gtfs_id) { // Train, vline train&coach, skybus
        this.route_type = route_type;
        this.route_id = route_id;
        this.route_name = route_name;
        this.route_gtfs_id = route_gtfs_id;
    }

    public Route(int route_type, int route_id, String route_name, String route_number, String route_gtfs_id) { // Tram, Buses (incl. night bus)
        this.route_type = route_type;
        this.route_id = route_id;
        this.route_name = route_name;
        this.route_number = route_number;
        this.route_gtfs_id = route_gtfs_id;
    }

    public Route(int route_id, String route_gtfs_id){
        this.route_id = route_id;
        this.route_gtfs_id = route_gtfs_id;
    }

    public Route(int route_id){
        this.route_id = route_id;
    }

    public Route(int route_type, int route_id, String route_name, String route_number, ArrayList<RouteGeopath> geopath) {
        this.route_type = route_type;
        this.route_id = route_id;
        this.route_name = route_name;
        this.route_number = route_number;
        this.geopath = geopath;
    }

    public Route(int route_type, int route_id, String route_name, String route_number, String route_gtfs_id, ArrayList<RouteGeopath> geopath) { // Tram, Buses (incl. night bus)
        this.route_type = route_type;
        this.route_id = route_id;
        this.route_name = route_name;
        this.route_number = route_number;
        this.route_gtfs_id = route_gtfs_id;
        this.geopath = geopath;
    }

    public int getRoute_type() {
        return route_type;
    }

    public void setRoute_type(int route_type) {
        this.route_type = route_type;
    }

    public int getRoute_id() {
        return route_id;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public String getRoute_number() {
        return route_number;
    }

    public void setRoute_number(String route_number) {
        this.route_number = route_number;
    }

    public String getRoute_gtfs_id() {
        return route_gtfs_id;
    }

    public void setRoute_gtfs_id(String route_gtfs_id) {
        this.route_gtfs_id = route_gtfs_id;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public ArrayList<RouteGeopath> getGeopath() {
        return geopath;
    }

    public void setGeopath(ArrayList<RouteGeopath> geopath) {
        this.geopath = geopath;
    }
}
