
package com.robobob.exception;

/**
 * Custom exception for arithmetic evaluation errors.
 */
public class ArithmeticEvaluationException extends RuntimeException {
    public ArithmeticEvaluationException(String message, Throwable cause) {
        super(message, cause);
    }
    public ArithmeticEvaluationException(String message) {
        super(message);
    }
}
