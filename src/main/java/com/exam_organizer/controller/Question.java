package com.exam_organizer.controller;

import com.exam_organizer.model.ExamModel;
import com.exam_organizer.repository.ExamRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
public class Question {


    private final ExamRepository examRepository;
    public Question(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @RequestMapping("/question")
    public  String question(){
        System.out.println("from question...A");
        return "question";
    }


    @PostMapping("/exam-data")
    public ResponseEntity<ExamModel> getExam(@RequestParam("examId") String input) {
        System.out.println("from exam individual exam data...");
//        String value = input.get("value");
        Long id=2L;
        Optional<ExamModel> exam;
        try {
            id = Long.parseLong(input);
            exam = examRepository.findById(id);
            if (exam.isPresent()) {
                return ResponseEntity.ok(exam.get()); // If the data is found, send it with 200 status code
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // If the data is not found, send 404 status code
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // If an error occurs, send a 500 status code
        }
    }


    @PostMapping("/question")
    public ResponseEntity<Long> saveQuestion(@RequestParam("questionText") String question, @RequestParam("image")MultipartFile image){
        Long id= 2L;
        System.out.println("from question save...");
        System.out.println(question);
        System.out.println(image.getOriginalFilename());
        return ResponseEntity.ok(id);
    }
}
