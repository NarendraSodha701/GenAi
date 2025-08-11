package com.genai.chat.DTO;

public class ForecastResponse {
    private String city;
    private String sunrise;
    private String sunset;
    private String enhancedMessage;

    public ForecastResponse() {}

    public ForecastResponse(String city, String sunrise, String sunset, String enhancedMessage) {
        this.city = city;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.enhancedMessage = enhancedMessage;
    }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getSunrise() { return sunrise; }
    public void setSunrise(String sunrise) { this.sunrise = sunrise; }
    public String getSunset() { return sunset; }
    public void setSunset(String sunset) { this.sunset = sunset; }
    public String getEnhancedMessage() { return enhancedMessage; }
    public void setEnhancedMessage(String enhancedMessage) { this.enhancedMessage = enhancedMessage; }
}
