package com.exam_organizer.controller;

import com.exam_organizer.model.QuestionModel;
import com.exam_organizer.repository.ExamRepository;
import com.exam_organizer.repository.QuestionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;

@Controller
public class Welcome {

    private final ExamRepository examRepository;

    private  final QuestionRepository questionRepository;
    public Welcome(ExamRepository examRepository, QuestionRepository questionRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
    }

    @RequestMapping("/")
    public String welcome(){
        System.out.println("from welcome");
        return "welcome";
    }

//    Don't Delete both are correct
    /*@PostMapping("/upload")
    public String imageUpload(@RequestParam("image") MultipartFile img,@RequestParam("questionText") String name){
        System.out.println("Form upload img...");
        QuestionModel qt = new QuestionModel();
        try{
            qt.setImage(img.getBytes());
            qt.setQuestionText(name);
            questionRepository.save(qt);
            System.out.println("success: "+name);
            System.out.println(img.getOriginalFilename());
        }catch (Exception ex){
            System.out.println("error save in database: "+ex);
            System.out.println(name);
        }

        return"redirect:/";
    }*/

    /*@GetMapping("/display")
    public ResponseEntity<String> displayImage(Model model) {
        QuestionModel qt = questionRepository.findByQuestionId(1); // Change the ID according to your database
        if (qt!=null) {
            String base64Encoded = Base64.getEncoder().encodeToString(qt.getImage());
            model.addAttribute("image", base64Encoded);
            return ResponseEntity.ok(base64Encoded);
        }
       return ResponseEntity.notFound().build();
    }*/
    @PostMapping("/upload")
    public ResponseEntity<String> handleOptionsUpload(@RequestParam("options") List<String> options) {
        System.out.println("Received Options:"+options);
        for (String option : options) {
            System.out.println(option);
        }
        return ResponseEntity.ok("ok");
    }

}
