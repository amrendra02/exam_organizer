package com.exam_organizer.candidate_controller;


import com.exam_organizer.candidate_service.CandidateExamHandleService;
import com.exam_organizer.constant.ApiResponse;
import com.exam_organizer.dto.AnswerDTO;
import com.exam_organizer.dto.OptionDto;
import com.exam_organizer.dto.QuestionDto;
import com.exam_organizer.dto.RequestOne;
import com.exam_organizer.model.QuestionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/candidate")
public class CandidateExamHandle {

    private final CandidateExamHandleService candidateExamHandleService;

    private Logger log = LoggerFactory.getLogger(CandidateExamHandle.class);

    public CandidateExamHandle(CandidateExamHandleService candidateExamHandleService) {
        this.candidateExamHandleService = candidateExamHandleService;
    }

    @PostMapping("/exam/participate")
    public ResponseEntity<?> examParticipate(@RequestBody RequestOne req) {
        log.info("Candidate Request to participate in exam: {}", req.getUsername());
        ApiResponse res = candidateExamHandleService.examParticipate(req);
        log.info("{}", res.getMessage());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @GetMapping("/exam/{examId}/question-list")
    public ResponseEntity<?> getQuestionList(@PathVariable Long examId) {
        log.info("Requesting the question list of exam: {}", examId);
        Map<QuestionDto, List<OptionDto>> res = candidateExamHandleService.questionList(examId);
        ApiResponse res2 = new ApiResponse("Internal error", false);
        return new ResponseEntity<>(res == null ? res2 : res, HttpStatus.OK);
    }

    @PostMapping("exam/{examNumber}/answer")
    public ResponseEntity<?> handleAnswer(@PathVariable Long examNumber, @RequestBody AnswerDTO req) {
        log.info("Request to submit AnswerSheet: {}", examNumber);
        log.info("{}", req);
        ApiResponse res = candidateExamHandleService.answerSubmit(req,examNumber);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
