package com.exam_organizer.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

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

    // Constructors, getters, and setters
}
