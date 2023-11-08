package com.endava.marketplace.backend.specification;

import com.endava.marketplace.backend.model.Endavan;
import org.springframework.data.jpa.domain.Specification;

public class EndavanSpecification {
    public static Specification<Endavan> withName(String name) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
    public static Specification<Endavan> withEmail(String email) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }
}
