package com.example.ptvimproved24;

public class SavedRoute {
    private String savedRoutename;
    private int savedRouteid;
    private String savedRoutedirection;
    private int savedRouteType;

    public SavedRoute(String savedRoutename, int savedRouteid, String savedRoutedirection, int savedRouteType) {
        this.savedRoutename = savedRoutename;
        this.savedRouteid = savedRouteid;
        this.savedRoutedirection = savedRoutedirection;
        this.savedRouteType = savedRouteType;
    }

    public SavedRoute(String savedRoutename, int savedRouteid, int savedRouteType) {
        this.savedRoutename = savedRoutename;
        this.savedRouteid = savedRouteid;
        this.savedRouteType = savedRouteType;
    }

    public SavedRoute(String savedRoutename, int savedRouteid, String savedRoutedirection) {
        this.savedRoutename = savedRoutename;
        this.savedRouteid = savedRouteid;
        this.savedRoutedirection = savedRoutedirection;
    }

    public SavedRoute(String savedRoutename, String savedRoutedirection) {
        this.savedRoutename = savedRoutename;
        this.savedRoutedirection = savedRoutedirection;
    }

    public String getSavedRoutename() {
        return savedRoutename;
    }

    public void setSavedRoutename(String savedRoutename) {
        this.savedRoutename = savedRoutename;
    }

    public int getSavedRouteid() {
        return savedRouteid;
    }

    public void setSavedRouteid(int savedRouteid) {
        this.savedRouteid = savedRouteid;
    }

    public String getSavedRoutedirection() {
        return savedRoutedirection;
    }

    public void setSavedRoutedirection(String savedRoutedirection) {
        this.savedRoutedirection = savedRoutedirection;
    }

    public int getSavedRouteType() {
        return savedRouteType;
    }

    public void setSavedRouteType(int savedRouteType) {
        this.savedRouteType = savedRouteType;
    }
}
