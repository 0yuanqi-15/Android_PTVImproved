package com.example.ptvimproved24;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Disruption {
    private int disruptionid;
    private String title;
    private String reflink;
    private String description;
    private String disruption_status;
    private String disruption_type;
    private String publishDatetime;
    private String affectDatetimeFrom;
    private String affectDatetimeUntil;
    private ArrayList<Routes> affectedRoutes;
    private ArrayList<Stop> affectedStops;
    private boolean display_status;

    public Disruption(int disruptionid, String title, String details, String status, String datetime, String reflink, int type) {
        this.disruptionid = disruptionid;
        this.title = title;
        this.reflink = reflink;
        this.description = description;
        this.disruption_status = disruption_status;
        this.disruption_type = disruption_type;
        this.publishDatetime = publishDatetime;
        this.affectDatetimeFrom = affectDatetimeFrom;
        this.affectDatetimeUntil = affectDatetimeUntil;
        this.affectedRoutes = affectedRoutes;
        this.affectedStops = affectedStops;
        this.display_status = display_status;
    }

    public Disruption(int disruptionid, String title, String reflink, String description, String disruption_status, String publishDatetime, ArrayList<Routes> affectedRoutes, ArrayList<Stop> affectedStops, boolean display_status) {
        this.disruptionid = disruptionid;
        this.title = title;
        this.reflink = reflink;
        this.description = description;
        this.disruption_status = disruption_status;
        this.publishDatetime = publishDatetime;
        this.affectedRoutes = affectedRoutes;
        this.affectedStops = affectedStops;
        this.display_status = display_status;
    }

    public Disruption(int disruptionid, String title, String datetime, int type) {
        this.disruptionid = disruptionid;
        this.title = title;
        this.reflink = reflink;
        this.description = description;
        this.disruption_status = disruption_status;
        this.publishDatetime = publishDatetime;
        this.affectedRoutes = affectedRoutes;
        this.affectedStops = affectedStops;
        this.display_status = display_status;
    }

    public int getDisruptionid() {
        return disruptionid;
    }

    public void setDisruptionid(int disruptionid) {
        this.disruptionid = disruptionid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReflink() {
        return reflink;
    }

    public void setReflink(String reflink) {
        this.reflink = reflink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisruption_status() {
        return disruption_status;
    }

    public void setDisruption_status(String disruption_status) {
        this.disruption_status = disruption_status;
    }

    public String getDisruption_type() {
        return disruption_type;
    }

    public void setDisruption_type(String disruption_type) {
        this.disruption_type = disruption_type;
    }

    public String getPublishDatetime() {
        return publishDatetime;
    }

    public void setPublishDatetime(String publishDatetime) {
        this.publishDatetime = publishDatetime;
    }

    public String getAffectDatetimeFrom() {
        return affectDatetimeFrom;
    }

    public void setAffectDatetimeFrom(String affectDatetimeFrom) {
        this.affectDatetimeFrom = affectDatetimeFrom;
    }

    public String getAffectDatetimeUntil() {
        return affectDatetimeUntil;
    }

    public void setAffectDatetimeUntil(String affectDatetimeUntil) {
        this.affectDatetimeUntil = affectDatetimeUntil;
    }

    public ArrayList<Routes> getAffectedRoutes() {
        return affectedRoutes;
    }

    public void setAffectedRoutes(ArrayList<Routes> affectedRoutes) {
        this.affectedRoutes = affectedRoutes;
    }

    public ArrayList<Stop> getAffectedStops() {
        return affectedStops;
    }

    public void setAffectedStops(ArrayList<Stop> affectedStops) {
        this.affectedStops = affectedStops;
    }

    public boolean isDisplay_status() {
        return display_status;
    }

    public void setDisplay_status(boolean display_status) {
        this.display_status = display_status;
    }

    @Override
    public String toString() {
        return "Disruptions{" +
                "disruptionid=" + disruptionid +
                ", title='" + title + '\'' +
                ", details='" + description + '\'' +
                ", status='" + disruption_status + '\'' +
                ", datetime='" + publishDatetime + '\'' +
                ", reflink='" + reflink + '\'' +
                ", type=" + disruption_type +
                '}';
    }
}
