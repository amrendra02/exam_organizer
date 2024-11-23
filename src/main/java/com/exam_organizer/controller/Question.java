package com.exam_organizer.controller;

import com.exam_organizer.model.ExamModel;
import com.exam_organizer.model.OptionModel;
import com.exam_organizer.model.QuestionModel;
import com.exam_organizer.repository.ExamRepository;
import com.exam_organizer.repository.OptionRepository;
import com.exam_organizer.repository.QuestionRepository;
import com.exam_organizer.service.ExamService;
import com.exam_organizer.service.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class Question {

    private final ObjectMapper objectMapper;

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;

    private final OptionRepository optionRepository;

    private final QuestionService questionService;

    private final ExamService examService;

    private Logger log = LoggerFactory.getLogger(Question.class);


    public Question(ObjectMapper objectMapper, ExamRepository examRepository, QuestionRepository questionRepository, OptionRepository optionRepository, QuestionService questionService, ExamService examService) {
        this.objectMapper = objectMapper;
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.questionService = questionService;
        this.examService = examService;
    }

    @RequestMapping("/question")
    public String question() {
        return "question";
    }


    //individually get exam data....
    @PostMapping("/exam-data")
    public ResponseEntity<ExamModel> getExam(@RequestParam("examId") String input) {
        try {
            Optional<ExamModel> exam;
            Long id = Long.parseLong(input);
            exam = examService.examData(id);
            ExamModel ex = exam.get();
            log.info("...2  {}", ex);

            ex.setOrganizer(null);

            if (exam.isPresent()) {
                return ResponseEntity.ok(exam.get());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // If the data is not found, send 404 status code
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // If an error occurs, send a 500 status code
        }
    }

    // Question add--------------
    @PostMapping("/question")
    public ResponseEntity<?> saveQuestion(@RequestParam("questionText") String question,
                                             @RequestParam("image") MultipartFile image,
                                             @RequestParam("examId") String examId,
                                             @RequestParam("correctOption") String correctOption,
                                             @RequestParam("options") String options) {

        Long exId = Long.valueOf(examId);
        log.info("from question save...");
        log.info(options);
//        log.info(image.getOriginalFilename());

        try {
            if (question.length() == 0 && image.isEmpty()) {
                // No Content
                log.info("No content found to sav!...");
                return ResponseEntity.noContent().build();
            }
            QuestionModel q = new QuestionModel(); // Create a new instance of QuestionModel
            q.setQuestionText(question); // Set the question text
            q.setCorrectOption(correctOption);//-----------------

            log.info(correctOption);
            if (!image.isEmpty()) {
                q.setImage(image.getBytes()); // Set the image bytes
            }

            Optional<ExamModel> ex = examRepository.findById(exId);
            if (ex.isPresent()) {
                ExamModel examModel = ex.get();
                q.setExamModel(examModel);
                QuestionModel qt = questionRepository.save(q);
                // OK
                log.info("Question Saved...",qt.getQuestionId());

                String res = handleOptionAdd(options,qt.getQuestionId(),exId);
                log.info(res);
                return new ResponseEntity<>(Map.of("question","saved","options",res.toString()), HttpStatus.OK);
//                return ResponseEntity.ok(qt.getQuestionId());
            } else {
                // Not Found
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            // Internal Server Error
            log.info("Error While Saving in dataBase!...");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //------Option add----------------

//    @PostMapping("/option-add")
//    public ResponseEntity<String> handleOptionAdd(@RequestParam("options") String options, @RequestParam("qId") String id, @RequestParam("eId") String eid) {
    public String handleOptionAdd(String options,Long qId,Long eId) {

//        Long qId = Long.valueOf(id);
//        Long eId = Long.valueOf(eid);

        try {
            List<Map<String, String>> optionList = objectMapper.readValue(options, List.class);

//            log.info(id);

            QuestionModel qt = questionRepository.findByQuestionId(Math.toIntExact(qId));
            Optional<ExamModel> ex = examRepository.findById(eId);

            List<OptionModel> op = new ArrayList<>();
            List<Integer> ids = new ArrayList<>();

            for (Map<String, String> option : optionList) {
                log.info("Received Option: {} ", option.toString());
                log.info("Received Option: {}", option.get("number"));
                OptionModel optemp = new OptionModel();
                optemp.setNumber(option.get("number"));
                optemp.setText(option.get("text"));
                optemp.setQuestionModel(qt);
                optemp.setExam_id(ex.get().getExamId());
                Long id = optionRepository.save(optemp).getOptionId();
                ids.add(Integer.valueOf(Math.toIntExact(id)));
                op.add(optemp);
            }
//            qt.setOptionModels(op);

//            log.info(/);
            return ids.toString();
//            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
//            return ResponseEntity.ok("failed");
        }
    }

    @GetMapping(value = "/question-list/{examId}")
    @ResponseBody
    public List<QuestionModel> getQuestionsByExamId(@PathVariable Long examId, Model model) {
        try {
            List<QuestionModel> questions = questionService.getQuestionsByExamId(examId);
            for (QuestionModel q : questions) {
                q.setExamModel(null);
                q.setOptionModels(null);
            }
            return questions;
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
        return null;
    }

}
