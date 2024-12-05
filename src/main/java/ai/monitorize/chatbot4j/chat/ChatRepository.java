package ai.monitorize.chatbot4j.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findAllByParentId(Long parentId);
    Optional<Chat> findByUuid(UUID uuid);
}
