package com.exam_organizer.candidate_Repository;

import com.exam_organizer.model.CandidateModel;
import com.exam_organizer.model.ExamModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<CandidateModel,Long> {

    CandidateModel findByUsername(String email);
    Page<CandidateModel> findByExamModel(ExamModel examModel, Pageable pageable);
    long countByExamModelExamId(Long examId);


}
