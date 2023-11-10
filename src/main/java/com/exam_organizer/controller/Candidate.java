package com.exam_organizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class Candidate {

    @RequestMapping("/candidate")
    public String candidate(){
        System.out.println("from candidate...");
        return "candidate";
    }
}
