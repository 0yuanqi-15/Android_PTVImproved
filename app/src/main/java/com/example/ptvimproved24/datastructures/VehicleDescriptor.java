package com.example.ptvimproved24.datastructures;

public class VehicleDescriptor {
    String operator; // Operator name of the vehicle such as "Metro Trains Melbourne", "Yarra Trams", "Ventura Bus Line", "CDC" or "Sita Bus Lines" . May be null/empty. Only available for train, tram, v/line and some bus runs. ,
    String id ; // May be null/empty. Only available for some tram and bus runs. ,
    boolean low_floor; //Indicator if vehicle has a low floor. May be null. Only available for some tram runs. ,
    boolean air_conditioned; //Indicator if vehicle is air conditioned. May be null. Only available for some tram runs. ,
    String description; // Vehicle description such as "6 Car Comeng", "6 Car Xtrapolis", "3 Car Comeng", "6 Car Siemens", "3 Car Siemens". May be null/empty. Only available for some metropolitan train runs. ,
    String supplier; // Supplier of vehicle descriptor data. ,
    String length; // The length of the vehicle. Applies to CIS - Metro Trains

    public VehicleDescriptor(String operator) {
        this.operator = operator;
    }

    public VehicleDescriptor(String operator, String id, boolean low_floor, boolean air_conditioned, String description, String supplier, String length) {
        this.operator = operator;
        this.id = id;
        this.low_floor = low_floor;
        this.air_conditioned = air_conditioned;
        this.description = description;
        this.supplier = supplier;
        this.length = length;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLow_floor() {
        return low_floor;
    }

    public void setLow_floor(boolean low_floor) {
        this.low_floor = low_floor;
    }

    public boolean isAir_conditioned() {
        return air_conditioned;
    }

    public void setAir_conditioned(boolean air_conditioned) {
        this.air_conditioned = air_conditioned;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
