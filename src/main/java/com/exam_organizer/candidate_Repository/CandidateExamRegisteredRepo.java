package com.exam_organizer.candidate_Repository;

import com.exam_organizer.model.CandidateExamRegisteredModel;
import com.exam_organizer.model.CandidateModel;
import com.exam_organizer.model.ExamModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateExamRegisteredRepo extends JpaRepository<CandidateExamRegisteredModel,Long> {
    Page<CandidateExamRegisteredModel> findByExamModel(ExamModel examModel, Pageable pageable);
    Page<CandidateExamRegisteredModel> findByCandidateModel(CandidateModel candidateModel, Pageable pageable);

    CandidateExamRegisteredModel findByCandidateModelAndExamModel(CandidateModel candidateModel,ExamModel examModel);
}
