package com.endava.marketplace.backend.repository;

import com.endava.marketplace.backend.model.Endavan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EndavanRepository extends JpaRepository<Endavan, Long>, JpaSpecificationExecutor<Endavan> {
    Optional<Endavan> findEndavanByEmailIgnoreCase(String email);
}
