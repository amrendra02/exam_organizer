package com.exam_organizer.controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Login {

    @RequestMapping("/login")
    public String login(){
        System.out.println("from login...");
        return "login";
    }

    @PostMapping("/login")
    public String lgoinPost(){

        System.out.println("from post Login...");
        return "";
    }
    @GetMapping("/logout")
    public String logout(){
        return "logout";
    }

    @RequestMapping("/failed")
    public String failed(){
        System.out.println("from failed...");
        return "failed";
    }
}
