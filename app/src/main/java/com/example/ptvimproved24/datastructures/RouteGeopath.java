package com.example.ptvimproved24.datastructures;

import java.util.ArrayList;

public class RouteGeopath {
    private int direction_id;
    private String valid_from;
    private String valid_to;
    private String[] paths;
    private ArrayList<Float> pathsf;

    public RouteGeopath(int direction_id, String valid_from, String valid_to, String[] paths) {
        this.direction_id = direction_id;
        this.valid_from = valid_from;
        this.valid_to = valid_to;
        this.paths = paths;
    }

    public RouteGeopath(int direction_id, String valid_from, String valid_to, ArrayList<Float> pathsf) {
        this.direction_id = direction_id;
        this.valid_from = valid_from;
        this.valid_to = valid_to;
        this.pathsf = pathsf;
    }

    public RouteGeopath(int direction_id, String[] paths) {
        this.direction_id = direction_id;
        this.paths = paths;
    }

    public int getDirection_id() {
        return direction_id;
    }

    public void setDirection_id(int direction_id) {
        this.direction_id = direction_id;
    }

    public String getValid_from() {
        return valid_from;
    }

    public void setValid_from(String valid_from) {
        this.valid_from = valid_from;
    }

    public String getValid_to() {
        return valid_to;
    }

    public void setValid_to(String valid_to) {
        this.valid_to = valid_to;
    }

    public String[] getPaths() {
        return paths;
    }

    public void setPaths(String[] paths) {
        this.paths = paths;
    }
}
