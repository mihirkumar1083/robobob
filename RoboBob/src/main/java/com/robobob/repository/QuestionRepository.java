package com.robobob.repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.robobob.exception.QuestionsMasterDataNotFoundException;

@Repository
public class QuestionRepository {

    private static final String FILE_PATH = "/questions.txt";  // Classpath path
    private final Map<String, String> questions = new HashMap<>();

    public QuestionRepository() {
        loadQuestions();
    }

    public String getAnswer(String question) {
        return questions.get(question.toLowerCase());
    }

    protected void loadQuestions() {
        InputStream inputStream = null;
        try {
            inputStream = getClass().getResourceAsStream(FILE_PATH);
            if (inputStream == null) {
                throw new QuestionsMasterDataNotFoundException("Could not find questions file in classpath.");
            }
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            String[] lines = content.split("\n");
            for (String line : lines) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    questions.put(parts[0].trim().toLowerCase(), parts[1].trim());
                }
            }
        } catch (IOException ex) {
            throw new QuestionsMasterDataNotFoundException("Could not load questions master data", ex);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    // Log the exception if needed, but ignore it for now as we're closing
                }
            }
        }
    }

    public Map<String, String> getQuestions() {
        return questions;
    }
}
