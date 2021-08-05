package com.example.ptvimproved24;

public class SavedRoute {
    private String savedRoutename;
    private String savedRouteid;
    private String savedRoutedirection;

    public SavedRoute(String savedRoutename, String savedRouteid, String savedRoutedirection) {
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

    public String getSavedRouteid() {
        return savedRouteid;
    }

    public void setSavedRouteid(String savedRouteid) {
        this.savedRouteid = savedRouteid;
    }

    public String getSavedRoutedirection() {
        return savedRoutedirection;
    }

    public void setSavedRoutedirection(String savedRoutedirection) {
        this.savedRoutedirection = savedRoutedirection;
    }
}
