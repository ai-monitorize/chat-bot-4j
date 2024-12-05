package ai.monitorize.chatbot4j.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    @Transactional
    public Set<UserDtos.UserDto> getAll() {

        List<User> entities = repository.findAll();

        return entities.stream()
                .map(this::fromEntityToDto)
                .collect(Collectors.toSet());
    }

    @Transactional
    public UserDtos.UserDto create(UserDtos.UserCreateUpdateDto dto) {

        User entity = fromCreateDtoToEntity(dto);

        entity = repository.save(entity);

        return fromEntityToDto(entity);
    }

    @Transactional
    public void update(Long id, UserDtos.UserCreateUpdateDto dto) {

        Optional<User> existing = repository.findById(id);

        if (existing.isEmpty()) {
            throw new RuntimeException(String.format("Entity not found [ id=%s ]", id));
        }

        User entity = fromUpdateDtoToEntity(id, dto);

        repository.save(entity);
    }

    private User fromUpdateDtoToEntity(Long id, UserDtos.UserCreateUpdateDto dto) {

        return User.builder()
                .id(id)
                .email(dto.getEmail())
                .build();
    }

    private User fromCreateDtoToEntity(UserDtos.UserCreateUpdateDto dto) {

        return User.builder()
                .email(dto.getEmail())
                .build();
    }

    private UserDtos.UserDto fromEntityToDto(User entity) {

        return UserDtos.UserDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .build();
    }
}
