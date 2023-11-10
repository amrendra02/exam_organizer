package com.exam_organizer.candidate_controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/candidate")
public class CandidateHome {

    @GetMapping("/home")
    public String home(){
        return "/candidate/home";
    }
}
