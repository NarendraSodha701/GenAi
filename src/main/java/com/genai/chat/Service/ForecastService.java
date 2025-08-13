package com.genai.chat.Service;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.P;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ForecastService {

    private final RestTemplate restTemplate;

    public ForecastService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final String OPEN_METEO_URL = "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&daily=sunrise,sunset&timezone=%s";

    @Tool("This method will fetch daily sunrise and sunset time for given city")
    public String getSunTimes(@P("it is a latitude of given city") double lat,
                              @P("it is a longitude of given city") double lon,
                              @P("It's is timezone of the city , if not passed default is IST") String tz) {
        String url = String.format(OPEN_METEO_URL, lat, lon, tz);
        return restTemplate.getForObject(url, String.class);
    }

}
