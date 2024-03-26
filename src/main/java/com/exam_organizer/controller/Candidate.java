package com.exam_organizer.controller;

import com.exam_organizer.dto.CandidateDto;
import com.exam_organizer.model.ExamModel;
import com.exam_organizer.service.CandidateService;
import com.exam_organizer.service.ExamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class Candidate {

    private Logger log = LoggerFactory.getLogger(Candidate.class);

    private ExamService examService;

    @Autowired
    private CandidateService candidateService;

    @RequestMapping("/candidate")
    public String candidate(){
        log.info("from candidate...");
        return "candidate";
    }

    @PostMapping("/candidate")
    public ResponseEntity<?>createCandidate(@RequestBody CandidateDto req){
        CandidateDto res =  candidateService.create(req);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }


}
