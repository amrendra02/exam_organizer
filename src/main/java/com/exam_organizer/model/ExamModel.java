package com.exam_organizer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name = "exams")
public class ExamModel {

    public enum Status {
        ACTIVE, CANCELLED, LIVE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examId;

    private String examName;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;


    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate examDate;

    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    private int duration;
    private int totalMarks;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private ExamOrganizer organizer;

    // ... (existing fields)

    @JsonIgnoreProperties("examModel")
    @OneToMany(mappedBy = "examModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionModel> questions;

    // Additional method to add a question
    public void addQuestion(QuestionModel question) {
        if (questions == null) {
            questions = new ArrayList<>();
        }
        questions.add(question);
        question.setExamModel(this);
    }

    // Additional method to remove a question
    public void removeQuestion(QuestionModel question) {
        if (questions != null) {
            questions.remove(question);
            question.setExamModel(null);
        }
    }
}
