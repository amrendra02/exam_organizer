package com.exam_organizer;


import com.exam_organizer.candidate_Repository.CandidateRepository;
import com.exam_organizer.model.CandidateModel;
import com.exam_organizer.model.ExamModel;
import com.exam_organizer.repository.ExamRepository;
import com.exam_organizer.service.ExamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExamServiceTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private ExamService examService;

    @Test
    public void testCandidateList() {
        // Mock data
        int page = 0;
        long examId = 1L;
        int pageSize = 10;

        ExamModel examModel = new ExamModel();
        examModel.setExamId(examId);

        Pageable pageable = (Pageable) PageRequest.of(page, pageSize, Sort.by("candidateId").descending());

        List<CandidateModel> candidateModels = new ArrayList<>();
        // Add some candidate data to the list

        Page<CandidateModel> expectedPage = new PageImpl<>(candidateModels, (org.springframework.data.domain.Pageable) pageable, candidateModels.size());

        // Mock repository calls
        when(examRepository.findById(examId)).thenReturn(Optional.of(examModel));
        when(candidateRepository.findByExamModel(examModel, (org.springframework.data.domain.Pageable) pageable)).thenReturn(expectedPage);

        // Call the service method
        Page<CandidateModel> resultPage = examService.candidateList(examId,page);

        // Verify the results
        assertEquals(expectedPage, resultPage);

        // Verify repository method calls
        verify(examRepository, times(1)).findById(examId);
        verify(candidateRepository, times(1)).findByExamModel(examModel, pageable);
    }
}
