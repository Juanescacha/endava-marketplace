package com.endava.marketplace.backend.event;

import com.endava.marketplace.backend.model.Rating;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SaleRatedEvent extends ApplicationEvent {
    private final Long id;
    private final Integer score;

    public SaleRatedEvent(Object source, Long id, Integer score) {
        super(source);
        this.id = id;
        this.score = score;
    }
}
