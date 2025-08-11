package Config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.azure.AzureOpenAiChatModel;

@Configuration
public class ChatConfig {


    @Value("${langchain4j.azure-open-ai.endpoint}")
    private String endpoint;

    @Value("${langchain4j.azure-open-ai.api-key}")
    private String apiKey;

    @Value("${langchain4j.azure-open-ai.deployment-name}")
    private String deploymentName;


    @Bean
    public ChatModel chatModel() {
        return AzureOpenAiChatModel.builder()
                .apiKey(apiKey)
                .endpoint(endpoint)
                .deploymentName(deploymentName)
                .build();
    }
}

