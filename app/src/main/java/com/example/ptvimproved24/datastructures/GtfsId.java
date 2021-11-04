package com.example.ptvimproved24.datastructures;

public class GtfsId {

    private static GtfsId instance;

    private GtfsId() {

    }

    //use double lock method
    public static GtfsId getInstance() {
        if (instance == null) {
            synchronized (GtfsId.class) {
                if (instance == null) {
                    instance = new GtfsId();
                }
            }
        }
        return instance;
    }

    public String restructureGtfsId(String gtfsId) {
        String result = gtfsId.substring(2);
        while (result.charAt(0) == '0') {
            result = result.substring(1);
        }
        return result;
    }
}
