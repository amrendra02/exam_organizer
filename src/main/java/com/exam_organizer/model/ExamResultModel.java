package com.exam_organizer.model;

import jakarta.persistence.*;
import lombok.Data;



@Entity
@Data
public class ExamResultModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long examId;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private CandidateModel candidateModel;

    private int wrong;
    private int correct;
    private int notDone;
    private String status;
}
