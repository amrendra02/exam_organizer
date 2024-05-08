package com.exam_organizer.candidate_service;

import com.exam_organizer.candidate_Repository.CandidateExamRegisteredRepo;
import com.exam_organizer.candidate_Repository.CandidateRepository;
import com.exam_organizer.model.CandidateExamRegisteredModel;
import com.exam_organizer.model.CandidateModel;
import com.exam_organizer.model.ExamModel;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CandidateExamList {

    @Autowired
    private ModelMapper modelMapper;

    private Logger log = LoggerFactory.getLogger(CandidateExamList.class);

    @Autowired
    private CandidateExamRegisteredRepo candidateExamRegisteredRepo;

    @Autowired
    private CandidateRepository candidateRepository;

    public List<ExamModel> getExamList(String username, int page) {
        try {
            CandidateModel candidateModel = this.candidateRepository.findByUsername(username);
            if (candidateModel!=null) {

                Page<CandidateExamRegisteredModel> res = this.findExamByCandidate(candidateModel, page);

                log.info("Response");
                List<ExamModel> exam = new ArrayList<>();
                for (CandidateExamRegisteredModel i : res) {
                    log.info("res");
                    log.info("candidate id: {} and exam id: {}", i.getCandidateModel().getCandidateId(), i.getExamModel().getExamId());
                    exam.add(i.getExamModel());
                }

                log.info("Successfully get Exam list.");

                return exam;
            }
            return null;
        } catch (Exception ex) {
            log.info("Error retrieving exams: " + ex.getMessage());
            return null;
        }
    }


    public Page<CandidateExamRegisteredModel> findExamByCandidate(CandidateModel req, int page) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").descending());
        Page<CandidateExamRegisteredModel> res = candidateExamRegisteredRepo.findByCandidateModel(req, pageable);
        return res;
    }


}
