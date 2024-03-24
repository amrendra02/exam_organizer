package com.exam_organizer.repository;

import com.exam_organizer.dto.ExamResultDto;
import com.exam_organizer.model.ExamModel;
import com.exam_organizer.model.ExamResultModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamResultRepository extends JpaRepository<ExamResultModel,Long> {

    Page<ExamResultModel> findAllByExamId(Long examId, Pageable pageable);
}
