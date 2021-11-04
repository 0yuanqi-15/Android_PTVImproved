package com.example.ptvimproved24.datastructures;

public class SavedRoute {
    private String savedRouteGtfsId;
    private int savedRouteid;
    private String savedRouteName;
    private int savedRouteType;

    public SavedRoute(String savedRouteGtfsId, int savedRouteid, String savedRouteName, int savedRouteType) {
        this.savedRouteGtfsId = savedRouteGtfsId;
        this.savedRouteid = savedRouteid;
        this.savedRouteName = savedRouteName;
        this.savedRouteType = savedRouteType;
    }

    public SavedRoute(String savedRouteGtfsId, int savedRouteid, int savedRouteType) {
        this.savedRouteGtfsId = savedRouteGtfsId;
        this.savedRouteid = savedRouteid;
        this.savedRouteType = savedRouteType;
    }

    public SavedRoute(String savedRouteGtfsId, int savedRouteid, String savedRouteName) {
        this.savedRouteGtfsId = savedRouteGtfsId;
        this.savedRouteid = savedRouteid;
        this.savedRouteName = savedRouteName;
    }

    public SavedRoute(String savedRouteGtfsId, String savedRouteName) {
        this.savedRouteGtfsId = savedRouteGtfsId;
        this.savedRouteName = savedRouteName;
    }

    public String getSavedRouteGtfsId() {
        return savedRouteGtfsId;
    }

    public void setSavedRouteGtfsId(String savedRouteGtfsId) {
        this.savedRouteGtfsId = savedRouteGtfsId;
    }

    public int getSavedRouteid() {
        return savedRouteid;
    }

    public void setSavedRouteid(int savedRouteid) {
        this.savedRouteid = savedRouteid;
    }

    public String getSavedRouteName() {
        return savedRouteName;
    }

    public void setSavedRouteName(String savedRouteName) {
        this.savedRouteName = savedRouteName;
    }

    public int getSavedRouteType() {
        return savedRouteType;
    }

    public void setSavedRouteType(int savedRouteType) {
        this.savedRouteType = savedRouteType;
    }
}
