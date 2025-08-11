package com.genai.chat;

import com.genai.chat.Controller.ChatController;
import com.genai.chat.DTO.ForecastResponse;
import com.genai.chat.Service.ForecastService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ForecastControllerTest {
    private ForecastService forecastService;
    private ChatController controller;

    @BeforeEach
    void setUp() {
        forecastService = mock(ForecastService.class);
        controller = new ChatController(forecastService);
    }

    @Test
    void testValidCityReturnsOk() {
        ForecastResponse resp = new ForecastResponse("Berlin", "2023-11-01T07:12:00+05:30", "2023-11-01T17:49:00+05:30", "AI message");
        when(forecastService.getForecast("Berlin")).thenReturn(resp);
        ResponseEntity<?> response = controller.getSunForecast("Berlin");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(ForecastResponse.class, response.getBody());
    }

    @Test
    void testEmptyCityReturnsBadRequest() {
        ResponseEntity<?> response = controller.getSunForecast("");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("City name must not be empty", response.getBody());
    }

    @Test
    void testServiceThrowsIllegalArgumentException() {
        when(forecastService.getForecast("InvalidCity")).thenThrow(new IllegalArgumentException("Invalid city name"));
        ResponseEntity<?> response = controller.getSunForecast("InvalidCity");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid city name", response.getBody());
    }

    @Test
    void testServiceThrowsOtherException() {
        when(forecastService.getForecast("Berlin")).thenThrow(new RuntimeException("Unexpected error"));
        ResponseEntity<?> response = controller.getSunForecast("Berlin");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal error", response.getBody());
    }
}

