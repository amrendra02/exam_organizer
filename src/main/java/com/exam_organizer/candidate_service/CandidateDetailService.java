package com.exam_organizer.candidate_service;

import com.exam_organizer.candidate_Repository.CandidateRepository;
import com.exam_organizer.model.CandidateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("candidateDetailService")
public class CandidateDetailService implements UserDetailsService  {



    @Autowired
    private CandidateRepository candidateRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        CandidateModel user = candidateRepository.findByUsername(username);
        if (user == null) {
            System.out.println("Candidate not found!");
            throw new UsernameNotFoundException("Candidate not found with username: " + username);
        }
        return user;

    }
}
