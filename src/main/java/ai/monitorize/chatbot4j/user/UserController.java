package ai.monitorize.chatbot4j.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<Set<UserDtos.UserDto>> getAll() {

        return ResponseEntity
                .ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<UserDtos.UserDto> create(@RequestBody UserDtos.UserCreateUpdateDto dto) {

        UserDtos.UserDto created = service.create(dto);

        return ResponseEntity
                .created(URI.create(String.valueOf(created.getId())))
                .build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDtos.UserDto> update(@PathVariable Long id, @RequestBody UserDtos.UserCreateUpdateDto dto) {

        service.update(id, dto);

        return ResponseEntity
                .noContent()
                .build();
    }
}
