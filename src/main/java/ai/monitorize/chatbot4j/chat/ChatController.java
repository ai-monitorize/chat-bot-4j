package ai.monitorize.chatbot4j.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/v1/users/{parentId}/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService service;

    @GetMapping
    public ResponseEntity<Set<ChatDtos.ChatDto>> getAll(@PathVariable Long parentId) {

        return ResponseEntity
                .ok(service.getAll(parentId));
    }

    @PostMapping
    public ResponseEntity<Void> create(@PathVariable Long parentId, @RequestBody ChatDtos.ChatCreateUpdateDto dto) {

        service.create(parentId, dto);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ChatDtos.ChatDto> update(@PathVariable Long parentId, @PathVariable Long id, @RequestBody ChatDtos.ChatCreateUpdateDto dto) {

        service.update(parentId, id, dto);

        return ResponseEntity
                .noContent()
                .build();
    }
}
