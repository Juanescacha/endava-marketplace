package com.endava.marketplace.backend.repository;

import com.endava.marketplace.backend.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Set<Question> findAllByListing_Id(Long id);
}
