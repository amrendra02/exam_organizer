package com.exam_organizer.controller;

import com.exam_organizer.model.ExamModel;
import com.exam_organizer.model.ExamOrganizer;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class Login {

    @RequestMapping("/login")
    public String login(){
        System.out.println("from login...A");
//        System.out.println(user);
//        System.out.println("p: "+password);
        return "login";
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
