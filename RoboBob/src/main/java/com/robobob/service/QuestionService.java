package com.robobob.service;

public interface QuestionService {
    /**
     * Processes a question by checking if it's predefined or an arithmetic expression.
     * 
     * @param question The question to process.
     * @return The answer to the question or the result of the arithmetic expression.
     * @throws InvalidQuestionException if the question is not found in the repository and is not a valid arithmetic expression.
     */
    String processQuestion(String question);
}
