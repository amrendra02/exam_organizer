package com.exam_organizer.dto;

import com.exam_organizer.model.ExamModel;
import lombok.Data;

import java.util.Date;

@Data
public class CandidateDto {
    private Long candidateId;
    private String candidateName;
    private Date dateOfBirth;
    private String username;
    private String phoneNumber;
    private String password;
    private String email;
    private String role;
    private String status;
//    private ExamModel examModel;
}
