package com.exam_organizer.service;

import com.exam_organizer.model.ExamOrganizer;
import com.exam_organizer.repository.ExamOrganizerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("examOrganizerDetailService")
public class ExamOrganizerDetailService implements UserDetailsService {

    @Autowired
    private ExamOrganizerRepository examOrganizerRepository;
    private Logger log = LoggerFactory.getLogger(ExamOrganizerDetailService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        ExamOrganizer user = examOrganizerRepository.findByUsername(username);
        if (user == null) {
            log.info("user not found!");
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user;
    }
}
