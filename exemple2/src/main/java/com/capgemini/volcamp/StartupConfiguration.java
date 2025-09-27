package com.capgemini.volcamp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StartupConfiguration implements CommandLineRunner {

    private final ChatClient.Builder chatClientBuilder;

    private final DataSource dataSource;

    private final VectorStore vectorStore;

    private void askQuestion(final String question) {
        ChatResponse response = chatClientBuilder.build().prompt()
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .user(question)
                .call()
                .chatResponse();

        log.info("Response: {}", response);
    }

    private void insertDocument() {
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(new ClassPathResource("30-recettes-preferees-des-francais.pdf"));
        List<Document> docbatch = pdfReader.read();

        // Clear old data
        var jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute("TRUNCATE public.vector_store");

        docbatch = new TokenTextSplitter().apply(docbatch);
        vectorStore.add(docbatch);
    }

    private List<Document> similaritySearchExample(String question, int topk) {
        return this.vectorStore.similaritySearch(SearchRequest.builder().query(question).topK(topk).build());
    }

    @Override
    public void run(String... args) {
        this.insertDocument();

        var question = """
            De quels ingredients j'ai besoin pour un Aligot de l’Aubrac ?

            Combien de temps faut-il pour le préparer ?
        """;

        var docs = this.similaritySearchExample(question, 5);

        docs.forEach(doc -> log.info("Doc: {}", doc.getText()));

        this.askQuestion(question);
    }
}
