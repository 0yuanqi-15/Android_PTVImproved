package com.example.ptvimproved24.datastructures;

import java.util.ArrayList;

public class StopDetail {
    private int stopId;
    private String stopName;
    private String operatingHours;
    private String flexibleHours;
    private ArrayList<Integer> disruptionIds;
    private String stationDescription;
    private int routeType;
    private StopLocation stopLocation;
    private ArrayList<Route> routes;

    public StopDetail(int stopId, String stopName, String operatingHours, String flexibleHours, ArrayList<Integer> disruptionIds, String stationDescription, int routeType, StopLocation stopLocation, ArrayList<Route> routes) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.operatingHours = operatingHours;
        this.flexibleHours = flexibleHours;
        this.disruptionIds = disruptionIds;
        this.stationDescription = stationDescription;
        this.routeType = routeType;
        this.stopLocation = stopLocation;
        this.routes = routes;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public int getStopId() {
        return stopId;
    }

    public void setStopId(int stopId) {
        this.stopId = stopId;
    }

    public String getOperatingHours() {
        return operatingHours;
    }

    public void setOperatingHours(String operatingHours) {
        this.operatingHours = operatingHours;
    }

    public String getFlexibleHours() {
        return flexibleHours;
    }

    public void setFlexibleHours(String flexibleHours) {
        this.flexibleHours = flexibleHours;
    }

    public ArrayList<Integer> getDisruptionIds() {
        return disruptionIds;
    }

    public void setDisruptionIds(ArrayList<Integer> disruptionIds) {
        this.disruptionIds = disruptionIds;
    }

    public String getStationDescription() {
        return stationDescription;
    }

    public void setStationDescription(String stationDescription) {
        this.stationDescription = stationDescription;
    }

    public int getRouteType() {
        return routeType;
    }

    public void setRouteType(int routeType) {
        this.routeType = routeType;
    }

    public StopLocation getStopLocation() {
        return stopLocation;
    }

    public void setStopLocation(StopLocation stopLocation) {
        this.stopLocation = stopLocation;
    }

    public ArrayList<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<Route> routes) {
        this.routes = routes;
    }

    @Override
    public String toString() {
        return "StopDetail{" +
                "stopId=" + stopId +
                ", stopName='" + stopName + '\'' +
                ", operatingHours='" + operatingHours + '\'' +
                ", flexibleHours='" + flexibleHours + '\'' +
                ", disruptionIds=" + disruptionIds +
                ", stationDescription='" + stationDescription + '\'' +
                ", routeType=" + routeType +
                ", stopLocation=" + stopLocation +
                ", routes=" + routes +
                '}';
    }
}
