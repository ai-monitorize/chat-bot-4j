package ai.monitorize.chatbot4j.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@NoArgsConstructor
public class ChatMessageDtos {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    @ToString
    public static class ChatMessageDto {

        private UUID uuid;
        private ChatMessageRole role;
        private String content;

        public void setContent(String content) {

            this.content = content;
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    @ToString
    public static class ChatMessageCreateDto {

        private String content;
        private ChatMessageRole role;
    }
}
