package com.exam_organizer.controller;

import com.exam_organizer.candidate_controller.CandidateHome;
import com.exam_organizer.model.ExamOrganizer;
import com.exam_organizer.repository.ExamOrganizerRepository;
import com.exam_organizer.service.SignupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class Signup {

    @Autowired
    private SignupService signupService;
    private Logger log = LoggerFactory.getLogger(Signup.class);

    @Autowired
    private ExamOrganizerRepository examOrganizerRepository;


    @RequestMapping("/signup")
    public String signup() {
       log.info("from Admin Signup page");
        return "signup";
    }

    @PostMapping("/signup")
    public String signupPost(@ModelAttribute ExamOrganizer user, @RequestParam("imageFile") MultipartFile imageFile, RedirectAttributes redirectAttributes) {

        log.info("Admin Signup controller post...");
//        log.info("{}",user);
        if (imageFile != null && !imageFile.isEmpty()) {
            log.info("proccessing the image file");
            try {
                user.setImage(imageFile.getBytes()); // Convert image file to byte array and set it in the DTO
                log.info("completed");
            } catch (IOException e) {
                log.error("Error processing image file", e);
                redirectAttributes.addFlashAttribute("signup", false);
                return "redirect:/admin/signup";
            }
        }
        // Save user data in database
        String resp = signupService.CreateUser(user);
        if (resp == "success") {
            log.info("{}",resp);
            redirectAttributes.addFlashAttribute("signup",true);
            return "redirect:/admin/login";
        }else{
            log.info("{}",resp);
            redirectAttributes.addFlashAttribute("signup",false);
            return "redirect:/admin/signup";
        }


    }

    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsernameExists(@RequestParam("username") String username) {
        try {
            log.info("Checking the username.");
            Boolean exists = examOrganizerRepository.existsByUsername(username);
            Map<String, Boolean> response = new HashMap<>();
            response.put("exists", exists);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error checking username", ex);
            Map<String, Boolean> errorResponse = new HashMap<>();
            errorResponse.put("exists", false); // Default to false if there's an error
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
