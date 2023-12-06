package com.endava.marketplace.backend.repository;

import com.endava.marketplace.backend.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> { }
