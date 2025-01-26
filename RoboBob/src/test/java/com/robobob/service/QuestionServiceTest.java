package com.robobob.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.robobob.exception.ArithmeticEvaluationException;
import com.robobob.exception.InvalidQuestionException;
import com.robobob.repository.QuestionRepository;

public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository; // Mocking the QuestionRepository

    @InjectMocks
    private QuestionServiceImpl questionService; // The service we're testing

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock predefined questions and their answers
        when(questionRepository.getAnswer("What is your name")).thenReturn("RoboBob");
        when(questionRepository.getAnswer("What do you do")).thenReturn("I help children learn math");
        when(questionRepository.getAnswer("Where are you from")).thenReturn("I am from the RoboWorld");
    }

    @Test
    public void testProcessQuestion_PredefinedAnswer_Name() {
        // Arrange
        String question = "What is your name";

        // Act
        String result = questionService.processQuestion(question);

        // Assert
        assertEquals("RoboBob", result);
    }

    @Test
    public void testProcessQuestion_QuestionNotFound() {
        // Arrange
        String question = "Unknown question";

        // Mock the repository to return null for an unknown question
        when(questionRepository.getAnswer(question.trim())).thenReturn(null);

        // Act and Assert
        InvalidQuestionException exception = assertThrows(InvalidQuestionException.class, () -> {
            questionService.processQuestion(question);
        });

        assertTrue(exception.getMessage().contains("The question 'Unknown question' was not found."));
    }
    
    @Test
    public void testProcessQuestion_ValidArithmeticExpression_Addition() {
        String expression = "2 + 2";
        String expectedResult = "4"; // Expected result

        // Act
        String result = questionService.processQuestion(expression);

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void testProcessQuestion_ValidArithmeticExpression_Multiplication() {
        String expression = "3 * 4";
        String expectedResult = "12"; // Expected result

        // Act
        String result = questionService.processQuestion(expression);

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void testProcessQuestion_ValidArithmeticExpression_Complex() {
        String expression = "5 + 3 * 2";
        String expectedResult = "11"; // Expected result

        // Act
        String result = questionService.processQuestion(expression);

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void testProcessQuestion_InvalidArithmeticExpression() {
        String invalidExpression = "2 + * 3"; // Invalid expression

        ArithmeticEvaluationException exception = assertThrows(ArithmeticEvaluationException.class, () -> {
            questionService.processQuestion(invalidExpression);
        });

        assertTrue(exception.getMessage().contains("Error evaluating the expression"));
    }
}

