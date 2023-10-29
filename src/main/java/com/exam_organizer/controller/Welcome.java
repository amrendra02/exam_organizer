package com.exam_organizer.controller;

import com.exam_organizer.model.ExamModel;
import com.exam_organizer.repository.ExamRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class Welcome {

    private final ExamRepository examRepository;

    public Welcome(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

//    @RequestMapping("/")
//    public String welcome(){
//        System.out.println("from welcome");
//        return "welcome";
//    }

    private static final int PAGE_SIZE = 10; // Number of items per page
    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String showAllExams(Model model) {
        System.out.println("from welcome...");
        int pageNumber = 0; // Default page number
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("examId").descending());
        List<ExamModel> exams = examRepository.findAll(pageable).getContent();
        model.addAttribute("exams", exams);
        return "welcome"; // The name of the Thymeleaf template to render the list of exams
    }
}
