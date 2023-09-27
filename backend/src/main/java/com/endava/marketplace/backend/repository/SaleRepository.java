package com.endava.marketplace.backend.repository;

import com.endava.marketplace.backend.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
    Optional<Set<Sale>> findSalesByBuyer_Id(Long buyerId);

    Optional<Set<Sale>> findSalesByListing_Seller_Id(Long sellerId);
}
