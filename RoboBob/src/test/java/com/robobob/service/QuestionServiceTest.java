package com.robobob.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.robobob.exception.ArithmeticEvaluationException;
import com.robobob.repository.QuestionRepository;

/**
 * Unit tests for the QuestionService class, ensuring proper handling of
 * arithmetic expressions and related exceptions.
 */
@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ScriptEngine mockScriptEngine;

    /**
     * Tests the processQuestion method for a valid arithmetic expression.
     * 
     * @throws ScriptException if there is an issue with evaluating the expression
     */
    @Test
    void testProcessQuestion_ValidArithmeticExpression() throws ScriptException {
        // Arrange
        String expression = "2 + 3";
        String expected = "5";

        // Mock ScriptEngine behavior to return expected result
        when(mockScriptEngine.eval(expression)).thenReturn(5);

        // Mock the ScriptEngineManager to return our mockScriptEngine
        ScriptEngineManager mockEngineManager = mock(ScriptEngineManager.class);
        when(mockEngineManager.getEngineByName("JavaScript")).thenReturn(mockScriptEngine);

        // Inject the mockScriptEngine into the service
        questionService.setScriptEngineManager(mockEngineManager);

        // Act
        String result = questionService.processQuestion(expression);

        // Assert
        assertEquals(expected, result);
    }

    /**
     * Tests the processQuestion method for an invalid arithmetic expression
     * (e.g., division by zero).
     * 
     * @throws ScriptException if there is an issue with evaluating the expression
     */
    @Test
    void testProcessQuestion_InvalidArithmeticExpression() throws ScriptException {
        // Arrange
        String invalidExpression = "2 / 0";  // Division by zero

        // Mock the ScriptEngine to throw an exception for division by zero
        when(mockScriptEngine.eval(invalidExpression)).thenThrow(new ScriptException("Division by zero"));

        // Mock the ScriptEngineManager to return our mockScriptEngine
        ScriptEngineManager mockEngineManager = mock(ScriptEngineManager.class);
        when(mockEngineManager.getEngineByName("JavaScript")).thenReturn(mockScriptEngine);

        // Inject the mockScriptEngine into the service
        questionService.setScriptEngineManager(mockEngineManager);

        // Act & Assert
        ArithmeticEvaluationException exception = assertThrows(ArithmeticEvaluationException.class, () -> {
            questionService.processQuestion(invalidExpression);
        });

        // Assert that the exception message matches the expected output
        assertEquals("Invalid arithmetic expression: 2 / 0", exception.getMessage());
    }

    /**
     * Tests the processQuestion method when the JavaScript engine is not available.
     * This simulates a situation where the ScriptEngineManager does not provide a valid engine.
     */
    @Test
    void testProcessQuestion_JavaScriptEngineNotAvailable() {
        // Arrange
        String expression = "2 + 3";

        // Mock ScriptEngineManager to return null (simulate engine not found)
        ScriptEngineManager mockEngineManager = mock(ScriptEngineManager.class);
        when(mockEngineManager.getEngineByName("JavaScript")).thenReturn(null);
        questionService.setScriptEngineManager(mockEngineManager);

        // Act & Assert
        ArithmeticEvaluationException exception = assertThrows(ArithmeticEvaluationException.class, () -> {
            questionService.processQuestion(expression);
        });
        assertEquals("JavaScript engine is not available.", exception.getMessage());
    }
}
