package com.exam_organizer.candidate_service;

import com.exam_organizer.candidate_Repository.CandidateRepository;
import com.exam_organizer.model.CandidateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CanSignupService {

    private final CandidateRepository candidateRepository;

    public CanSignupService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }



    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public String CreateUser(CandidateModel user){

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if (candidateRepository.findByUsername(user.getUsername()) == null) {
            candidateRepository.save(user);
            return "success";
        } else {
            String res = String.valueOf(candidateRepository.findByUsername(user.getUsername()));
            res = "status: exist : " + res;
            return res;
        }
    }
}
