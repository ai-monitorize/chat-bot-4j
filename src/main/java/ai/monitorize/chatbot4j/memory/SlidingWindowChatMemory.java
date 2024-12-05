package ai.monitorize.chatbot4j.memory;

import ai.monitorize.chatbot4j.message.ChatMessage;
import ai.monitorize.chatbot4j.message.ChatMessageDtos;
import ai.monitorize.chatbot4j.message.ChatMessageMapper;
import ai.monitorize.chatbot4j.message.ChatMessageRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class SlidingWindowChatMemory {

    private final Integer maxMessagesCount;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageMapper chatMessageMapper;

    public SlidingWindowChatMemory(@Value("${chat-memory.sliding-window.size}") Integer maxMessagesCount,
                                   ChatMessageRepository chatMessageRepository, ChatMessageMapper chatMessageMapper) {

        this.maxMessagesCount = maxMessagesCount;
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessageMapper = chatMessageMapper;
    }


    @Transactional
    public List<ChatMessageDtos.ChatMessageDto> getChatMemory(Long chatId) {

        ChatMessage systemMessage = chatMessageRepository.findByParentIdAndSystemMessageTrue(chatId);

        List<ChatMessageDtos.ChatMessageDto> nonSystemMessage = chatMessageRepository.findAllByParentIdAndSystemMessageFalseOrderByCreatedAtDesc(chatId, Limit.of(maxMessagesCount - 1))
                .sorted(Comparator.comparing(ChatMessage::getCreatedAt))
                .map(chatMessageMapper::from)
                .toList();

        List<ChatMessageDtos.ChatMessageDto> messages = new ArrayList<>();
        messages.add(chatMessageMapper.from(systemMessage));
        messages.addAll(nonSystemMessage);

        return messages;
    }

    @Transactional
    public void add(ChatMessage message) {

        chatMessageRepository.save(message);
    }
}
