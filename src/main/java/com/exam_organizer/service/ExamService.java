package com.exam_organizer.service;

import com.exam_organizer.model.ExamModel;
import com.exam_organizer.repository.ExamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExamService {


    private final ExamRepository examRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    private static final int PAGE_SIZE = 10; // Number of items per page

    public String CreateExam(ExamModel exam)
    {
        try{
            examRepository.save(exam);
            return"success";
        }catch(Exception ex){

            return"failed";
        }
    }


 /*   public Optional<ExamModel> examData(Long id){

        Optional<ExamModel> exam=null;
        try{
            exam = examRepository.findById(id);
        }catch (Exception ex){
            return exam;
        }
        return exam;
    }*/

    public Page<ExamModel> examList(int page){
        int pageSize = 10; // Define the page size
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("examId").descending());
        try {
            return examRepository.findAll(pageable);
        } catch (Exception ex) {
            System.out.println("Error retrieving exams: " + ex.getMessage());
            return null;
        }
    }
}
