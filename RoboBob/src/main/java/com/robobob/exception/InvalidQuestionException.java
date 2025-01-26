
package com.robobob.exception;

/**
 * Custom exception for invalid questions.
 */
public class InvalidQuestionException extends RuntimeException {
    public InvalidQuestionException(String message) {
        super(message);
    }
}
