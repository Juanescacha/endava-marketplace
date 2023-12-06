package com.endava.marketplace.backend.listener;

import com.endava.marketplace.backend.event.SaleRatedEvent;
import com.endava.marketplace.backend.model.Rating;
import com.endava.marketplace.backend.service.RatingService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SaleRatedListener {
    private final RatingService ratingService;

    public SaleRatedListener(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @EventListener
    public void onApplicationEvent(SaleRatedEvent event) {
        Rating rating = ratingService.loadRating(event.getId());
        Integer quantity = rating.getQuantity();

        if(quantity.equals(0)) {
            quantity = 1;
            rating.setScore(Math.round((double) event.getScore() * 10) / 10d);
            rating.setQuantity(quantity);
        }
        else {
            quantity += 1;
            rating.setScore(Math.round((rating.getScore() * (quantity - 1) + event.getScore()) * 10) / (quantity * 10d));
            rating.setQuantity(quantity);
        }

        ratingService.updateRating(rating);
    }
}
