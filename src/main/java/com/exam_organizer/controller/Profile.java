package com.exam_organizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Profile {
    @RequestMapping("/profile")
    public String profile(){
        System.out.println("from profile...");
        return "profile";
    }
}
