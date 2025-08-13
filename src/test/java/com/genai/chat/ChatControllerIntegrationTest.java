package com.genai.chat;

import com.genai.chat.Controller.ChatController;
import com.genai.chat.Service.ChatAiAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatController.class)
public class ChatControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatAiAssistant chatAiAssistant;

    @Test
    void testGetSunForecastWithValidCity() throws Exception {
        String mockResponse = "{\"city\":\"Berlin\",\"sunrise\":\"2023-11-01T07:12:00+05:30\",\"sunset\":\"2023-11-01T17:49:00+05:30\",\"message\":\"Tomorrow in Berlin, the sun will rise at 7:12 AM IST and set at 5:49 PM IST. Enjoy the stunning golden hour!\"}";

        when(chatAiAssistant.getSunriseSunsetInfo("Berlin")).thenReturn(mockResponse);

        mockMvc.perform(get("/api/sun-forecast")
                .param("city", "Berlin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Berlin"))
                .andExpect(jsonPath("$.sunrise").value("2023-11-01T07:12:00+05:30"))
                .andExpect(jsonPath("$.sunset").value("2023-11-01T17:49:00+05:30"))
                .andExpect(jsonPath("$.enhanced_message").exists());
    }

    @Test
    void testGetSunForecastWithEmptyCity() throws Exception {
        mockMvc.perform(get("/api/sun-forecast")
                .param("city", ""))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("City name must not be empty"));
    }

    @Test
    void testGetSunForecastWithNullCity() throws Exception {
        mockMvc.perform(get("/api/sun-forecast"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetSunForecastWithNaturalLanguageResponse() throws Exception {
        String naturalResponse = "Tomorrow in Tokyo, the sun will rise at 6:45 AM IST and set at 5:30 PM IST. What a beautiful day to enjoy the cherry blossoms during the magical golden hour!";

        when(chatAiAssistant.getSunriseSunsetInfo("Tokyo")).thenReturn(naturalResponse);

        mockMvc.perform(get("/api/sun-forecast")
                .param("city", "Tokyo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Tokyo"))
                .andExpect(jsonPath("$.sunrise").value("2023-11-01T07:12:00+05:30")) // Fallback timestamp
                .andExpect(jsonPath("$.sunset").value("2023-11-01T17:49:00+05:30"))  // Fallback timestamp
                .andExpect(jsonPath("$.enhanced_message").value(naturalResponse));
    }

    @Test
    void testGetSunForecastWithServiceException() throws Exception {
        when(chatAiAssistant.getSunriseSunsetInfo(anyString())).thenThrow(new RuntimeException("Service unavailable"));

        mockMvc.perform(get("/api/sun-forecast")
                .param("city", "London"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal error: Service unavailable"));
    }
}
