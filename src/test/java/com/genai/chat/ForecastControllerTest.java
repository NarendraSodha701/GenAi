package com.genai.chat;

import com.genai.chat.Controller.ChatController;
import com.genai.chat.DTO.ForecastResponse;
import com.genai.chat.Service.ChatAiAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ForecastControllerTest {
    private ChatAiAssistant chatAiAssistant;
    private ChatController controller;

    @BeforeEach
    void setUp() {
        chatAiAssistant = mock(ChatAiAssistant.class);
        controller = new ChatController();
        // Use reflection to inject the mock
        try {
            var field = ChatController.class.getDeclaredField("chatAiAssistant");
            field.setAccessible(true);
            field.set(controller, chatAiAssistant);
        } catch (Exception e) {
            fail("Failed to inject mock: " + e.getMessage());
        }
    }

    @Test
    void testValidCityReturnsOkWithAiResponse() {
        String mockAiResponse = "{\"city\":\"Berlin\",\"sunrise\":\"2023-11-01T07:12:00+05:30\",\"sunset\":\"2023-11-01T17:49:00+05:30\",\"message\":\"Tomorrow in Berlin, the sun will rise at 7:12 AM IST and set at 5:49 PM IST. Enjoy the stunning golden hour!\"}";
        when(chatAiAssistant.getSunriseSunsetInfo("Berlin")).thenReturn(mockAiResponse);

        ResponseEntity<?> response = controller.getSunForecast("Berlin");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(ForecastResponse.class, response.getBody());

        ForecastResponse forecastResponse = (ForecastResponse) response.getBody();
        assertEquals("Berlin", forecastResponse.getCity());
        assertEquals("2023-11-01T07:12:00+05:30", forecastResponse.getSunrise());
        assertEquals("2023-11-01T17:49:00+05:30", forecastResponse.getSunset());
    }

    @Test
    void testValidCityWithNonJsonAiResponse() {
        String mockAiResponse = "Tomorrow in Paris, the sun will rise at 7:12 AM IST and set at 5:49 PM IST. Enjoy the stunning golden hour!";
        when(chatAiAssistant.getSunriseSunsetInfo("Paris")).thenReturn(mockAiResponse);

        ResponseEntity<?> response = controller.getSunForecast("Paris");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(ForecastResponse.class, response.getBody());

        ForecastResponse forecastResponse = (ForecastResponse) response.getBody();
        assertEquals("Paris", forecastResponse.getCity());
        assertEquals(mockAiResponse, forecastResponse.getEnhancedMessage());
    }

    @Test
    void testEmptyCityReturnsBadRequest() {
        ResponseEntity<?> response = controller.getSunForecast("");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("City name must not be empty", response.getBody());
    }

    @Test
    void testNullCityReturnsBadRequest() {
        ResponseEntity<?> response = controller.getSunForecast(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("City name must not be empty", response.getBody());
    }

    @Test
    void testWhitespaceCityReturnsBadRequest() {
        ResponseEntity<?> response = controller.getSunForecast("   ");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("City name must not be empty", response.getBody());
    }

    @Test
    void testAiServiceThrowsException() {
        when(chatAiAssistant.getSunriseSunsetInfo("ErrorCity")).thenThrow(new RuntimeException("AI service error"));
        ResponseEntity<?> response = controller.getSunForecast("ErrorCity");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("Internal error"));
    }
}
