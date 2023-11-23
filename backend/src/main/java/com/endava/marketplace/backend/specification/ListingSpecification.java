package com.endava.marketplace.backend.specification;

import com.endava.marketplace.backend.model.Listing;
import org.springframework.data.jpa.domain.Specification;

public class ListingSpecification {
    public static Specification<Listing> withName(String name) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Listing> withCategoryId(Integer categoryId) {
        return (root, query, builder) -> builder.equal(root.get("category").get("id"), categoryId);
    }
}
