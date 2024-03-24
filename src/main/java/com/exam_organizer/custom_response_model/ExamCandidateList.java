package com.exam_organizer.custom_response_model;

import com.exam_organizer.model.CandidateModel;
import com.exam_organizer.model.ExamModel;
import lombok.Data;

import java.util.List;

@Data
public class ExamCandidateList {
    private ExamModel examModel;
    private List<CandidateModel> candidateModelList;
}
