package com.exam_organizer.candidate_controller;

import com.exam_organizer.candidate_Repository.CandidateRepository;
import com.exam_organizer.candidate_service.CanSignupService;
import com.exam_organizer.dto.CandidateDto;
import com.exam_organizer.model.CandidateModel;
import com.exam_organizer.model.ExamOrganizer;
import org.modelmapper.ModelMapper;
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
@RequestMapping("/candidate")
public class CanSignup {

    @Autowired
    private CanSignupService signupService;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ModelMapper modelMapper;

    private Logger log = LoggerFactory.getLogger(CanSignup.class);
    @GetMapping("/signup")
    public String signup() {
        log.info("from Signup... Candidate");
        return "candidate/signup";
    }

    @PostMapping("/signup")
    public String singupPost(@ModelAttribute CandidateDto req, @RequestParam("imageFile") MultipartFile imageFile, RedirectAttributes redirectAttributes) {

        log.info("Candidate Signup controller...");
        log.debug("{}",req);

        if (imageFile != null && !imageFile.isEmpty()) {
            log.info("proccessing the image file");
            try {
                req.setImage(imageFile.getBytes()); // Convert image file to byte array and set it in the DTO
                log.info("completed");
            } catch (IOException e) {
                log.error("Error processing image file", e);
                redirectAttributes.addFlashAttribute("signup", false);
                return "redirect:/candidate/signup";
            }
        }

        CandidateModel user = modelMapper.map(req,CandidateModel.class);

        // Save user data in database
        String resp = signupService.CreateUser(user);
        if (resp == "success") {
            log.debug("{}",resp);
            redirectAttributes.addFlashAttribute("signup",true);
            return "redirect:/candidate/login";
        }else{
            log.info("{}",resp);
            redirectAttributes.addFlashAttribute("signup",false);
            return "redirect:/candidate/signup";
        }


    }

    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsernameExists(@RequestParam("username") String username) {
        try {
            log.info("Candidate Checking the username.");
            Boolean exists = candidateRepository.existsByUsername(username);
            Map<String, Boolean> response = new HashMap<>();
            response.put("exists", exists);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Candidate Error checking username", ex);
            Map<String, Boolean> errorResponse = new HashMap<>();
            errorResponse.put("exists", false); // Default to false if there's an error
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
