package com.example.ptvimproved24;

import com.example.ptvimproved24.datastructures.RouteGeopath;

public class Direction {
    private int route_type;
    private int route_id;
    private int direction_id;


    private String route_direction_description;
    private String direction_name;


    public Direction(int route_type, int route_id, int direction_id, String route_direction_description, String direction_name) {
        this.route_type = route_type;
        this.route_id = route_id;
        this.direction_id = direction_id;
        this.route_direction_description = route_direction_description;
        this.direction_name = direction_name;
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

    public void setRoute_id(int route_id) {
        this.route_type = route_id;
    }

    public int getDirection_id() {
        return direction_id;
    }

    public void setDirection_id(int direction_id) {
        this.direction_id = direction_id;
    }

    public String getRoute_direction_description() {
        return route_direction_description;
    }

    public void setRoute_direction_description(String route_direction_description) {
        this.route_direction_description = route_direction_description;
    }

    public String getDirection_name() {
        return direction_name;
    }

    public void setDirection_name(String route_gtfs_id) {
        this.direction_name = direction_name;
    }




}
