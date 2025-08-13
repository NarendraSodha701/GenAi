package com.genai.chat.Controller;

import dev.langchain4j.model.chat.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatAiController {
    @Autowired
    private ChatModel model;

    @GetMapping("/openapi")
    public String retrieveAiResponse(@RequestParam(name = "message") String message) {
        return model.chat(message);
    }

}
