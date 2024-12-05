package ai.monitorize.chatbot4j.message;

import ai.monitorize.chatbot4j.chat.Chat;
import ai.monitorize.chatbot4j.chat.ChatService;
import ai.monitorize.chatbot4j.client.LlmClient;
import ai.monitorize.chatbot4j.memory.SlidingWindowChatMemory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatService chatService;
    private final ChatMessageRepository repository;
    private final LlmClient llmClient;
    private final SlidingWindowChatMemory slidingWindowChatMemory;

    @Transactional
    public void clearChat(UUID parentUuid) {

        Optional<Chat> parent = chatService.get(parentUuid);

        if (parent.isEmpty()) {
            throw new RuntimeException("Chat not found");
        }

        List<ChatMessage> entities = repository
                .findAllByParentIdAndSystemMessageFalseOrderByCreatedAtDesc(parent.get().getId(), Limit.of(10000))
                .toList();
        repository.deleteAll(entities);
    }

    @Transactional
    public List<ChatMessageDtos.ChatMessageDto> getHistory(UUID parentUuid, Pageable pageable) {

        Optional<Chat> parent = chatService.get(parentUuid);

        if (parent.isEmpty()) {
            throw new RuntimeException("Chat not found");
        }

        List<ChatMessage> entities = repository.findAllByParentIdOrderByCreatedAtAsc(parent.get().getId(), pageable);

        return entities.stream()
                .map(this::fromEntityToDto)
                .toList();
    }

    public Flux<ChatMessageDtos.ChatMessageDto> generate(UUID parentUuid, ChatMessageDtos.ChatMessageCreateDto dto) {

        Optional<Chat> parent = chatService.get(parentUuid);

        if (parent.isEmpty()) {
            throw new RuntimeException("Chat not found]");
        }

        Long chatId = parent.get().getId();

        ChatMessage chatMessage = fromCreateDtoToEntity(chatId, dto);

        slidingWindowChatMemory.add(chatMessage);
        List<ChatMessageDtos.ChatMessageDto> messages = slidingWindowChatMemory.getChatMemory(chatId);

        AtomicReference<ChatMessageDtos.ChatMessageDto> latestValue = new AtomicReference<>();

        return llmClient
                .stream(messages)
                .doOnNext(val -> updateContent(latestValue, val))
                .doOnComplete(() -> repository.save(fromDtoToEntity(chatId, latestValue.get())));
    }

    private void updateContent(AtomicReference<ChatMessageDtos.ChatMessageDto> oldValue, ChatMessageDtos.ChatMessageDto dto) {

        ChatMessageDtos.ChatMessageDto newValue = new ChatMessageDtos.ChatMessageDto(dto.getUuid(), dto.getRole(), dto.getContent());

        if (oldValue.get() != null) {
            String oldContent = oldValue.get().getContent();
            newValue.setContent(oldContent + newValue.getContent());
        }

        oldValue.set(newValue);
    }

    private ChatMessage fromCreateDtoToEntity(Long parentId, ChatMessageDtos.ChatMessageCreateDto dto) {

        return ChatMessage.builder()
                .parentId(parentId)
                .content(dto.getContent())
                .role(dto.getRole())
                .build();
    }

    private ChatMessage fromDtoToEntity(Long parentId, ChatMessageDtos.ChatMessageDto dto) {

        return ChatMessage.builder()
                .parentId(parentId)
                .content(dto.getContent())
                .role(dto.getRole())
                .build();
    }

    private ChatMessageDtos.ChatMessageDto fromEntityToDto(ChatMessage entity) {

        return ChatMessageDtos.ChatMessageDto.builder()
                .uuid(entity.getUuid())
                .content(entity.getContent())
                .role(entity.getRole())
                .build();
    }
}
