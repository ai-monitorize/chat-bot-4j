package ai.monitorize.chatbot4j.message;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT cm from ChatMessage cm WHERE cm.parentId=?1 AND cm.role='SYSTEM'")
    ChatMessage findByParentIdAndSystemMessageTrue(Long parentId);

    @Query("SELECT cm from ChatMessage cm WHERE cm.parentId=?1 AND cm.role!='SYSTEM' ORDER BY cm.createdAt DESC")
    Stream<ChatMessage> findAllByParentIdAndSystemMessageFalseOrderByCreatedAtDesc(Long parentId, Limit limit);

    List<ChatMessage> findAllByParentIdOrderByCreatedAtAsc(Long parentId, Pageable pageable);
}
