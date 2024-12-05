package ai.monitorize.chatbot4j.user;

import ai.monitorize.chatbot4j.chat.Chat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "app_user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private Set<Chat> chats = new HashSet<>();

    @Builder
    public User(Long id, String email) {

        this.id = id;
        this.email = email;
    }
}
