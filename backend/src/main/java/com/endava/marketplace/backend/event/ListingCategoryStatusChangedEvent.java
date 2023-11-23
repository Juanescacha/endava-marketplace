package com.endava.marketplace.backend.event;

import com.endava.marketplace.backend.model.ListingCategory;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ListingCategoryStatusChangedEvent extends ApplicationEvent {
    private final ListingCategory listingCategory;

    public ListingCategoryStatusChangedEvent(Object source, ListingCategory listingCategory) {
        super(source);
        this.listingCategory = listingCategory;
    }
}
