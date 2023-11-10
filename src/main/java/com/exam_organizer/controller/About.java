package com.exam_organizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class About {

    @RequestMapping("/about")
    public String about(){
        System.out.println("from about...");
        return "about";
    }

}
