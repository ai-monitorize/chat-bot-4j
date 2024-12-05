package ai.monitorize.chatbot4j.chat;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository repository;

    @Transactional
    public Optional<Chat> get(UUID uuid) {

        return repository.findByUuid(uuid);
    }

    @Transactional
    public Set<ChatDtos.ChatDto> getAll(Long parentId) {

        List<Chat> entities = repository.findAllByParentId(parentId);

        return entities.stream()
                .map(this::fromEntityToDto)
                .collect(Collectors.toSet());
    }

    @Transactional
    public ChatDtos.ChatDto create(Long parentId, ChatDtos.ChatCreateUpdateDto dto) {

        Chat entity = fromCreateDtoToEntity(parentId, dto);

        entity = repository.save(entity);

        return fromEntityToDto(entity);
    }

    @Transactional
    public void update(Long parentId, Long id, ChatDtos.ChatCreateUpdateDto dto) {

        Optional<Chat> existing = repository.findById(id);

        if (existing.isEmpty()) {
            throw new RuntimeException(String.format("Entity not found [ id=%s ]", id));
        }

        Chat entity = fromUpdateDtoToEntity(id, dto);

        repository.save(entity);
    }

    private Chat fromUpdateDtoToEntity(Long id, ChatDtos.ChatCreateUpdateDto dto) {

        return Chat.builder()
                .id(id)
                .name(dto.getName())
                .build();
    }

    private Chat fromCreateDtoToEntity(Long parentId, ChatDtos.ChatCreateUpdateDto dto) {

        return Chat.builder()
                .parentId(parentId)
                .name(dto.getName())
                .build();
    }

    private ChatDtos.ChatDto fromEntityToDto(Chat entity) {

        return ChatDtos.ChatDto.builder()
                .uuid(entity.getUuid())
                .name(entity.getName())
                .build();
    }
}
