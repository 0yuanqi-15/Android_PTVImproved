package com.example.ptvimproved24.datastructures;

import java.util.ArrayList;

public class Pattern {
    private ArrayList<Departure> departures;
    private ArrayList<Stop> stops;
    private ArrayList<Route> routes;
    private ArrayList<Runs> runs;

    public Pattern(ArrayList<Departure> departures, ArrayList<Stop> stops, ArrayList<Route> routes, ArrayList<Runs> runs) {
        this.departures = departures;
        this.stops = stops;
        this.routes = routes;
        this.runs = runs;
    }

    public ArrayList<Departure> getDepartures() {
        return departures;
    }

    public void setDepartures(ArrayList<Departure> departures) {
        this.departures = departures;
    }

    public ArrayList<Stop> getStops() {
        return stops;
    }

    public void setStops(ArrayList<Stop> stops) {
        this.stops = stops;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    public ArrayList<Runs> getRuns() {
        return runs;
    }

    public void setRuns(ArrayList<Runs> runs) {
        this.runs = runs;
    }
}
