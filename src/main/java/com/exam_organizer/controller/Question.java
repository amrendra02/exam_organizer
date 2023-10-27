package com.exam_organizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Question {

    @RequestMapping("/question")
    public  String question(){
        System.out.println("from question...");
        return "question";
    }
}
