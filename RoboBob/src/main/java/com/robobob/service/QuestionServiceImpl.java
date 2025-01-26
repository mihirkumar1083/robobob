package com.robobob.service;

import java.util.regex.Pattern;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.robobob.exception.ArithmeticEvaluationException;
import com.robobob.exception.InvalidQuestionException;
import com.robobob.repository.QuestionRepository;

/**
 * Service class for processing questions and performing arithmetic evaluations.
 */
@Service
public class QuestionServiceImpl implements QuestionService {

    // Logger for logging important events
    private static final Logger logger = LoggerFactory.getLogger(QuestionServiceImpl.class);

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
    @Override
    public String processQuestion(String question) {
        String trimmedQuestion = question.trim();
        logger.info("Processing question: '{}'", trimmedQuestion);

        String answer = questionRepository.getAnswer(trimmedQuestion);

        // Return predefined answer if found
        if (answer != null) {
            logger.info("Found predefined answer: '{}'", answer);
            return answer;
        }

        // If it's an arithmetic expression, evaluate it
        if (isArithmeticExpression(trimmedQuestion)) {
            logger.info("Detected arithmetic expression: '{}'", trimmedQuestion);
            return evaluateExpression(trimmedQuestion);
        }

        // Throw exception if the question is not found
        logger.warn("The question '{}' was not found in the repository.", trimmedQuestion);
        throw new InvalidQuestionException("The question '" + trimmedQuestion + "' was not found.");
    }

    /**
     * Checks whether the input string is a valid arithmetic expression.
     * 
     * @param input The input string to check.
     * @return true if the input is a valid arithmetic expression, false otherwise.
     */
    protected boolean isArithmeticExpression(String input) {
        boolean isValid = ARITHMETIC_PATTERN.matcher(input).matches();
        logger.debug("Is '{}' a valid arithmetic expression? {}", input, isValid);
        return isValid;
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
            logger.info("Evaluating arithmetic expression: '{}'", expression);
            // Evaluate the expression in JavaScript
            Value result = context.eval("js", expression);

            // Check if the result is null
            if (result.isNull()) {
                logger.error("Evaluation returned null for expression: '{}'", expression);
                throw new ArithmeticEvaluationException("Invalid arithmetic expression: " + expression);
            }

            String resultString = result.toString();
            logger.info("Arithmetic evaluation result: '{}'", resultString);
            return resultString;
        } catch (Exception e) {
            logger.error("Error evaluating the expression: '{}'", expression, e);
            throw new ArithmeticEvaluationException("Error evaluating the expression: " + expression, e);
        }
    }
}
