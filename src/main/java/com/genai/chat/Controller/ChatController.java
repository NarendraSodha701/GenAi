package com.genai.chat.Controller;

import com.genai.chat.DTO.ForecastResponse;
import com.genai.chat.Service.ForecastService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sun-forecast")
public class ChatController {
    private final ForecastService forecastService;

    public ChatController(ForecastService forecastService) {
        this.forecastService = forecastService;
    }


    @GetMapping
    public ResponseEntity<?> getSunForecast(@RequestParam String city) {
        if (city == null || city.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("City name must not be empty");
        }
        try {
            ForecastResponse response = forecastService.getForecast(city);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error");
        }
    }
}

