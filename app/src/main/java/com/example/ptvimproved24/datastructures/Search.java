package com.example.ptvimproved24.datastructures;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Search {
    private ArrayList<Stop> stopArrayList;
    private ArrayList<Route> routeArrayList;
    private ArrayList<Outlet> outletsArrayList;
    private Status status;

    public Search(ArrayList<Stop> stopArrayList, ArrayList<Route> routeArrayList, ArrayList<Outlet> outletsArrayList, Status status) {
        this.stopArrayList = stopArrayList;
        this.routeArrayList = routeArrayList;
        this.outletsArrayList = outletsArrayList;
        this.status = status;
    }

    public Search(ArrayList<Stop> stopArrayList, ArrayList<Route> routeArrayList, Status status) {
        this.stopArrayList = stopArrayList;
        this.routeArrayList = routeArrayList;
        this.status = status;
    }

    public Search(ArrayList<Stop> stopArrayList, ArrayList<Route> routeArrayList) {
        this.stopArrayList = stopArrayList;
        this.routeArrayList = routeArrayList;
    }

    public ArrayList<Stop> getStopArrayList() {
        return stopArrayList;
    }

    public void setStopArrayList(ArrayList<Stop> stopArrayList) {
        this.stopArrayList = stopArrayList;
    }

    public ArrayList<Route> getRouteArrayList() {
        return routeArrayList;
    }

    public void setRouteArrayList(ArrayList<Route> routeArrayList) {
        this.routeArrayList = routeArrayList;
    }

    public ArrayList<Outlet> getOutletsArrayList() {
        return outletsArrayList;
    }

    public void setOutletsArrayList(ArrayList<Outlet> outletsArrayList) {
        this.outletsArrayList = outletsArrayList;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
