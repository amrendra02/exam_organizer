package com.exam_organizer.repository;

import com.exam_organizer.model.ResultModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<ResultModel,Long> {
}
