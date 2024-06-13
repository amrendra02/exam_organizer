package com.exam_organizer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestOne {
    private Long examId;
    private Long candidateId;
    private String name;
    private String username;
    private String password;
}
