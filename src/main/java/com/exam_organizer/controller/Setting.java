package com.exam_organizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class Setting {

    @RequestMapping("/setting")
    public String setting(){

        return "setting";
    }
}
