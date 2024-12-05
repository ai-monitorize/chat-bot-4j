package ai.monitorize.chatbot4j.message;

import ai.monitorize.chatbot4j.chat.Chat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "chat_message")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @Column(name = "content", nullable = false, updatable = false)
    private String content;

    @Column(name = "chat_id", nullable = false, updatable = false)
    private Long parentId;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private ChatMessageRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", insertable = false, updatable = false)
    private Chat chat;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Builder
    public ChatMessage(Long id, String content, Long parentId, ChatMessageRole role) {

        this.id = id;
        this.content = content;
        this.parentId = parentId;
        this.role = role;
        this.createdAt = Instant.now();
        this.uuid = UUID.randomUUID();
    }

    public Long getChatId() {

        return parentId;
    }

    public boolean isSystemMessage() {

        return ChatMessageRole.SYSTEM.equals(role);
    }

    public boolean isUserMessage() {

        return ChatMessageRole.USER.equals(role);
    }

    public boolean isAssistantMessage() {

        return ChatMessageRole.ASSISTANT.equals(role);
    }
}
