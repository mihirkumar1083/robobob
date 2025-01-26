
package com.robobob.exception;

/**
 * Custom exception for when the master data file cannot be loaded.
 */
public class QuestionsMasterDataNotFoundException extends RuntimeException {
    public QuestionsMasterDataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
