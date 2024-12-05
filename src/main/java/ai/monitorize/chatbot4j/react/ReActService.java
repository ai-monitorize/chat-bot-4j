package ai.monitorize.chatbot4j.react;

import ai.monitorize.chatbot4j.client.LlmClient;
import ai.monitorize.chatbot4j.message.ChatMessageDtos;
import ai.monitorize.chatbot4j.message.ChatMessageRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ReActService {

    private static final String SYSTEM_MESSAGE = "You are a reasoning assistant. Solve the user query step by step using the [Thought], [Action], [Observation] framework.\n" +
            "\n" +
            "Here are the allowed actions you can choose from:\n" +
            "1. `search_online`: Use this to perform a web search. Example: search_online({\"query\": \"TV stores in Serbia\"}).\n" +
            "\n" +
            "**Do not invent new actions or modify the allowed ones.**\n" +
            "\n" +
            "---\n" +
            "\n" +
            "Follow these rules strictly:\n" +
            "1. Provide only a single [Thought] and [Action] in your response.\n" +
            "2. Wait for the [Observation] before continuing. Do not assume or generate the [Observation] yourself.\n" +
            "3. If you do not need to take further action, proceed to [Final Answer].\n" +
            "4. Stop after providing an [Action].\n" +
            "\n" +
            "---\n" +
            "\n" +
            "Example Interaction:\n" +
            "\n" +
            "User Query: Compare the weather in San Francisco and New York.\n" +
            "\n" +
            "Assistant:\n" +
            "[Thought]: I need to fetch the weather data for San Francisco.\n" +
            "[Action]: search_online({\"query\": \"weather in San Francisco\"})\n" +
            "\n" +
            "User (provides Observation): The weather in San Francisco is sunny, 20°C.\n" +
            "\n" +
            "Assistant:\n" +
            "[Thought]: Now, I need to fetch the weather data for New York.\n" +
            "[Action]: search_online({\"query\": \"weather in New York\"})\n" +
            "\n" +
            "User (provides Observation): The weather in New York is rainy, 15°C.\n" +
            "\n" +
            "Assistant:\n" +
            "[Thought]: I can now compare the weather data.\n" +
            "[Final Answer]: San Francisco is sunny at 20°C, while New York is rainy at 15°C.\n" +
            "\n" +
            "---\n" +
            "\n" +
            "Now solve the following query step by step. Remember to only use allowed actions and wait for [Observation] after each [Action]. Do not invent new actions or make assumptions.\n" +
            "\n" +
            "User Query:";

    private final LlmClient llmClient;

    public ReActService(LlmClient llmClient) {

        this.llmClient = llmClient;
    }

    public void solve(String prompt) {

        ChatMessageDtos.ChatMessageDto systemMessage = ChatMessageDtos.ChatMessageDto
                .builder()
                .content(SYSTEM_MESSAGE)
                .role(ChatMessageRole.SYSTEM)
                .build();

        ChatMessageDtos.ChatMessageDto userMessage = ChatMessageDtos.ChatMessageDto
                .builder()
                .content(prompt)
                .role(ChatMessageRole.USER)
                .build();

        log.info("User: {}", userMessage.getContent());

        List<ChatMessageDtos.ChatMessageDto> messages = new ArrayList<>(List.of(systemMessage, userMessage));

        ChatMessageDtos.ChatMessageDto assistantMessage = llmClient.get(messages).block();

        log.info("Assistant: {}", assistantMessage.getContent());

    }
}
