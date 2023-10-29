package com.exam_organizer.repository;

import com.exam_organizer.model.ExamModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<ExamModel,Long> {
}
