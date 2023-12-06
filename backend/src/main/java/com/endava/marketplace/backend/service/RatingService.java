package com.endava.marketplace.backend.service;

import com.endava.marketplace.backend.exception.EntityNotFoundException;
import com.endava.marketplace.backend.model.Rating;
import com.endava.marketplace.backend.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating loadRating(Long id) {
        Optional<Rating> rating = ratingRepository.findById(id);
        if(rating.isEmpty()) {
            throw new EntityNotFoundException("Rating with id " + id + " wasn't found");
        }
        return rating.get();
    }

    public void updateRating(Rating rating) {
        ratingRepository.save(rating);
    }
}
