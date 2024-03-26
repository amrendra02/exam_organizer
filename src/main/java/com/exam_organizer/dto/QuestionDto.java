package com.exam_organizer.dto;

import com.exam_organizer.model.ExamModel;
import com.exam_organizer.model.OptionModel;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {
    private Long questionId;
    private ExamModel examModel;
    private String questionText;
    private byte[] image;
//    private List<OptionModel> optionModels;

    private String correctOption;
}
