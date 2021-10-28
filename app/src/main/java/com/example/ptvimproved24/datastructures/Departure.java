package com.example.ptvimproved24.datastructures;

import java.util.ArrayList;

public class Departure {
    private int stop_id;
    private int route_id;
    private int run_id;
    private String run_ref;
    private int direction_id;
    private ArrayList<Integer> disruption_ids;
    private String scheduled_departure_utc;
    private String estimated_departure_utc;
    private boolean at_platform;
    private String platform_number;
    private String flags;
    private int departure_sequence;

    public Departure(int stop_id, int route_id, int run_id, String run_ref, int direction_id, ArrayList<Integer> disruption_ids, String scheduled_departure_utc, String estimated_departure_utc, boolean at_platform, String platform_number, String flags, int departure_sequence) {
        this.stop_id = stop_id;
        this.route_id = route_id;
        this.run_id = run_id;
        this.run_ref = run_ref;
        this.direction_id = direction_id;
        this.disruption_ids = disruption_ids;
        this.scheduled_departure_utc = scheduled_departure_utc;
        this.estimated_departure_utc = estimated_departure_utc;
        this.at_platform = at_platform;
        this.platform_number = platform_number;
        this.flags = flags;
        this.departure_sequence = departure_sequence;
    }

    public Departure(int stop_id, int route_id, int run_id, String run_ref, int direction_id, String scheduled_departure_utc, String estimated_departure_utc, boolean at_platform, String platform_number, String flags, int departure_sequence) {
        this.stop_id = stop_id;
        this.route_id = route_id;
        this.run_id = run_id;
        this.run_ref = run_ref;
        this.direction_id = direction_id;
        this.scheduled_departure_utc = scheduled_departure_utc;
        this.estimated_departure_utc = estimated_departure_utc;
        this.at_platform = at_platform;
        this.platform_number = platform_number;
        this.flags = flags;
        this.departure_sequence = departure_sequence;
    }

    public Departure(int stop_id, int route_id, int run_id) {
        this.stop_id = stop_id;
        this.route_id = route_id;
        this.run_id = run_id;
    }

    public Departure(int stop_id, int route_id, int run_id, int direction_id) {
        this.stop_id = stop_id;
        this.route_id = route_id;
        this.run_id = run_id;
        this.direction_id = direction_id;
    }

    public int getStop_id() {
        return stop_id;
    }

    public void setStop_id(int stop_id) {
        this.stop_id = stop_id;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public int getRun_id() {
        return run_id;
    }

    public void setRun_id(int run_id) {
        this.run_id = run_id;
    }

    public String getRun_ref() {
        return run_ref;
    }

    public void setRun_ref(String run_ref) {
        this.run_ref = run_ref;
    }

    public int getDirection_id() {
        return direction_id;
    }

    public void setDirection_id(int direction_id) {
        this.direction_id = direction_id;
    }

    public ArrayList<Integer> getDisruption_ids() {
        return disruption_ids;
    }

    public void setDisruption_ids(ArrayList<Integer> disruption_ids) {
        this.disruption_ids = disruption_ids;
    }

    public String getScheduled_departure_utc() {
        return scheduled_departure_utc;
    }

    public void setScheduled_departure_utc(String scheduled_departure_utc) {
        this.scheduled_departure_utc = scheduled_departure_utc;
    }

    public String getEstimated_departure_utc() {
        return estimated_departure_utc;
    }

    public void setEstimated_departure_utc(String estimated_departure_utc) {
        this.estimated_departure_utc = estimated_departure_utc;
    }

    public boolean isAt_platform() {
        return at_platform;
    }

    public void setAt_platform(boolean at_platform) {
        this.at_platform = at_platform;
    }

    public String getPlatform_number() {
        return platform_number;
    }

    public void setPlatform_number(String platform_number) {
        this.platform_number = platform_number;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public int getDeparture_sequence() {
        return departure_sequence;
    }

    public void setDeparture_sequence(int departure_sequence) {
        this.departure_sequence = departure_sequence;
    }
}
