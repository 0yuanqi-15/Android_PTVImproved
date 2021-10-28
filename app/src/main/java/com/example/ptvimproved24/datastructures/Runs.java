package com.example.ptvimproved24.datastructures;

public class Runs {
    int run_id ;
    String run_ref;
    int route_id;
    int route_type;
    int final_stop_id;
    String destination_name;
    String status;
    int direction_id;
    int run_sequence;
    int express_stop_count;
    VehiclePosition vp;
    VehicleDescriptor vd;

    public Runs(int run_id, String run_ref, int route_id, int route_type, int final_stop_id, String destination_name, String status, int direction_id, int run_sequence, int express_stop_count) {
        this.run_id = run_id;
        this.run_ref = run_ref;
        this.route_id = route_id;
        this.route_type = route_type;
        this.final_stop_id = final_stop_id;
        this.destination_name = destination_name;
        this.status = status;
        this.direction_id = direction_id;
        this.run_sequence = run_sequence;
        this.express_stop_count = express_stop_count;
    }

    public Runs(int run_id, String run_ref, int route_id, int route_type, int final_stop_id, String destination_name, String status, int direction_id, int run_sequence, int express_stop_count, VehiclePosition vp, VehicleDescriptor vd) {
        this.run_id = run_id;
        this.run_ref = run_ref;
        this.route_id = route_id;
        this.route_type = route_type;
        this.final_stop_id = final_stop_id;
        this.destination_name = destination_name;
        this.status = status;
        this.direction_id = direction_id;
        this.run_sequence = run_sequence;
        this.express_stop_count = express_stop_count;
        this.vp = vp;
        this.vd = vd;
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

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public int getRoute_type() {
        return route_type;
    }

    public void setRoute_type(int route_type) {
        this.route_type = route_type;
    }

    public int getFinal_stop_id() {
        return final_stop_id;
    }

    public void setFinal_stop_id(int final_stop_id) {
        this.final_stop_id = final_stop_id;
    }

    public String getDestination_name() {
        return destination_name;
    }

    public void setDestination_name(String destination_name) {
        this.destination_name = destination_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDirection_id() {
        return direction_id;
    }

    public void setDirection_id(int direction_id) {
        this.direction_id = direction_id;
    }

    public int getRun_sequence() {
        return run_sequence;
    }

    public void setRun_sequence(int run_sequence) {
        this.run_sequence = run_sequence;
    }

    public int getExpress_stop_count() {
        return express_stop_count;
    }

    public void setExpress_stop_count(int express_stop_count) {
        this.express_stop_count = express_stop_count;
    }

    public VehiclePosition getVp() {
        return vp;
    }

    public void setVp(VehiclePosition vp) {
        this.vp = vp;
    }

    public VehicleDescriptor getVd() {
        return vd;
    }

    public void setVd(VehicleDescriptor vd) {
        this.vd = vd;
    }
}
