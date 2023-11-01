package com.exam_organizer.service;

import com.exam_organizer.model.QuestionModel;
import com.exam_organizer.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<QuestionModel> getQuestionsByExamId(Long examId) {
        return questionRepository.findAllByExamModelExamId(examId);
    }

}
