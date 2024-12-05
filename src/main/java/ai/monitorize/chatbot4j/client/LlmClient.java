package ai.monitorize.chatbot4j.client;

import ai.monitorize.chatbot4j.message.ChatMessageDtos;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class LlmClient {

    private final WebClient webClient;
    private final String url;

    public LlmClient(WebClient webClient, @Value("${chat-bot.url}") String url) {

        this.webClient = webClient;
        this.url = url;
    }

    public Flux<ChatMessageDtos.ChatMessageDto> stream(List<ChatMessageDtos.ChatMessageDto> messages) {

        return webClient.post()
                .uri(url)
                .bodyValue(Map.of("messages", messages))
                .retrieve()
                .bodyToFlux(ChatMessageDtos.ChatMessageDto.class);
    }

    public Mono<ChatMessageDtos.ChatMessageDto> get(List<ChatMessageDtos.ChatMessageDto> messages) {

        return webClient.post()
                .uri(url + "?stream=false")
                .bodyValue(Map.of("messages", messages))
                .retrieve()
                .bodyToMono(ChatMessageDtos.ChatMessageDto.class);
    }
}
