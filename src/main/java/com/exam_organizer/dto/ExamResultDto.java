package com.exam_organizer.dto;

import com.exam_organizer.model.CandidateModel;
import lombok.Data;

@Data
public class ExamResultDto {
    private Long id;
    private Long examId;
    private CandidateDto candidateModel;
    private int wrong;
    private int correct;
    private int notDone;
    private String status;
}
