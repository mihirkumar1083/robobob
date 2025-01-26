
package com.robobob.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for handling custom exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ArithmeticEvaluationException.class)
    public ResponseEntity<String> handleArithmeticEvaluationException(ArithmeticEvaluationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidQuestionException.class)
    public ResponseEntity<String> handleInvalidQuestionException(InvalidQuestionException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(QuestionsMasterDataNotFoundException.class)
    public ResponseEntity<String> handleQuestionsMasterDataNotFoundException(QuestionsMasterDataNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
