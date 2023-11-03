package com.exam_organizer.repository;

import com.exam_organizer.model.ExamModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<ExamModel, Long> {

    //    @Query("SELECT e FROM ExamModel e WHERE e.organizerId = :organizerId")
    //    List<ExamModel> findAllExamsByOrganizerId(@Param("organizerId") Long organizerId);
    @Query("SELECT e FROM ExamModel e WHERE e.organizer.organizerId = :organizerId")
    List<ExamModel> findAllExamsByOrganizerId(@Param("organizerId") Long organizerId);

    Page<ExamModel> findAllByOrganizer_OrganizerId(Long organizerId, Pageable pageable);

    ExamModel findByExamIdAndOrganizer_OrganizerId(Long examId, Long organizerId);


}
