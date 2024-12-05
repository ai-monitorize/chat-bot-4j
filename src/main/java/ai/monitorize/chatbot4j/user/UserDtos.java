package ai.monitorize.chatbot4j.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserDtos {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class UserCreateUpdateDto {

        private String email;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    public static class UserDto {

        private long id;

        private String email;
    }

}
