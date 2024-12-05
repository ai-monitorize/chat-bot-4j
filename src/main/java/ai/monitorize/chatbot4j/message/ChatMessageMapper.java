package ai.monitorize.chatbot4j.message;

import org.springframework.stereotype.Component;

@Component
public class ChatMessageMapper {

    public ChatMessageDtos.ChatMessageDto from(ChatMessage chatMessage) {

        return ChatMessageDtos.ChatMessageDto.builder()
                .uuid(chatMessage.getUuid())
                .role(chatMessage.getRole())
                .content(chatMessage.getContent())
                .build();
    }
}
