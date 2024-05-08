package com.exam_organizer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForgetPassword {

    private Logger log = LoggerFactory.getLogger(ForgetPassword.class);
    @RequestMapping("/forgetpassword")
    public String forgetpassword(){
       log.info("Forget Password page.");
        return "forgetpassword";
    }
}
