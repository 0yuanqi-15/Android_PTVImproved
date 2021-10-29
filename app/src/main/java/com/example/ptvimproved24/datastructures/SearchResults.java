package com.example.ptvimproved24.datastructures;

public class SearchResults {
    private int target_id;  // Route id/stop id
    private int route_type; // Route type
    private String target_name; // route name / stop name
    private int target_type; // 0=stops, 1=routes
    private String note;

    public SearchResults(int target_id, int route_type, String target_name, int target_type) {
        this.target_id = target_id;
        this.route_type = route_type;
        this.target_name = target_name;
        this.target_type = target_type;
    }

    public SearchResults(int target_id, int route_type, String target_name, int target_type, String note) {
        this.target_id = target_id;
        this.route_type = route_type;
        this.target_name = target_name;
        this.target_type = target_type;
        this.note = note;
    }

    public int getTarget_id() {
        return target_id;
    }

    public void setTarget_id(int target_id) {
        this.target_id = target_id;
    }

    public int getRoute_type() {
        return route_type;
    }

    public void setRoute_type(int route_type) {
        this.route_type = route_type;
    }

    public String getTarget_name() {
        return target_name;
    }

    public void setTarget_name(String target_name) {
        this.target_name = target_name;
    }

    public int getTarget_type() {
        return target_type;
    }

    public void setTarget_type(int target_type) {
        this.target_type = target_type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
