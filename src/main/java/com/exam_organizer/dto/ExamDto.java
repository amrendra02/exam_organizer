package com.exam_organizer.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExamDto {

    public enum Status {
        ACTIVE, CANCELLED, LIVE
    }
    private Long examId;
    private String examName;
    private Status status;

    private LocalDate examDate;
    private LocalTime startTime;
    private int duration;
    private int totalMarks;


}
