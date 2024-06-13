package com.exam_organizer.controller;

import com.exam_organizer.constant.ApiResponse;
import com.exam_organizer.constant.User;
import com.exam_organizer.dto.CandidateDto;
import com.exam_organizer.dto.ExamDto;
import com.exam_organizer.model.CandidateModel;
import com.exam_organizer.model.ExamModel;
import com.exam_organizer.model.ExamOrganizer;
import com.exam_organizer.payload.Response;
import com.exam_organizer.repository.ExamRepository;
import com.exam_organizer.service.ExamService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class Exam {

    private final ExamRepository examRepository;
    @Autowired
    private ExamService examService;
    @Autowired
    private ModelMapper modelMapper;
    private User user = new User();
    private Logger log = LoggerFactory.getLogger(Exam.class);
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public Exam(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    // get exam page
    @RequestMapping(value = "/exam", method = RequestMethod.GET)
    public String exam(Model model) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof ExamOrganizer) {
                ExamOrganizer examOrganizer = (ExamOrganizer) principal;
                Long organizerId = examOrganizer.getOrganizerId();
                String name = examOrganizer.getFirstName()+" "+examOrganizer.getLastName();
                log.info("Organizer Id: {}",organizerId);
//                log.info(name);
                int total=0;
                int cancel=0;
                int active=0;
                int live=0;
                try{
                    List<ExamModel> exam = examRepository.findAllExamsByOrganizerId(organizerId);
                    for(ExamModel i: exam){
//                        System.out.println(i.getExamId());
                        total+=1;
                        String s = String.valueOf(i.getStatus());

                        if("ACTIVE" == s){
                            active+=1;
                        }else if("CANCELLED"==s){
                            cancel+=1;
                        }else{
                            live+=1;
                        }

                    }
                    log.info("{}",active);
                    log.info("{}",cancel);
                    log.info("{}",live);
                    log.info("{}",total);
                }catch (Exception ex){
                    System.out.println("error while total count exam: "+ex);
                }


                model.addAttribute("name", name);
                model.addAttribute("id", organizerId);
                model.addAttribute("exam", total);
                model.addAttribute("cancel", cancel);
                model.addAttribute("active", active);
                model.addAttribute("live", live);

            } else {
                log.info("Principal is not of type ExamOrganizer");
            }
        } else {
            log.info("User not authenticated");
        }
        return "exam";
    }

    // get exam list
    @GetMapping("/exam-list")
    public ResponseEntity<?> getExamsList(@RequestParam(defaultValue = "0") int page) {
        log.info("from Login get...");
        List<ExamModel> exams = new ArrayList<>();
        List<ExamDto> examR = new ArrayList<>();
        try {
            Page<ExamModel> examPage = examService.examList(page);
            exams = examPage.getContent();
            examR = exams.stream().map((x) -> this.modelMapper.map(x, ExamDto.class)).collect(Collectors.toList());
        } catch (Exception ex) {
            log.info("Error retrieving exams: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(examR, HttpStatus.OK);
    }

    // create exam
    @PostMapping("/exam")
    public ResponseEntity<?> examCreate(@RequestBody ExamModel exam) {
        System.out.println("from exam create... ");
        HashMap<String, String> map = new HashMap<>();

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof ExamOrganizer) {
                ExamOrganizer examOrganizer = (ExamOrganizer) principal;

                exam.setOrganizer(examOrganizer);
                String resp = examService.CreateExam(exam);
//                System.out.println("exam status: " + resp);

                map.put("Success", "Exam Create");
                return new ResponseEntity<>(map, HttpStatus.OK);
            } else {
                System.out.println("Principal is not of type ExamOrganizer");
            }
        } else {
            System.out.println("User not authenticated");
        }
        map.put("Failed", "Exam Create");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    // delete exam
    @DeleteMapping("/exam/{examId}")
    public ResponseEntity<Response> deleteExam(@PathVariable Long examId) {
        System.out.println("from exam delete..." + examId);
        try {
            this.examService.deletExam(examId);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return new ResponseEntity<Response>(new Response("Successfully deleted", true), HttpStatus.OK);
    }

    // update exam
    @PostMapping(value = "/exam/update/{examId}")
    public ResponseEntity<?> updateExam(@PathVariable long examId, @RequestBody ExamDto examDto) {
        System.out.println("exam update...");
        try {
            System.out.println(examDto);
            this.examService.update(this.user.getUserId(), examId, examDto);
            HashMap<String, String> map = new HashMap<>();
            map.put("success", "update");

            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/exam/{examId}/candidates/{page}")
    public ResponseEntity<?> getAllCandidates(@PathVariable Long examId, @PathVariable int page) {
        log.info("Request to get candidates list: {}", examId);

        log.info("Candidate list request...");
        List<CandidateModel> candidate = new ArrayList<>();
        List<CandidateDto> res = new ArrayList<>();
        try {
//            Page<CandidateModel> examPage
            candidate = examService.candidateList(examId, page);
//            candidate = examPage.getContent();
            res = candidate.stream().map((x) -> this.modelMapper.map(x, CandidateDto.class)).collect(Collectors.toList());

        } catch (Exception ex) {
            log.info("Error retrieving exams: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


}
