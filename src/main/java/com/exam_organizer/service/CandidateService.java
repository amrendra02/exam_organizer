package com.exam_organizer.service;

import com.exam_organizer.candidate_Repository.CandidateRepository;
import com.exam_organizer.dto.CandidateDto;
import com.exam_organizer.exception.ResourceNotFoundException;
import com.exam_organizer.model.CandidateModel;
import com.exam_organizer.model.ExamModel;
import com.exam_organizer.repository.ExamRepository;
import org.hibernate.service.spi.ServiceException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CandidateService {
    private Logger log = LoggerFactory.getLogger(CandidateService.class);

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public CandidateDto create(CandidateDto candidateDto) {

        try {
            log.info("Processing to create the student data");

            CandidateModel res = modelMapper.map(candidateDto, CandidateModel.class);
            res.setPassword(bCryptPasswordEncoder.encode(res.getPassword()));

            Optional<ExamModel> exam = examRepository.findById(candidateDto.getExamId());
            if (exam.isPresent()) {
                res.setExamModel(exam.get());
                res = candidateRepository.save(res);
                log.info("Successfully created the student data: {}", res.getCandidateId());
                return modelMapper.map(res, CandidateDto.class);
            }else{
                log.warn("Exam Id not found !!");
                throw new ResourceNotFoundException("Resource not found","Exam ID",candidateDto.getExamId());
            }
        } catch (Exception ex) {
            log.warn(ex.getMessage());
            throw new ServiceException(ex.getMessage());
        }
    }

}
