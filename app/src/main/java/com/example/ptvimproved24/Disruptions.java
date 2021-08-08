package com.example.ptvimproved24;

public class Disruptions {
    private int disruptionid;
    private String title;
    private String details;
    private String status;
    private String datetime;
    private String reflink;
    private int type;

    public Disruptions(int disruptionid, String title, String details, String status, String datetime, String reflink, int type) {
        this.disruptionid = disruptionid;
        this.title = title;
        this.details = details;
        this.status = status;
        this.datetime = datetime;
        this.reflink = reflink;
        this.type = type;
    }

    public Disruptions(int disruptionid, String title, String datetime, int type) {
        this.disruptionid = disruptionid;
        this.title = title;
        this.datetime = datetime;
        this.type = type;
    }

    public int getDisruptionid() {
        return disruptionid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public int getType() {
        return type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getReflink() {
        return reflink;
    }

    public void setReflink(String reflink) {
        this.reflink = reflink;
    }

    public void setType(int type) {
        this.type = type;
    }
}
