
package com.robobob.controller;

import com.robobob.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling user questions.
 */
@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * Handles user-submitted questions and provides responses.
     *
     * @param question the question submitted by the user.
     * @return the response to the question.
     */
    @PostMapping
    public String handleQuestion(@RequestBody String question) {
        return questionService.processQuestion(question);
    }
}
