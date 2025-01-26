
package com.robobob.repository;

import com.robobob.exception.QuestionsMasterDataNotFoundException;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Repository for managing predefined questions and answers.
 */
@Repository
public class QuestionRepository {

    private static final String FILE_PATH = "src/main/resources/questions.txt";
    private final Map<String, String> questions = new HashMap<>();

    public QuestionRepository() {
        loadQuestions();
    }

    public String getAnswer(String question) {
        return questions.get(question.toLowerCase());
    }

    protected void loadQuestions() {
        try {
            Files.lines(Paths.get(FILE_PATH)).forEach(line -> {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    questions.put(parts[0].trim().toLowerCase(), parts[1].trim());
                }
            });
        } catch (IOException ex) {
            throw new QuestionsMasterDataNotFoundException("Could not load questions master data", ex);
        }
    }

	public Map<String, String> getQuestions() {
		return questions;
	}
}
