package com.exam_organizer.service;

import com.exam_organizer.candidate_Repository.CandidateRepository;
import com.exam_organizer.dto.ExamDto;
import com.exam_organizer.exception.ResourceNotFoundException;
import com.exam_organizer.model.CandidateModel;
import com.exam_organizer.model.ExamModel;
import com.exam_organizer.model.ExamOrganizer;
import com.exam_organizer.repository.ExamOrganizerRepository;
import com.exam_organizer.repository.ExamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExamService {


    private final ExamOrganizerRepository examOrganizerRepository;
    private final ExamRepository examRepository;

    private Logger log = LoggerFactory.getLogger(ExamService.class);


    private final CandidateRepository candidateRepository;

    public ExamService(ExamOrganizerRepository examOrganizerRepository, ExamRepository examRepository, CandidateRepository candidateRepository) {
        this.examOrganizerRepository = examOrganizerRepository;
        this.examRepository = examRepository;
        this.candidateRepository = candidateRepository;
    }

    public String CreateExam(ExamModel exam) {
        try {
            examRepository.save(exam);
            return "success";
        } catch (Exception ex) {

            return "failed";
        }
    }

    public Page<ExamModel> examList(int page) {
        int pageSize = 10; // Define the page size
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("examId").descending());
        try {

            Long organizerId = null;
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof ExamOrganizer) {
                    ExamOrganizer examOrganizer = (ExamOrganizer) principal;
                    organizerId = examOrganizer.getOrganizerId();
                }
                return examRepository.findAllByOrganizer_OrganizerId(organizerId, pageable);
            }
            return null;
        } catch (Exception ex) {
            System.out.println("Error retrieving exams: " + ex.getMessage());
            return null;
        }
    }

    public Optional<ExamModel> examData(Long id) {
        System.out.println("from EXAM Service..."+ id);
        Optional<ExamModel> exam;
        Long organizerId = 0L;
        try {
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof ExamOrganizer) {
                    ExamOrganizer examOrganizer = (ExamOrganizer) principal;
                    organizerId = examOrganizer.getOrganizerId();
                }
            }
            exam = Optional.ofNullable(examRepository.findByExamIdAndOrganizer_OrganizerId(id, organizerId));
            exam.get().setOrganizer(null);
            exam.get().setQuestions(null);
            System.out.println("..3");
        } catch (Exception ex) {
            return null;
        }
        return exam;
    }

    public void deletExam(Long examId) {
        ExamModel exam = this.examRepository.findById(examId).orElseThrow(() -> new ResourceNotFoundException("Exam", "Exam Id", examId));
        this.examRepository.delete(exam);
    }

    public void update(String name,long examId,ExamDto examDto){

        Long organizerId =  this.examOrganizerRepository.findByUsername(name).getOrganizerId();
        ExamModel exam = this.examRepository.findByExamIdAndOrganizer_OrganizerId(examId,organizerId);
        if(examDto.getExamName()!=null) exam.setExamName(examDto.getExamName());
        if(examDto.getExamDate()!=null) exam.setExamDate(examDto.getExamDate());
        if (examDto.getDuration() >0) exam.setDuration(examDto.getDuration());
        if (examDto.getStartTime()!=null) exam.setStartTime(examDto.getStartTime());
        if(examDto.getTotalMarks()>0) exam.setTotalMarks(examDto.getTotalMarks());
        if(examDto.getStatus()!=null){
            if (examDto.getStatus()== ExamDto.Status.ACTIVE) exam.setStatus(ExamModel.Status.ACTIVE);
            if (examDto.getStatus()== ExamDto.Status.CANCELLED) exam.setStatus(ExamModel.Status.CANCELLED);
            if (examDto.getStatus()== ExamDto.Status.LIVE) exam.setStatus(ExamModel.Status.LIVE);
        }
        this.examRepository.save(exam);
    }

    // get registered candidate list
    public Page<CandidateModel> candidateList(Long examId, int page) {
        int pageSize = 10; // Define the page size
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("candidateId").descending());
        try {
            Optional<ExamModel> examModel = this.examRepository.findById(examId);
            if(examModel.isPresent()){
                return candidateRepository.findByExamModel(examModel.get(), pageable);
            }
            return null;
        } catch (Exception ex) {
            log.info("Error retrieving exams: " + ex.getMessage());
            return null;
        }
    }
}
