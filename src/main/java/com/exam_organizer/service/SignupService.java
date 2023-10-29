package com.exam_organizer.service;

import com.exam_organizer.model.ExamOrganizer;
import com.exam_organizer.repository.ExamOrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignupService {


    private final ExamOrganizerRepository examOrganizerRepository;

    public SignupService(ExamOrganizerRepository examOrganizerRepository) {
        this.examOrganizerRepository = examOrganizerRepository;
    }

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public String CreateUser(ExamOrganizer user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (examOrganizerRepository.findByUsername(user.getUsername()) == null) {
            examOrganizerRepository.save(user);
            return "success";
        } else {
            String res = String.valueOf(examOrganizerRepository.findByUsername(user.getUsername()));
            res = "status: exist : " + res;
            return res;
        }
    }
}
