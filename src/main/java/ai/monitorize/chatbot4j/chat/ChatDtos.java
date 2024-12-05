package ai.monitorize.chatbot4j.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class ChatDtos {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class ChatCreateUpdateDto {

        private String name;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class ChatDto {

        private UUID uuid;
        private String name;
    }
}
