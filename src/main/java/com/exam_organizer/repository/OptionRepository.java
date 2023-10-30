package com.exam_organizer.repository;

import com.exam_organizer.model.OptionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<OptionModel,Long> {



}
