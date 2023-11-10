package com.exam_organizer.candidate_controller;

import com.exam_organizer.candidate_service.CanSignupService;
import com.exam_organizer.model.CandidateModel;
import com.exam_organizer.model.ExamOrganizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/candidate")
public class CanSignup {

    @Autowired
    private CanSignupService signupService;

    @GetMapping("/signup")
    public String signup() {
        System.out.println("from Signup... Candidate");
        return "candidate/signup";
    }

    @PostMapping("/signup")
    public String singupPost(@ModelAttribute CandidateModel user, RedirectAttributes redirectAttributes) {

        System.out.println("Candidate Signup controller...");
        System.out.println(user);
        // Save user data in database
        String resp = signupService.CreateUser(user);
        if (resp == "success") {
            System.out.println(resp);
            redirectAttributes.addFlashAttribute("signup",true);
            return "redirect:/admin/login";
        }else{
            System.out.println(resp);
            redirectAttributes.addFlashAttribute("signup",false);
            return "redirect:/admin/signup";
        }


    }

}
