package com.example.ptvimproved24;

import java.security.Key;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class commonDataRequest {

    private static final int devId = 3001136;
    private static final String devKey = "5d246d05-f36d-4606-96df-829d509a4e60";
    private static final String baseURL = "http://timetableapi.ptv.vic.gov.au ";

    public static String buildAPIrequest(final String uri) throws Exception
    {
        String HMAC_SHA1_ALGORITHM = "HmacSHA1";
        StringBuffer uriWithDeveloperID = new StringBuffer().append(uri).append(uri.contains("?") ? "&" : "?").append("devid=" + devId);
        byte[] keyBytes = devKey.getBytes();
        byte[] uriBytes = uriWithDeveloperID.toString().getBytes();
        Key signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        byte[] signatureBytes = mac.doFinal(uriBytes);
        StringBuffer signature = new StringBuffer(signatureBytes.length * 2);
        for (byte signatureByte : signatureBytes)
        {
            int intVal = signatureByte & 0xff;
            if (intVal < 0x10)
            {
                signature.append("0");
            }
            signature.append(Integer.toHexString(intVal));
        }
        StringBuffer url = new StringBuffer(baseURL).append(uri).append("&signature=" + signature.toString().toUpperCase());

        return url.toString();
    }

//  Departures
    public static String showRouteDepartureOnStop(int routeType, int stopId, int routeId) throws Exception {
        return buildAPIrequest("/v3/departures/route_type/"+routeType+"/stop/"+stopId+"/route/"+routeId+"?expand=all&devid="+devId);
    }
    public static String showRouteDepartureOnStop(int routeType, int stopId, int routeId, int directionId) throws Exception {
        return buildAPIrequest("/v3/departures/route_type/"+routeType+"/stop/"+stopId+"/route/"+routeId+"?direction_id="+directionId+"&expand=all&devid="+devId);
    }

    public static String nextDeparture(int routeType, int stopId) throws Exception {
        return buildAPIrequest("/v3/departures/route_type/"+routeType+"/stop/"+stopId+"?max_results=200&expand=all&devid="+devId);
    }

// Disruptions
    public static String disruptions() throws Exception {
        return buildAPIrequest("/v3/disruptions?devid="+devId);
    }
    public static String disruptionByRoute(int routeId) throws Exception {
        return buildAPIrequest("/v3/disruptions/route/"+routeId+"?devid="+devId);
    }
    public static String disruptionByStop(int stopId) throws Exception {
        return buildAPIrequest("/v3/disruptions/stop/"+stopId+"?devid="+devId);
    }
    public static String disruption(int disruptionId) throws Exception {
        return buildAPIrequest("/v3/disruptions/"+disruptionId+"?devid="+devId);
    }

//     Directions
    public static String showDirectionsOnRoute(int routeId) throws Exception {  // View directions that a route travels in
        return buildAPIrequest("/v3/directions/route/"+routeId+"?devid="+devId);
    }

//     Patterns
    public static String showPatternonRoute(int runId, int routeType) throws Exception{    // View the stopping pattern for a specific trip/service run
        return buildAPIrequest("/v3/pattern/run/"+runId+"/route_type/"+routeType+"?expand=all&devid="+devId);
    }

//     Routes
    public static String showRouteInfo(int routeId) throws Exception {                      // View route name and number for specific route ID
        return buildAPIrequest("/v3/routes/"+routeId+"?devid="+devId);
    }

    //Runs
    public static String showRouteRuns(int routeId) throws Exception {      //View all trip/service runs for a specific route ID and route type
        return buildAPIrequest("/v3/runs/route/"+routeId+"?devid="+devId);
    }

    public static String showRoutesRun(int routeId, int routeType) throws Exception {      //View all trip/service runs for a specific route ID and route type
        return buildAPIrequest("/v3/runs/route/"+routeId+"/route_type/"+routeType+"?devid="+devId);
    }

    public static String showRunInfo(int runId, int routeType) throws Exception {          // View the trip/service run for a specific run ID and route type
        return buildAPIrequest("/v3/runs/"+runId+"/route_type/"+routeType+"?devid="+devId);
    }

    // Search
    public static String showSearchResults(String searchTerm) throws Exception {            // View stops, routes and myki ticket outlets that match the search term
        return buildAPIrequest("/v3/search/"+searchTerm+"?devid="+devId);
    }
    public static String showSearchResults(String searchTerm, float latitude, float longitude) throws Exception {   // View stops, routes and myki ticket outlets that match the search term
        return buildAPIrequest("/v3/search/"+searchTerm+"?latitude="+latitude+"&longitude="+longitude+"&devid="+devId);
    }

    // Stops
    public static String showStopsInfo(int stopId, int routeType) throws Exception {       // View facilities at a specific stop (Metro and V/Line stations only)
        return buildAPIrequest("/v3/stops/"+stopId+"/route_type/"+routeType+"?stop_location=true&devid="+devId);
    }

    public static String showRoutesStop(int routeId, int routeType) throws Exception {     // View all stops on a specific route
        return buildAPIrequest("/v3/stops/route/"+routeId+"/route_type/"+routeType+"?devid="+devId);
    }

    public static String nearByStops(float latitude, float longtitude) throws Exception {    // View all stops near a specific location
        return buildAPIrequest("/v3/stops/location/"+latitude+","+longtitude+"?max_results=3&max_distance=1500&devid="+devId);
    }

    public static String nearByStopsOnSelect(float latitude, float longtitude) throws Exception {    // View all stops near a specific location
        return buildAPIrequest("/v3/stops/location/"+latitude+","+longtitude+"?max_results=750&max_distance=10000&devid="+devId);
    }

    public static String nearByTrainStopsOnSelect(float latitude, float longtitude) throws Exception {    // View metro and vline stops only at large scale
        return buildAPIrequest("/v3/stops/location/"+latitude+","+longtitude+"?route_types=0&route_types=3&max_results=750&max_distance=9999999&devid="+devId);
    }

    public static String nearByVlineStopsOnSelect(float latitude, float longtitude) throws Exception {    // View vline stops only at large scale
        return buildAPIrequest("/v3/stops/location/"+latitude+","+longtitude+"?route_types=3&max_results=750&max_distance=9999999&devid="+devId);
    }
}