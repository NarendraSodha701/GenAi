package com.genai.chat.Service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.spring.AiService;


@AiService public interface ChatAiAssistant {
    @SystemMessage("You are a helpful assistant that gives sunrise/sunset info in a friendly way. Strictly show sunrise and sunset time in IST timezone")
    String getSunriseSunsetInfo(String city);
}
