package com.capgemini.volcamp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

// Ajout imports
import com.capgemini.volcamp.model.Warrior;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class StartupConfiguration implements CommandLineRunner {

    private final ChatClient.Builder chatClientBuilder;

    public static class ExempleTools {

        @Tool(description = "Simulate the role of 2 dices and return their sum")
        public int rollDices(@ToolParam(description = "Number of dices to use") int nbDices) {
            int score = 0;
            for (int i = 1; i <= nbDices; i++) {
                int dice = (int) (Math.random() * 6) + 1;
                score += dice;
            }

            log.info("TOOLS: With {} dices score is " + score, nbDices);

            return score;
        }
    }

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

        String prompt = """
            Génére moi les attributs d'un guerrier.
            Le guerrier doit avoir: un nom, une force, une agilité et une liste d'armes.

            Utilise un lancé de deux dés pour déterminer la force et l'agilité du guerrier.
        """;

        return chatClient.prompt()
                .user(prompt)
                .tools(new ExempleTools())
                .call()
                .entity(Warrior.class); // Structured output

    }

    @Override
    public void run(String... args) {
        var question = """
                Génère moi un guerrier ultra musclé en code ASCII.
 
                Il doit être en position de combat, avec une épée dans une main et un bouclier dans l'autre.
                Il doit avoir deux mains et deux jambes.
            """;
        var response = this.askQuestion(question);

        log.info("Réponse texte libre à '{}': {}", question, response);

        Warrior warrior = generateStructuredWarrior();

        log.info("Guerrier structuré: name={}, strength={}, agility={}, weapons={}",
                warrior.getName(), warrior.getStrength(), warrior.getAgility(), warrior.getWeapons());
    }
}
