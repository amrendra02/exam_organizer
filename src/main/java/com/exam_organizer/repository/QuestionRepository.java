package com.exam_organizer.repository;

import com.exam_organizer.model.QuestionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionModel,Long> {
    QuestionModel findByQuestionId(int id);

    List<QuestionModel> findAllByExamModelExamId(Long id);


}
