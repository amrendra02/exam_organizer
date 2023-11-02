package com.exam_organizer.model;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name = "options")
public class OptionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private QuestionModel questionModel;

    private Long exam_id;

    private String number;
    private String text;

    // Constructors, getters, and setters
}
