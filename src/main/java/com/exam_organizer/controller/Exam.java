package com.exam_organizer.controller;

import com.exam_organizer.constant.ApiResponse;
import com.exam_organizer.constant.User;
import com.exam_organizer.dto.ExamDto;
import com.exam_organizer.model.ExamModel;
import com.exam_organizer.model.ExamOrganizer;
import com.exam_organizer.payload.Response;
import com.exam_organizer.repository.ExamRepository;
import com.exam_organizer.service.ExamService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
public class Exam {

    @Autowired
    private ExamService examService;


    @Autowired
    private ModelMapper modelMapper;

    private User user = new User();


    private final ExamRepository examRepository;

    public Exam(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    // get exam page
    @RequestMapping(value = "/exam", method = RequestMethod.GET)
    public String exam(Model model) {
        return "exam";
    }


    // get exam list
    @GetMapping("/exam-list")
    public ResponseEntity<?> getExamsList(@RequestParam(defaultValue = "0") int page) {
        System.out.println("from Login get...");
        List<ExamModel> exams = new ArrayList<>();
        List<ExamDto> examR = new ArrayList<>();
        try {
            Page<ExamModel> examPage = examService.examList(page);
            exams = examPage.getContent();
            examR = exams.stream().map((x) -> this.modelMapper.map(x, ExamDto.class)).collect(Collectors.toList());
        } catch (Exception ex) {
            System.out.println("Error retrieving exams: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(examR, HttpStatus.OK);
    }

    // create exam
    @PostMapping("/exam")
    public ResponseEntity<?> examCreate(@ModelAttribute ExamModel exam) {
        System.out.println("from exam create...");

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof ExamOrganizer) {
                ExamOrganizer examOrganizer = (ExamOrganizer) principal;
                Long organizerId = examOrganizer.getOrganizerId();
                System.out.println("Organizer Id: " + organizerId);
                exam.setOrganizer(examOrganizer);
                String resp = examService.CreateExam(exam);
                System.out.println("exam status: " + resp);

            } else {
                System.out.println("Principal is not of type ExamOrganizer");
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            System.out.println("User not authenticated");
        }

//        System.out.println(exam);
//        return "redirect:/admin/exam";
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

}
