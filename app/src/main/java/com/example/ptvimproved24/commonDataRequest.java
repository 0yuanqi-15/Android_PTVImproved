package com.example.ptvimproved24;

import java.security.Key;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class commonDataRequest {

    private static final String baseURL = "https://ptv-dataconnect.herokuapp.com";
    public static String buildAPIrequest(final String uri) throws Exception
    {
        StringBuffer url = new StringBuffer(baseURL).append(uri);
        return url.toString();
    }

//  Departures
    public static String showRouteDepartureOnStop(int routeType, int stopId, int routeId) throws Exception {
        return buildAPIrequest("/v3/departures/route_type/"+routeType+"/stop/"+stopId+"/route/"+routeId+"?expand=all");
    }
    public static String showRouteDepartureOnStop(int routeType, int stopId, int routeId, int directionId) throws Exception {
        return buildAPIrequest("/v3/departures/route_type/"+routeType+"/stop/"+stopId+"/route/"+routeId+"?direction_id="+directionId+"&expand=all");
    }

    public static String nextDeparture(int routeType, int stopId) throws Exception {
        return buildAPIrequest("/v3/departures/route_type/"+routeType+"/stop/"+stopId+"?max_results=200&expand=all");
    }

// Disruptions
    public static String disruptions() throws Exception {
        return buildAPIrequest("/v3/disruptions");
    }
    public static String disruptionByRoute(int routeId) throws Exception {
        return buildAPIrequest("/v3/disruptions/route/"+routeId);
    }
    public static String disruptionByStop(int stopId) throws Exception {
        return buildAPIrequest("/v3/disruptions/stop/"+stopId);
    }
    public static String disruption(int disruptionId) throws Exception {
        return buildAPIrequest("/v3/disruptions/"+disruptionId);
    }

//     RouteDirections）
    public static String showDirectionsOnRoute(int routeId) throws Exception {  // View directions that a route travels in
        return buildAPIrequest("/v3/directions/route/"+routeId);
    }

//     Patterns
    public static String showPatternonRoute(String run_ref, int routeType) throws Exception{    // View the stopping pattern for a specific trip/service run
        return buildAPIrequest("/v3/pattern/run/"+run_ref+"/route_type/"+routeType+"?expand=All");
    }

//     Routes
    public static String showRouteInfo(int routeId) throws Exception {                      // View route name and number for specific route ID
        return buildAPIrequest("/v3/routes/"+routeId);
    }
    public static String showRouteInfoWithPath(int routeId) throws Exception{
        return buildAPIrequest("/v3/routes/"+routeId+"?include_geopath=true");
    }

    //Runs
    public static String showRouteRuns(int routeId) throws Exception {      //View all trip/service runs for a specific route ID and route type
        return buildAPIrequest("/v3/runs/route/"+routeId);
    }

    public static String showRoutesRun(int routeId, int routeType) throws Exception {      //View all trip/service runs for a specific route ID and route type
        return buildAPIrequest("/v3/runs/route/"+routeId+"/route_type/"+routeType);
    }

    public static String showRunInfo(int runId, int routeType) throws Exception {          // View the trip/service run for a specific run ID and route type
        return buildAPIrequest("/v3/runs/"+runId+"/route_type/"+routeType);
    }

    // Search
    public static String showSearchResults(String searchTerm) throws Exception {            // View stops, routes and myki ticket outlets that match the search term
        return buildAPIrequest("/v3/search/"+searchTerm);
    }
    public static String showSearchResults(String searchTerm, float latitude, float longitude) throws Exception {   // View stops, routes and myki ticket outlets that match the search term
        return buildAPIrequest("/v3/search/"+searchTerm+"?latitude="+latitude+"&longitude="+longitude+"");
    }

    // Stops
    public static String showStopsInfo(int stopId, int routeType) throws Exception {       // View facilities at a specific stop (Metro and V/Line stations only)
        return buildAPIrequest("/v3/stops/"+stopId+"/route_type/"+routeType+"?stop_location=true");
    }

    public static String showRoutesStop(int routeId, int routeType) throws Exception {     // View all stops on a specific route
        return buildAPIrequest("/v3/stops/route/"+routeId+"/route_type/"+routeType);
    }

    public static String showRoutesStopByDirectionId(int routeId, int routeType, int directionId) throws Exception {     // View all stops on a specific route
        return buildAPIrequest("/v3/stops/route/"+routeId+"/route_type/"+routeType+"?direction_id="+directionId);
    }

    public static String nearByStops(float latitude, float longtitude) throws Exception {    // View all stops near a specific location
        return buildAPIrequest("/v3/stops/location/"+latitude+","+longtitude+"?max_results=5&max_distance=2000");
    }

    public static String nearByStopsOnSelect(float latitude, float longtitude) throws Exception {    // View all stops near a specific location
        return buildAPIrequest("/v3/stops/location/"+latitude+","+longtitude+"?max_results=100&max_distance=8000");
    }

    public static String nearByTrainStopsOnSelect(float latitude, float longtitude) throws Exception {    // View metro and vline stops only at large scale
        return "https://ptv.testingstar.top/stops03.json";
    }

    public static String nearByVlineStopsOnSelect(float latitude, float longtitude) throws Exception {    // View vline stops only at large scale
        return "https://ptv.testingstar.top/stops03.json";
    }
}