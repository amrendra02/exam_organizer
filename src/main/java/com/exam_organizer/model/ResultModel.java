package com.exam_organizer.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name = "results")
public class ResultModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resultId;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    private CandidateModel candidateModel;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private ExamModel examModel;

    private int marksObtained;
    private Date examDate;

    @ElementCollection
    @CollectionTable(name = "result_options", joinColumns = @JoinColumn(name = "result_id"))
    @MapKeyColumn(name = "option_id")
    @Column(name = "selected")
    private Map<Long, Boolean> selectedOptions;

    // Constructors, getters, and setters
}

