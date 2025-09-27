package com.capgemini.volcamp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.content.Media;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StartupConfiguration implements CommandLineRunner {

    private final ChatClient.Builder chatClientBuilder;

    private void askQuestion(final String question) {

        var imageResource = new ClassPathResource("/logo.png");

        var imageMessage = UserMessage.builder()
                .media(new Media(MimeTypeUtils.IMAGE_PNG, imageResource))
                .text(question)
                .build();

        ChatResponse response = chatClientBuilder.build().prompt()
                .messages(List.of(imageMessage))
                .call()
                .chatResponse();

        log.info("Response: {}", response.getResult().getOutput().getText());
    }

    @Override
    public void run(String... args) {
        var question = """
            Que vois tu dans cette image ?
        """;

        this.askQuestion(question);
    }
}
