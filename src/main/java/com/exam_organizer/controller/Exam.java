package com.exam_organizer.controller;

import com.exam_organizer.model.ExamModel;
import com.exam_organizer.model.ExamOrganizer;
import com.exam_organizer.payload.Response;
import com.exam_organizer.repository.ExamRepository;
import com.exam_organizer.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
@RequestMapping("/admin")
public class Exam {

    @Autowired
    private ExamService examService;


    private final ExamRepository examRepository;

    public Exam(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    // get exam page
    @RequestMapping(value = "/exam", method = RequestMethod.GET)
    public String exam(Model model) {
//        System.out.println("from Exam...");
        return "exam";
    }


    // get exam list
    @GetMapping("/exam-list")
    public ResponseEntity<List<ExamModel>> getExamsList(@RequestParam(defaultValue = "0") int page) {
        System.out.println("from Login get...");
        int pageSize = 10; // Set the page size
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("examId").descending());
        List<ExamModel> exams = new ArrayList<>();
        try {
            System.out.println(page);
            Page<ExamModel> examPage = examService.examList(page);
            exams = examPage.getContent();
        } catch (Exception ex) {
            System.out.println("Error retrieving exams: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(exams, HttpStatus.OK);
    }

    // create exam
    @PostMapping("/exam")
    public String examCreate(@ModelAttribute ExamModel exam) {
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
        } else {
            System.out.println("User not authenticated");
        }

//        System.out.println(exam);
        return "redirect:/admin/exam";
    }

    // delete exam

    @DeleteMapping("/exam/{examId}")
    public ResponseEntity<Response> deleteExam(@PathVariable Long examId) {
        System.out.println("from exam delete..."+examId);
        try{
            this.examService.deletExam(examId);
        }catch (Exception ex){
            System.out.println(ex);
        }
        return new ResponseEntity<Response>(new Response("Successfully deleted",true),HttpStatus.OK);
    }




}
