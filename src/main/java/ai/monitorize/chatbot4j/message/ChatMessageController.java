package ai.monitorize.chatbot4j.message;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/chats/{parentUuid}/messages")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService service;

    @GetMapping
    public ResponseEntity<List<ChatMessageDtos.ChatMessageDto>> getHistory(@PathVariable UUID parentUuid) {

        return ResponseEntity
                .ok(service.getHistory(parentUuid, Pageable.ofSize(10000)));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearChat(@PathVariable UUID parentUuid) {

        service.clearChat(parentUuid);

        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ChatMessageDtos.ChatMessageDto> generate(@PathVariable UUID parentUuid, @RequestBody ChatMessageDtos.ChatMessageCreateDto dto) {

        return service.generate(parentUuid, dto);
    }
}
