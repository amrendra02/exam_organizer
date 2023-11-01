package com.exam_organizer.controller;

import com.exam_organizer.model.ExamModel;
import com.exam_organizer.model.OptionModel;
import com.exam_organizer.model.QuestionModel;
import com.exam_organizer.repository.ExamRepository;
import com.exam_organizer.repository.OptionRepository;
import com.exam_organizer.repository.QuestionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class Question {


    private final ObjectMapper objectMapper;

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;

    private final OptionRepository optionRepository;

    public Question(ObjectMapper objectMapper, ExamRepository examRepository, QuestionRepository questionRepository, OptionRepository optionRepository) {
        this.objectMapper = objectMapper;
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
    }

    @RequestMapping("/question")
    public String question() {
        System.out.println("from question...A");
        return "question";
    }


    //individually get exam data....
    @PostMapping("/exam-data")
    public ResponseEntity<ExamModel> getExam(@RequestParam("examId") String input) {
        System.out.println("from exam individual exam data...");
//        String value = input.get("value");
        Long id = 2L;
        Optional<ExamModel> exam;
        try {
            id = Long.parseLong(input);
            exam = examRepository.findById(id);
            ExamModel ex = exam.get();
            ex.setOrganizer(null);

            if (exam.isPresent()) {
                return ResponseEntity.ok(exam.get()); // If the data is found, send it with 200 status code
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // If the data is not found, send 404 status code
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // If an error occurs, send a 500 status code
        }
    }

    // Question add--------------
    @PostMapping("/question")
    public ResponseEntity<Long> saveQuestion(@RequestParam("questionText") String question,
                                             @RequestParam("image") MultipartFile image,
                                             @RequestParam("examId") String examId,
                                             @RequestParam("correctOption") String correctOption) {

        Long exId = Long.valueOf(examId);
        System.out.println("from question save...");
//        System.out.println(question);
//        System.out.println(image.getOriginalFilename());

        try {
            if (question.length() == 0 && image.isEmpty()) {
                // No Content
                System.out.println("No content found to sav!...");
                return ResponseEntity.noContent().build();
            }
            QuestionModel q = new QuestionModel(); // Create a new instance of QuestionModel
            q.setQuestionText(question); // Set the question text
            q.setCorrectOption(correctOption);//-----------------

            System.out.println(correctOption);
            if (!image.isEmpty()) {
                q.setImage(image.getBytes()); // Set the image bytes
            }

            Optional<ExamModel> ex = examRepository.findById(exId);
            if (ex.isPresent()) {
                ExamModel examModel = ex.get();
                q.setExamModel(examModel);
                QuestionModel qt = questionRepository.save(q);
                // OK
                System.out.println("Question Saved...");
                return ResponseEntity.ok(qt.getQuestionId());
            } else {
                // Not Found
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            // Internal Server Error
            System.out.println("Error While Saving in dataBase!...");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //------Option add----------------

    @PostMapping("/option-add")
    public ResponseEntity<String> handleOptionAdd(@RequestParam("options") String options, @RequestParam("qId") String id) {

        Long qId = Long.valueOf(id);

        try {
            List<Map<String, String>> optionList = objectMapper.readValue(options, List.class);

            System.out.println(id);

            QuestionModel qt = questionRepository.findByQuestionId(Math.toIntExact(qId));
            List<OptionModel> op = new ArrayList<>();

            for (Map<String, String> option : optionList) {
                System.out.println("Received Option: " + option.toString());
                System.out.println("Received Option: " + option.get("number"));
                OptionModel optemp = new OptionModel();
                optemp.setNumber(option.get("number"));
                optemp.setText(option.get("text"));
                optemp.setQuestionModel(qt);
                optionRepository.save(optemp);
                op.add(optemp);
            }
            qt.setOptionModels(op);
            // Return a response if required
            // System.out.println(qt.getOptionModels());
            String response = "{\"status\": \"success\"}";
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("failed to save options");
            e.printStackTrace();
            return ResponseEntity.ok("failed");
        }
    }


    @GetMapping("/question-list")
    public ResponseEntity<String> getQuestionList(@RequestParam("eaxnId") String id,Model model){

        System.out.println("from question list exam id:"+id);

        return ResponseEntity.ok("ok");
    }

}
