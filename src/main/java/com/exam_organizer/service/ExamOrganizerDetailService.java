package com.exam_organizer.service;

import com.exam_organizer.model.ExamOrganizer;
import com.exam_organizer.repository.ExamOrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ExamOrganizerDetailService implements UserDetailsService {

    @Autowired
    private ExamOrganizerRepository examOrganizerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ExamOrganizer user =  examOrganizerRepository.findByUsername(username);
        if(user==null)
        {
            System.out.println("user not found!");
            throw new UsernameNotFoundException("User not found with username: "+username);
        }
        return user;
    }
}
