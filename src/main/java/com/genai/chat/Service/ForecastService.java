package com.genai.chat.Service;


import com.genai.chat.DTO.ForecastResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service

public class ForecastService {
    private final RestTemplate restTemplate;
    private final GeocodingService geocodingService;
    private final LangChain4jService langChain4jService;

    public ForecastService(RestTemplate restTemplate, GeocodingService geocodingService, LangChain4jService langChain4jService) {
        this.restTemplate = restTemplate;
        this.geocodingService = geocodingService;
        this.langChain4jService = langChain4jService;
    }

    public ForecastResponse getForecast(String city) {
        GeocodeResult geo = geocodingService.geocode(city);
        if (geo == null) throw new IllegalArgumentException("Invalid city name");
        String url = String.format("https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&daily=sunrise,sunset&timezone=IST", geo.getLat(), geo.getLon());
        String apiResponse = restTemplate.getForObject(url, String.class);
        // api response parsing
        JSONObject obj = new JSONObject(apiResponse);
        JSONObject daily = obj.getJSONObject("daily");
        JSONArray sunriseArr = daily.getJSONArray("sunrise");
        JSONArray sunsetArr = daily.getJSONArray("sunset");
        String sunrise = sunriseArr.length() > 0 ? sunriseArr.getString(0) : null;
        String sunset = sunsetArr.length() > 0 ? sunsetArr.getString(0) : null;
        if (sunrise == null || sunset == null) throw new IllegalArgumentException("No sunrise/sunset data found");
        String enhanced = langChain4jService.generateMessage(city, sunrise, sunset);
        return new ForecastResponse(city, sunrise, sunset, enhanced);
    }

}
