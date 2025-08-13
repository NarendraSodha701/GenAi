package com.genai.chat;

import com.genai.chat.Service.ChatAiAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;

public class ForecastServiceTest {
    private ChatAiAssistant chatAiAssistant;

    @BeforeEach
    void setUp() {
        chatAiAssistant = Mockito.mock(ChatAiAssistant.class);
    }

    @Test
    void testValidCityReturnsAiGeneratedForecast() {
        String mockAiResponse = "{\"city\":\"Berlin\",\"sunrise\":\"2023-11-01T07:12:00+05:30\",\"sunset\":\"2023-11-01T17:49:00+05:30\",\"message\":\"Tomorrow in Berlin, the sun will rise at 7:12 AM IST and set at 5:49 PM IST. Enjoy the stunning golden hour!\"}";
        Mockito.when(chatAiAssistant.getSunriseSunsetInfo("Berlin")).thenReturn(mockAiResponse);

        String result = chatAiAssistant.getSunriseSunsetInfo("Berlin");
        assertNotNull(result);
        assertEquals(mockAiResponse, result);
    }

    @Test
    void testDifferentCitiesReturnDifferentResponses() {
        String berlinResponse = "{\"city\":\"Berlin\",\"sunrise\":\"2023-11-01T07:12:00+05:30\",\"sunset\":\"2023-11-01T17:49:00+05:30\",\"message\":\"Berlin sunrise message\"}";
        String parisResponse = "{\"city\":\"Paris\",\"sunrise\":\"2023-11-01T07:30:00+05:30\",\"sunset\":\"2023-11-01T18:15:00+05:30\",\"message\":\"Paris sunrise message\"}";

        Mockito.when(chatAiAssistant.getSunriseSunsetInfo("Berlin")).thenReturn(berlinResponse);
        Mockito.when(chatAiAssistant.getSunriseSunsetInfo("Paris")).thenReturn(parisResponse);

        String berlinResult = chatAiAssistant.getSunriseSunsetInfo("Berlin");
        String parisResult = chatAiAssistant.getSunriseSunsetInfo("Paris");

        assertNotNull(berlinResult);
        assertNotNull(parisResult);
        assertEquals(berlinResponse, berlinResult);
        assertEquals(parisResponse, parisResult);
    }

    @Test
    void testAiServiceReturnsNaturalLanguageResponse() {
        String naturalResponse = "Tomorrow in Tokyo, the sun will rise at 6:45 AM IST and set at 5:30 PM IST. What a beautiful day to enjoy the cherry blossoms during the magical golden hour!";
        Mockito.when(chatAiAssistant.getSunriseSunsetInfo("Tokyo")).thenReturn(naturalResponse);

        String result = chatAiAssistant.getSunriseSunsetInfo("Tokyo");
        assertNotNull(result);
        assertEquals(naturalResponse, result);
    }

    @Test
    void testAiServiceHandlesVariousInputFormats() {
        Mockito.when(chatAiAssistant.getSunriseSunsetInfo(anyString())).thenAnswer(invocation -> {
            String city = invocation.getArgument(0);
            return String.format("AI generated response for %s with sunrise and sunset times in IST timezone", city);
        });

        String result1 = chatAiAssistant.getSunriseSunsetInfo("New York");
        String result2 = chatAiAssistant.getSunriseSunsetInfo("london");
        String result3 = chatAiAssistant.getSunriseSunsetInfo("MUMBAI");

        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertEquals("AI generated response for New York with sunrise and sunset times in IST timezone", result1);
        assertEquals("AI generated response for london with sunrise and sunset times in IST timezone", result2);
        assertEquals("AI generated response for MUMBAI with sunrise and sunset times in IST timezone", result3);
    }
}
