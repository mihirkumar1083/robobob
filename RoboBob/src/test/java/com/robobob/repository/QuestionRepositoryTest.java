package com.robobob.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class QuestionRepositoryTest {

    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        // Create a new instance of the repository before each test
        questionRepository = new QuestionRepository();
    }

    @Test
    void testGetAnswer_ValidQuestion1() {
        // Assuming the question "What is your name?" is present in the questions.txt
        String answer = questionRepository.getAnswer("What is your name");
        assertNotNull(answer, "The answer should not be null.");
        assertEquals("RoboBob", answer, "The answer should be 'RoboBob'.");
    }

    @Test
    void testGetAnswer_ValidQuestion2() {
        // Assuming the question "What do you do?" is present in the questions.txt
        String answer = questionRepository.getAnswer("What do you do");
        assertNotNull(answer, "The answer should not be null.");
        assertEquals("I help children learn math", answer, "The answer should be 'I help children learn math'.");
    }

    @Test
    void testGetAnswer_InvalidQuestion() {
        // Testing a question that is not present in the questions.txt
        String answer = questionRepository.getAnswer("How old are you");
        assertNull(answer, "The answer should be null for an unknown question.");
    }

   
}
