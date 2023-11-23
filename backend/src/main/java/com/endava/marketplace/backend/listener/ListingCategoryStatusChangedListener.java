package com.endava.marketplace.backend.listener;

import com.endava.marketplace.backend.event.ListingCategoryStatusChangedEvent;
import com.endava.marketplace.backend.model.Listing;
import com.endava.marketplace.backend.model.ListingCategory;
import com.endava.marketplace.backend.model.ListingStatus;
import com.endava.marketplace.backend.service.ListingService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListingCategoryStatusChangedListener {
    private final ListingService listingService;

    public ListingCategoryStatusChangedListener(ListingService listingService) {
        this.listingService = listingService;
    }


    @EventListener
    public void onApplicationEvent(ListingCategoryStatusChangedEvent event) {
        ListingCategory listingCategory = event.getListingCategory();
        List<Listing> listingsWithCategory = listingService.findAllListingsByCategory(listingCategory);
        ListingStatus newListingStatus = (listingCategory.getActive()) ?
                listingService.getListingStatusService().getListingStatuses().get("Available") :
                listingService.getListingStatusService().getListingStatuses().get("Blocked");
        listingsWithCategory.forEach(listing -> {
            listing.setStatus(newListingStatus);
            listingService.updateListing(listing);
        });
    }
}
