package com.robobob.repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.robobob.exception.QuestionsMasterDataNotFoundException;

@Repository
public class QuestionRepository {

    private static final String FILE_PATH = "/questions.txt";  // Classpath path
    private final Map<String, String> questions = new HashMap<>();

    // Logger for logging important events
    private static final Logger logger = LoggerFactory.getLogger(QuestionRepository.class);

    public QuestionRepository() {
        logger.info("Initializing QuestionRepository...");
        loadQuestions();
    }

    public String getAnswer(String question) {
        logger.debug("Getting answer for question: '{}'", question);
        return questions.get(question.toLowerCase());
    }

    protected void loadQuestions() {
        logger.info("Loading questions from file: '{}'", FILE_PATH);
        InputStream inputStream = null;
        try {
            inputStream = getClass().getResourceAsStream(FILE_PATH);
            if (inputStream == null) {
                logger.error("Questions file not found at path: '{}'", FILE_PATH);
                throw new QuestionsMasterDataNotFoundException("Could not find questions file in classpath.");
            }
            
            String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            String[] lines = content.split("\n");
            for (String line : lines) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String question = parts[0].trim().toLowerCase();
                    String answer = parts[1].trim();
                    questions.put(question, answer);
                    logger.debug("Loaded question-answer pair: '{}' = '{}'", question, answer);
                } else {
                    logger.warn("Skipping invalid line (not in key=value format): '{}'", line);
                }
            }
            logger.info("Successfully loaded {} question(s).", questions.size());
        } catch (IOException ex) {
            logger.error("Error reading the questions file '{}'", FILE_PATH, ex);
            throw new QuestionsMasterDataNotFoundException("Could not load questions master data", ex);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                    logger.debug("Closed input stream for file: '{}'", FILE_PATH);
                } catch (IOException ex) {
                    logger.warn("Error closing input stream for file: '{}'", FILE_PATH, ex);
                }
            }
        }
    }

    public Map<String, String> getQuestions() {
        logger.debug("Returning all loaded questions. Total: {}", questions.size());
        return questions;
    }
}
