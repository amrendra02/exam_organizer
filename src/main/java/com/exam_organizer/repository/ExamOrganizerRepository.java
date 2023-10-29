package com.exam_organizer.repository;

import com.exam_organizer.model.ExamOrganizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamOrganizerRepository extends JpaRepository<ExamOrganizer, Long> {
    ExamOrganizer findByUsernameAndPassword(String username, String password);

    ExamOrganizer findByUsername(String username);

}
