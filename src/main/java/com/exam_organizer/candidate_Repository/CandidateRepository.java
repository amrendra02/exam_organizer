package com.exam_organizer.candidate_Repository;

import com.exam_organizer.model.CandidateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<CandidateModel,Long> {

    CandidateModel findByUsername(String email);

}
