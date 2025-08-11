package com.genai.chat.Service;

import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

public class GeocodingService {
    private final RestTemplate restTemplate = new RestTemplate();
    public GeocodeResult geocode(String city) {
       // api for geocoding

        String url = String.format("https://nominatim.openstreetmap.org/search?q=%s&format=json&limit=1", city);
        String response = restTemplate.getForObject(url, String.class);
        org.json.JSONArray arr = new org.json.JSONArray(response);
        if (arr.length() == 0)  return null;
        JSONObject obj = arr.getJSONObject(0);
        double lat = obj.getDouble("lat");
        double lon = obj.getDouble("lon");
        return new GeocodeResult(lat, lon);
    }
}
