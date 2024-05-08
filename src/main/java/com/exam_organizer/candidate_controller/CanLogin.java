package com.exam_organizer.candidate_controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/candidate")
public class CanLogin {

    private Logger log = LoggerFactory.getLogger(CanLogin.class);
    @GetMapping("/login")
    public String canlogin(){
        log.info("from login...Candidate");
        return "candidate/login";
    }

}
