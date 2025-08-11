package com.genai.chat.Service;

public class LangChain4jService {
    public String generateMessage(String city, String sunrise, String sunset) {
        // or LangChain4j integration
        return String.format("Tomorrow in %s, the sun will rise at %s and set at %s. Enjoy the stunning golden hour!", city, sunrise.substring(11,16), sunset.substring(11,16));
    }
}
