package com.exam_organizer.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name = "exams")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long examId;
    private String examName;
    private Date examDate;
    private Date startTime;
    private int duration;
    private int totalMarks;
    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private ExamOrganizer organizer;

    // Constructors, getters, and setters
}
