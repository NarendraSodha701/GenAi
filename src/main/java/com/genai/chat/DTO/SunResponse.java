package com.genai.chat.DTO;

public class SunResponse {
    private String city;
    private String sunrise;
    private String sunset;
    private String message;

    public SunResponse() {}

    public SunResponse(String city, String sunrise, String sunset, String message) {
        this.city = city;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.message = message;
    }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getSunrise() { return sunrise; }
    public void setSunrise(String sunrise) { this.sunrise = sunrise; }
    public String getSunset() { return sunset; }
    public void setSunset(String sunset) { this.sunset = sunset; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
