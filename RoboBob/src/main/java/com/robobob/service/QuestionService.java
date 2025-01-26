package com.robobob.service;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
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

    private ScriptEngineManager scriptEngineManager = new ScriptEngineManager();  // Default ScriptEngineManager

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
     * Evaluates the given arithmetic expression using JavaScript engine.
     * 
     * @param expression The arithmetic expression to evaluate.
     * @return The result of the evaluated expression as a string.
     * @throws ArithmeticEvaluationException if the expression is invalid and cannot be evaluated.
     */
    protected String evaluateExpression(String expression) {
        try {
            ScriptEngine engine = scriptEngineManager.getEngineByName("JavaScript");

            // Check if the engine is null
            if (engine == null) {
                throw new ArithmeticEvaluationException("JavaScript engine is not available.");
            }

            return engine.eval(expression).toString();
        } catch (ScriptException e) {
            throw new ArithmeticEvaluationException("Invalid arithmetic expression: " + expression, e);
        }
    }

    // Setter for ScriptEngineManager (useful for testing)
    public void setScriptEngineManager(ScriptEngineManager scriptEngineManager) {
        this.scriptEngineManager = scriptEngineManager;
    }
}
