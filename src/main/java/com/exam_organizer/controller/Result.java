package com.exam_organizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class Result {

    @RequestMapping("/result")
    public String result(){
        System.out.println("from result...");
        return "result";
    }
}
