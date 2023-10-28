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
@Table(name = "candidates")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long candidateId;
    private String candidateName;
    private Date dateOfBirth;
    private String email;
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    // Constructors, getters, and setters
}
