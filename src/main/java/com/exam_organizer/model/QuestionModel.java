package com.exam_organizer.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name = "questions")
public class QuestionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "exam_id")
    private ExamModel examModel;

    @Lob
    @Column(length = 1000)
    private String questionText;

    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;

    @OneToMany(mappedBy = "questionModel", cascade = CascadeType.ALL)
    private List<OptionModel> optionModels;

    private String correctOption;

    // Constructors, getters, and setters
}
