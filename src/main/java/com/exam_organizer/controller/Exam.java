package com.exam_organizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

public class Exam {

    @RequestMapping("/exam")
    public String exam(){
        System.out.println("from Exam...");
        return "exam";
    }

}
