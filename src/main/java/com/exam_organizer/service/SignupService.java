package com.exam_organizer.service;

import com.exam_organizer.model.ExamOrganizer;
import com.exam_organizer.repository.ExamOrganizerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignupService {

    private Logger log = LoggerFactory.getLogger(SignupService.class);

    private final ExamOrganizerRepository examOrganizerRepository;

    public SignupService(ExamOrganizerRepository examOrganizerRepository) {
        this.examOrganizerRepository = examOrganizerRepository;
    }

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public String CreateUser(ExamOrganizer user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole("ADMIN");
        if (examOrganizerRepository.findByUsername(user.getUsername()) == null) {
            examOrganizerRepository.save(user);
            log.info("Username not found creating new user.");
            return "success";
        } else {
            String res = String.valueOf(examOrganizerRepository.findByUsername(user.getUsername()));
            res = "status: exist : " + res;
            log.info("Username found: {}",user.getUsername());
            return res;
        }
    }
}
