package com.genai.chat.Service;

public class GeocodeResult {
    private double lat;
    private double lon;
    public GeocodeResult(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
    public double getLat() { return lat; }
    public double getLon() { return lon; }
}
