package com.example.ptvimproved24.datastructures;

public class StopLocation {
    private int postCode;
    private String municipality;
    private int municipalityId;
    private String primaryName;
    private String primaryRoadType;
    private String secondName;
    private String secondRoadType;
    private String suburb;
    private float latitude;
    private float longitude;

    public StopLocation(int postCode, String municipality, int municipalityId, String primaryName, String primaryRoadType, String secondName, String secondRoadType, String suburb, float latitude, float longitude) {
        this.postCode = postCode;
        this.municipality = municipality;
        this.municipalityId = municipalityId;
        this.primaryName = primaryName;
        this.primaryRoadType = primaryRoadType;
        this.secondName = secondName;
        this.secondRoadType = secondRoadType;
        this.suburb = suburb;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public int getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(int municipalityId) {
        this.municipalityId = municipalityId;
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    public String getPrimaryRoadType() {
        return primaryRoadType;
    }

    public void setPrimaryRoadType(String primaryRoadType) {
        this.primaryRoadType = primaryRoadType;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSecondRoadType() {
        return secondRoadType;
    }

    public void setSecondRoadType(String secondRoadType) {
        this.secondRoadType = secondRoadType;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "StopLocation{" +
                "postCode=" + postCode +
                ", municipality='" + municipality + '\'' +
                ", municipalityId=" + municipalityId +
                ", primaryName='" + primaryName + '\'' +
                ", primaryRoadType='" + primaryRoadType + '\'' +
                ", secondName='" + secondName + '\'' +
                ", secondRoadType='" + secondRoadType + '\'' +
                ", suburb='" + suburb + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
