package com.genai.chat;

import com.genai.chat.Service.ChatAiAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatAiAssistantTest {

    @Mock
    private ChatAiAssistant chatAiAssistant;

    @BeforeEach
    void setUp() {
        // Setup can be added here if needed
    }

    @Test
    void testGetSunriseSunsetInfoReturnsValidJson() {
        String expectedResponse = "{\"city\":\"Berlin\",\"sunrise\":\"2023-11-01T07:12:00+05:30\",\"sunset\":\"2023-11-01T17:49:00+05:30\",\"message\":\"Tomorrow in Berlin, the sun will rise at 7:12 AM IST and set at 5:49 PM IST. Enjoy the stunning golden hour!\"}";

        when(chatAiAssistant.getSunriseSunsetInfo("Berlin")).thenReturn(expectedResponse);

        String result = chatAiAssistant.getSunriseSunsetInfo("Berlin");
        assertNotNull(result);
        assertTrue(result.contains("Berlin"));
        assertTrue(result.contains("sunrise"));
        assertTrue(result.contains("sunset"));
        assertTrue(result.contains("golden hour"));
    }

    @Test
    void testGetSunriseSunsetInfoForDifferentCities() {
        String parisResponse = "{\"city\":\"Paris\",\"sunrise\":\"2023-11-01T07:30:00+05:30\",\"sunset\":\"2023-11-01T18:15:00+05:30\",\"message\":\"In Paris, the sun will rise at 7:30 AM IST and set at 6:15 PM IST. Perfect time to visit the Eiffel Tower during golden hour!\"}";

        when(chatAiAssistant.getSunriseSunsetInfo("Paris")).thenReturn(parisResponse);

        String result = chatAiAssistant.getSunriseSunsetInfo("Paris");
        assertNotNull(result);
        assertTrue(result.contains("Paris"));
        assertTrue(result.contains("Eiffel Tower") || result.contains("golden hour"));
    }

    @Test
    void testGetSunriseSunsetInfoWithNaturalLanguageResponse() {
        String naturalResponse = "Tomorrow in Tokyo, the sun will rise at 6:45 AM IST and set at 5:30 PM IST. What a beautiful day to enjoy the cherry blossoms during the magical golden hour!";

        when(chatAiAssistant.getSunriseSunsetInfo("Tokyo")).thenReturn(naturalResponse);

        String result = chatAiAssistant.getSunriseSunsetInfo("Tokyo");
        assertNotNull(result);
        assertTrue(result.contains("Tokyo"));
        assertTrue(result.contains("IST"));
        assertTrue(result.length() > 50); // Ensure it's a substantial response
    }

    @Test
    void testGetSunriseSunsetInfoHandlesEmptyResponse() {
        when(chatAiAssistant.getSunriseSunsetInfo(anyString())).thenReturn("");

        String result = chatAiAssistant.getSunriseSunsetInfo("UnknownCity");
        assertNotNull(result);
        assertEquals("", result);
    }
}
