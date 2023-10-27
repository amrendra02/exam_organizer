package com.exam_organizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForgetPassword {
    @RequestMapping("/forgetpassword")
    public String forgetpassword(){
        System.out.println("from forget password");
        return "forgetpassword";
    }
}
