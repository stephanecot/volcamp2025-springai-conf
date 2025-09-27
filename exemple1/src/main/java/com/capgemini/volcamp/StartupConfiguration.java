package com.capgemini.volcamp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

// Ajout imports
import com.capgemini.volcamp.model.Warrior;

@Service
@RequiredArgsConstructor
@Slf4j
public class StartupConfiguration implements CommandLineRunner {

    private final ChatClient.Builder chatClientBuilder;

    private String askQuestion(final String question) {

        String prompt = """
            Réponds en français à la question suivante: %s
            """.formatted(question);

        var chatClient = this.chatClientBuilder.build();

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }

    private Warrior generateStructuredWarrior() {
        var chatClient = this.chatClientBuilder.build();

        String prompt = "Génére moi les attributs d'un guerrier. Le guerrier doit avoir: un nom, une force (1-100), une agilité (1-100) et une liste d'armes";

        return chatClient.prompt()
                .user(prompt)
                .call()
                .entity(Warrior.class);

    }

    @Override
    public void run(String... args) {
        // Exemple simple (texte libre)
        var question = """
                Génère moi un guerrier ultra musclé en code ASCII.
 
                Il doit être en position de combat, avec une épée dans une main et un bouclier dans l'autre.
                Il doit avoir deux mains et deux jambes.
            """;
        var response = this.askQuestion(question);

        log.info("Réponse texte libre à '{}': {}", question, response);

        // Exemple structuré (JSON -> POJO)
        Warrior warrior = generateStructuredWarrior();
        log.info("Guerrier structuré: name={}, strength={}, agility={}, weapons={}",
                warrior.getName(), warrior.getStrength(), warrior.getAgility(), warrior.getWeapons());
    }
}
