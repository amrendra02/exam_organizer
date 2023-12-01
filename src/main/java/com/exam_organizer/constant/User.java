package com.exam_organizer.constant;

import com.exam_organizer.model.ExamOrganizer;
import com.exam_organizer.repository.ExamOrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class User {

    @Autowired
    private ExamOrganizerRepository examOrganizerRepository;

    public String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {

            String name = ((UserDetails) authentication.getPrincipal()).getUsername();
//            System.out.println(name);
            return name;
        }

        return null;
    }
}
