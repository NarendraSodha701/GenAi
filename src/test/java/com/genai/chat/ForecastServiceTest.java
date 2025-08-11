package com.genai.chat;

import com.genai.chat.DTO.ForecastResponse;
import com.genai.chat.Service.ForecastService;
import com.genai.chat.Service.GeocodeResult;
import com.genai.chat.Service.GeocodingService;
import com.genai.chat.Service.LangChain4jService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

public  class ForecastServiceTest  {
    private ForecastService service;
    private GeocodingService geocodingService;
    private LangChain4jService langChain4jService;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        geocodingService = Mockito.mock(GeocodingService.class);
        langChain4jService = Mockito.mock(LangChain4jService.class);
        restTemplate = Mockito.mock(RestTemplate.class);
        service = new ForecastService(restTemplate, geocodingService, langChain4jService);
    }

    @Test
    void testValidCityReturnsForecast() {
        Mockito.when(geocodingService.geocode("Berlin")).thenReturn(new GeocodeResult(52.5, 13.41));
        String apiResponse = "{\"daily\":{\"sunrise\":[\"2023-11-01T07:12:00+05:30\"],\"sunset\":[\"2023-11-01T17:49:00+05:30\"]}}";
        Mockito.when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(apiResponse);
        Mockito.when(langChain4jService.generateMessage(anyString(), anyString(), anyString())).thenReturn("AI message");
        ForecastResponse resp = service.getForecast("Berlin");
        assertEquals("Berlin", resp.getCity());
        assertEquals("2023-11-01T07:12:00+05:30", resp.getSunrise());
        assertEquals("2023-11-01T17:49:00+05:30", resp.getSunset());
        assertEquals("AI message", resp.getEnhancedMessage());
    }

    @Test
    void testInvalidCityThrowsException() {
        Mockito.when(geocodingService.geocode("")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> service.getForecast(""));
    }

    @Test
    void testNoSunriseSunsetThrowsException() {
        Mockito.when(geocodingService.geocode("Paris")).thenReturn(new GeocodeResult(48.85, 2.35));
        String apiResponse = "{\"daily\":{\"sunrise\":[],\"sunset\":[]}}";
        Mockito.when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(apiResponse);
        assertThrows(IllegalArgumentException.class, () -> service.getForecast("Paris"));
    }
}

