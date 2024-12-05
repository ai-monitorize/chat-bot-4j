package ai.monitorize.chatbot4j.chat;

import ai.monitorize.chatbot4j.message.ChatMessage;
import ai.monitorize.chatbot4j.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "chat")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long parentId;

    @OneToMany(mappedBy = "chat", orphanRemoval = true, cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private Set<ChatMessage> messages = new HashSet<>();

    @Builder
    public Chat(Long id, String name, Long parentId) {

        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.uuid = UUID.randomUUID();
    }
}
