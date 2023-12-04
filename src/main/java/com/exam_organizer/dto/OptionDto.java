package com.exam_organizer.dto;

import com.exam_organizer.model.QuestionModel;
import jakarta.persistence.*;

public class OptionDto {

    private Long optionId;

//    private QuestionModel questionModel;

    private Long exam_id;

    private String number;
    private String text;

}
