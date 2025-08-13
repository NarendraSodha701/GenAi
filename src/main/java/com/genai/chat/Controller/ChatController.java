package com.genai.chat.Controller;

import com.genai.chat.DTO.ForecastResponse;
import com.genai.chat.Service.ChatAiAssistant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class ChatController {

    @Autowired
    private ChatAiAssistant chatAiAssistant;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/sun-forecast")
    public ResponseEntity<?> getSunForecast(@RequestParam String city) {
        if (city == null || city.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("City name must not be empty");
        }
        try {
            // Use LangChain4j to get AI-generated response
            String aiResponse = chatAiAssistant.getSunriseSunsetInfo(city);

            // Parse the AI response and create ForecastResponse
            ForecastResponse response = parseAiResponseToForecastResponse(aiResponse, city);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error: " + e.getMessage());
        }
    }

    private ForecastResponse parseAiResponseToForecastResponse(String aiResponse, String city) {
        try {
            // Try to parse JSON response from AI
            var jsonNode = objectMapper.readTree(aiResponse);
            String sunrise = jsonNode.has("sunrise") ? jsonNode.get("sunrise").asText() : "07:00:00+05:30";
            String sunset = jsonNode.has("sunset") ? jsonNode.get("sunset").asText() : "18:00:00+05:30";
            String message = jsonNode.has("message") ? jsonNode.get("message").asText() : aiResponse;

            return new ForecastResponse(city, sunrise, sunset, message);
        } catch (Exception e) {
            // Fallback: use the AI response as the enhanced message
            String fallbackSunrise = "2023-11-01T07:12:00+05:30";
            String fallbackSunset = "2023-11-01T17:49:00+05:30";
            return new ForecastResponse(city, fallbackSunrise, fallbackSunset, aiResponse);
        }
    }
}
