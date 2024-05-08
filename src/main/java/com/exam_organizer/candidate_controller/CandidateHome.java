package com.exam_organizer.candidate_controller;

import com.exam_organizer.candidate_service.CandidateExamList;
import com.exam_organizer.dto.CandidateDto;
import com.exam_organizer.dto.ExamDto;
import com.exam_organizer.model.CandidateModel;
import com.exam_organizer.model.ExamModel;
import com.exam_organizer.model.ExamOrganizer;
import com.fasterxml.jackson.annotation.JsonFormat;
import netscape.javascript.JSObject;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/candidate")
public class CandidateHome {

    private Logger log = LoggerFactory.getLogger(CandidateHome.class);

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CandidateExamList candidateExamList;

    @GetMapping("/home")
    public String home(Model model) {
        log.info("From Candidate Home");
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            log.info("Candidate authenticated");
            Object principal = authentication.getPrincipal();
            if (principal instanceof CandidateModel) {
                CandidateModel candidateModel = (CandidateModel) principal;
                model.addAttribute("name", candidateModel.getCandidateName());
                model.addAttribute("username", candidateModel.getUsername());
                model.addAttribute("email", candidateModel.getEmail());
                model.addAttribute("phone", candidateModel.getPhoneNumber());

                log.info("{}", model);
            } else {
                log.warn("Principle is not type of Candidate");
            }
        } else {
            log.warn("Not authenticated!!");
        }
        return "/candidate/home";
    }
    @GetMapping("/setting")
    public String setting (Model model){
        log.info("Candidate setting page.");
        log.info("From Candidate Home");
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            log.info("Candidate authenticated");
            Object principal = authentication.getPrincipal();
            if (principal instanceof CandidateModel) {
                CandidateModel candidateModel = (CandidateModel) principal;
                model.addAttribute("name", candidateModel.getCandidateName());
                model.addAttribute("username", candidateModel.getUsername());
                model.addAttribute("email", candidateModel.getEmail());
                model.addAttribute("phone", candidateModel.getPhoneNumber());

                log.info("{}", model);
            } else {
                log.warn("Principle is not type of Candidate");
            }
        } else {
            log.warn("Not authenticated!!");
        }
        return "/candidate/setting";
    }
    @GetMapping("/help")
    public String help (Model model){
        log.info("Candidate help page.");
        log.info("From Candidate Home");
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            log.info("Candidate authenticated");
            Object principal = authentication.getPrincipal();
            if (principal instanceof CandidateModel) {
                CandidateModel candidateModel = (CandidateModel) principal;
                model.addAttribute("name", candidateModel.getCandidateName());
                model.addAttribute("username", candidateModel.getUsername());
                model.addAttribute("email", candidateModel.getEmail());
                model.addAttribute("phone", candidateModel.getPhoneNumber());

                log.info("{}", model);
            } else {
                log.warn("Principle is not type of Candidate");
            }
        } else {
            log.warn("Not authenticated!!");
        }
        return "/candidate/help";
    }

    @GetMapping("/exam-test")
    public String examTest (Model model){
        log.info("Candidate test  page.");
        log.info("From Candidate Home");
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            log.info("Candidate authenticated");
            Object principal = authentication.getPrincipal();
            if (principal instanceof CandidateModel) {
                CandidateModel candidateModel = (CandidateModel) principal;
                model.addAttribute("name", candidateModel.getCandidateName());
                model.addAttribute("username", candidateModel.getUsername());
                model.addAttribute("email", candidateModel.getEmail());
                model.addAttribute("phone", candidateModel.getPhoneNumber());

                log.info("{}", model);
            } else {
                log.warn("Principle is not type of Candidate");
            }
        } else {
            log.warn("Not authenticated!!");
        }
        return "/candidate/test";
    }




    @GetMapping("/{username}/exams/page/{page}")
    public ResponseEntity<?> getAllCandidates(@PathVariable String username, @PathVariable int page) {
        log.info("Requesting the exam list ");
        List<ExamModel> res = candidateExamList.getExamList(username, page);
        log.info("Successfully get the exam list");
        List<ExamDto> exam = new ArrayList<>();

        exam = res.stream().map((x) -> this.modelMapper.map(x, ExamDto.class)).collect(Collectors.toList());

        return new ResponseEntity<>(exam, HttpStatus.OK);
    }



}
