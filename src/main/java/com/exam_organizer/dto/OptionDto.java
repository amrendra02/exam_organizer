package com.exam_organizer.dto;

import com.exam_organizer.model.QuestionModel;
import jakarta.persistence.*;
import lombok.*;

@Data
public class OptionDto {

    private Long optionId;

    private Long exam_id;

    private String number;
    private String text;

}
