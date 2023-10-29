package com.exam_organizer.controller;

import com.exam_organizer.model.ExamOrganizer;
import com.exam_organizer.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class Signup {

    @Autowired
    private SignupService signupService;

    @RequestMapping("/signup")
    public String signup() {
        System.out.println("from Singup...");
        return "signup";
    }

    @PostMapping("/signup")
    public String singupPost(@ModelAttribute ExamOrganizer user, RedirectAttributes redirectAttributes) {

        System.out.println("Signup controller post...");
        System.out.println(user);
        // Save user data in database
        String resp = signupService.CreateUser(user);
        if (resp == "success") {
            System.out.println(resp);
            redirectAttributes.addFlashAttribute("signup",true);
            return "redirect:/login";
        }else{
            System.out.println(resp);
            redirectAttributes.addFlashAttribute("signup",false);
            return "redirect:/signup";
        }


    }
}
