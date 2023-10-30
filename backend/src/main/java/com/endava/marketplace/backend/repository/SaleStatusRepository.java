package com.endava.marketplace.backend.repository;

import com.endava.marketplace.backend.model.SaleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SaleStatusRepository extends JpaRepository<SaleStatus, Long> {
    Optional<SaleStatus> findSaleStatusByNameEqualsIgnoreCase(String saleStatusName);
}
