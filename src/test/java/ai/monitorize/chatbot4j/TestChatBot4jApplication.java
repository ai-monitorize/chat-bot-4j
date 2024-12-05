package ai.monitorize.chatbot4j;

import org.springframework.boot.SpringApplication;

public class TestChatBot4jApplication {

    public static void main(String[] args) {

        SpringApplication.from(ChatBot4jApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
