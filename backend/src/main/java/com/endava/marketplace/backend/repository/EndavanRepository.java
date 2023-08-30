package com.endava.marketplace.backend.repository;

import com.endava.marketplace.backend.model.Endavan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EndavanRepository extends JpaRepository<Endavan, Integer> {
}
