package com.example.ptvimproved24;

public class Stoppings {
    private int stops_id;
    private String stops_name;
    private int stops_type;

    public Stoppings(int stops_id, String stops_name, int stops_type) {
        this.stops_id = stops_id;
        this.stops_name = stops_name;
        this.stops_type = stops_type;
    }

    public Stoppings(int stops_id, String stops_name) {
        this.stops_id = stops_id;
        this.stops_name = stops_name;
    }

    public Stoppings(int stops_id) {
        this.stops_id = stops_id;
    }

    public int getStops_id() {
        return stops_id;
    }

    public void setStops_id(int stops_id) {
        this.stops_id = stops_id;
    }

    public String getStops_name() {
        return stops_name;
    }

    public void setStops_name(String stops_name) {
        this.stops_name = stops_name;
    }

    public int getStops_type() {
        return stops_type;
    }

    public void setStops_type(int stops_type) {
        this.stops_type = stops_type;
    }
}
