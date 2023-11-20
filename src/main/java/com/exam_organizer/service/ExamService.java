package com.exam_organizer.service;

import com.exam_organizer.exception.ResourceNotFoundException;
import com.exam_organizer.model.ExamModel;
import com.exam_organizer.model.ExamOrganizer;
import com.exam_organizer.repository.ExamOrganizerRepository;
import com.exam_organizer.repository.ExamRepository;
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

    public ExamService(ExamOrganizerRepository examOrganizerRepository, ExamRepository examRepository) {
        this.examOrganizerRepository = examOrganizerRepository;
        this.examRepository = examRepository;
    }

    private static final int PAGE_SIZE = 10; // Number of items per page

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
        System.out.println("from EXAM Service...");
        Optional<ExamModel> exam;
        Long organizerId = 0L;
        try {
//            id = Long.parseLong(id);
//            exam = examRepository.findById(id);
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
        } catch (Exception ex) {
            return null;
        }

        return exam;
    }

    public void deletExam(Long examId) {
//        this.examOrganizerRepository.findById(organizerId).orElseThrow(()->new ResourceNotFoundException("User","User Id",organizerId));
        ExamModel exam = this.examRepository.findById(examId).orElseThrow(() -> new ResourceNotFoundException("Exam", "Exam Id", examId));
        this.examRepository.delete(exam);
    }

}
