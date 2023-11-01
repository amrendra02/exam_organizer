package com.exam_organizer.controller;

import com.exam_organizer.model.QuestionModel;
import com.exam_organizer.repository.QuestionRepository;
import com.exam_organizer.service.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiController {
    private final ObjectMapper objectMapper;

    private final QuestionRepository questionRepository;

    private final QuestionService questionService;

    public ApiController(ObjectMapper objectMapper, QuestionRepository questionRepository, QuestionService questionService) {
        this.objectMapper = objectMapper;
        this.questionRepository = questionRepository;
        this.questionService = questionService;
    }


    @GetMapping(value = "/question-list/{examId}")
    public String getQuestionsByExamId(@PathVariable Long examId, Model model) {
        System.out.println("from question list... : "+examId);
        try {
            System.out.println("geting from database..");
            List<QuestionModel> questions = questionService.getQuestionsByExamId(examId);
            model.addAttribute("questions", questions);
            for(QuestionModel q:questions)
            {
                System.out.println(q.getQuestionText());
            }
            return "question-list";
        } catch (Exception ex) {
            System.out.println("failed to gget all question");
        }
        return "failed";
    }


}
