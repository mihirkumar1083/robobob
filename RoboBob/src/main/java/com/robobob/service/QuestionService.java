package com.robobob.service;

import org.graalvm.polyglot.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.robobob.exception.ArithmeticEvaluationException;
import com.robobob.exception.InvalidQuestionException;
import com.robobob.repository.QuestionRepository;

import java.util.regex.Pattern;

/**
 * Service class for processing questions and performing arithmetic evaluations.
 */
@Service
public class QuestionService {

    // Pattern to identify arithmetic expressions
    private static final Pattern ARITHMETIC_PATTERN = Pattern.compile("^[0-9+\\-*/().\\s]+$");

    // Injected repository for predefined questions
    @Autowired
    protected QuestionRepository questionRepository;

    /**
     * Processes a question by checking if it's predefined or an arithmetic expression.
     * It first checks the predefined questions from the repository. If the question doesn't exist, it checks
     * if the input is an arithmetic expression and evaluates it.
     * 
     * @param question The question to process.
     * @return The answer to the question or the result of the arithmetic expression.
     * @throws InvalidQuestionException if the question is not found in the repository and is not a valid arithmetic expression.
     */
    public String processQuestion(String question) {
        String trimmedQuestion = question.trim();
        String answer = questionRepository.getAnswer(trimmedQuestion);

        // Return predefined answer if found
        if (answer != null) {
            return answer;
        }

        // If it's an arithmetic expression, evaluate it
        if (isArithmeticExpression(trimmedQuestion)) {
            return evaluateExpression(trimmedQuestion);
        }

        // Throw exception if the question is not found
        throw new InvalidQuestionException("The question '" + trimmedQuestion + "' was not found.");
    }

    /**
     * Checks whether the input string is a valid arithmetic expression.
     * 
     * @param input The input string to check.
     * @return true if the input is a valid arithmetic expression, false otherwise.
     */
    protected boolean isArithmeticExpression(String input) {
        return ARITHMETIC_PATTERN.matcher(input).matches();
    }

    /**
     * Evaluates the given arithmetic expression using GraalVM's JavaScript engine.
     * 
     * @param expression The arithmetic expression to evaluate.
     * @return The result of the evaluated expression as a string.
     * @throws ArithmeticEvaluationException if the expression is invalid and cannot be evaluated.
     */
    protected String evaluateExpression(String expression) {
        try (Context context = Context.create()) {
            // Evaluate the expression in JavaScript
            Value result = context.eval("js", expression);

            // Check if the result is null
           if (result.isNull()) {
                throw new ArithmeticEvaluationException("Invalid arithmetic expression: " + expression);
            }

            return result.toString();
        } catch (Exception e) {
            throw new ArithmeticEvaluationException("Error evaluating the expression: " + expression, e);
        }
    }

}
