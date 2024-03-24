package com.exam_organizer.service;

import com.exam_organizer.controller.Result;
import com.exam_organizer.exception.ResourceNotFoundException;
import com.exam_organizer.model.CandidateModel;
import com.exam_organizer.model.ExamModel;
import com.exam_organizer.model.ExamResultModel;
import com.exam_organizer.repository.ExamRepository;
import com.exam_organizer.repository.ExamResultRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResultService {

    @Autowired
    private ExamResultRepository examResultRepository;

    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ModelMapper modelMapper;


    private Logger log = LoggerFactory.getLogger(ResultService.class);


    public Page<ExamResultModel> candidateList(Long examId, int page) {
        log.info("Processing the request in service.");
        int pageSize = 10; // Define the page size
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("candidateModel.candidateId").descending());
        try {

            Optional<ExamModel> exam = this.examRepository.findById(examId);
            log.info("Exam found: {}",exam.isPresent());
            if(exam.isPresent())
            {
                     Page<ExamResultModel> res=examResultRepository.findAllByExamId(examId, pageable);
                     log.info("page: {}",res.toString());
                return res;
            }else{
                throw new ResourceNotFoundException("Exam not found","Exam ID: ",examId);
            }

        } catch (Exception ex) {
            log.info("Error retrieving exams: " + ex.getMessage());
            return null;
        }
    }




}
