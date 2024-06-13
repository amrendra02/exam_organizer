package com.exam_organizer.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CandidateExamRegisteredModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private CandidateModel candidateModel;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private ExamModel examModel;

    private String status;

    // Other fields related to the exam registration

    // Constructors, getters, and setters
}
