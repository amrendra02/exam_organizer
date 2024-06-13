package com.exam_organizer.dto;


import lombok.Data;

import java.util.Map;

@Data
public class AnswerDTO {
    private Map<String, OptionDetails> options;
    private String username;
    private String email;

    @Data
    public static class OptionDetails {
        private String optionNumber;
        private String text;
        private Long optionId;
        private int questionId;
    }
}