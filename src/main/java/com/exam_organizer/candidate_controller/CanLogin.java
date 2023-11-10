package com.exam_organizer.candidate_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/candidate")
public class CanLogin {

    @GetMapping("/login")
    public String canlogin(){
        System.out.println("from login...Candidate");
        return "candidate/login";
    }

}
