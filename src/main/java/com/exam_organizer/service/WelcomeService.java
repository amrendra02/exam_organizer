package com.exam_organizer.service;

import com.exam_organizer.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class WelcomeService {

    @Autowired
    private QuestionRepository questionRepository;

    public void store(MultipartFile file) throws Exception{


    }
}
