package com.exam_organizer.dto;

import com.exam_organizer.model.ExamModel;
import lombok.Data;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Data
@Validated
public class CandidateDto {
    private Long candidateId;
    @NotBlank(message = "Candidate not be blank")
    private String candidateName;
    private Date dateOfBirth;

    @NotBlank(message = "Username not be blank")
    private String username;
    private String phoneNumber;

    @NotBlank(message = "Password not be blank")
    private String password;

    @NotBlank(message = "Email not be blank")
    private String email;
    private String role;
    private String status;
    private byte[] image;

    @NotBlank(message = "Exam Id not blank")
    private Long examId;
//    private ExamModel examModel;
}
