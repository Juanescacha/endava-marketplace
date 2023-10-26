package com.endava.marketplace.backend.repository;

import com.endava.marketplace.backend.model.Endavan;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EndavanRepository extends JpaRepository<Endavan, Long> {
    Optional<Endavan> findEndavanByEmailIgnoreCase(String email);

    @Modifying
    @Transactional
    @Query("update Endavan e set e.admin = :isAdmin where e.id = :id")
    void updateAdminRole(@Param(value = "id") Long id, @Param(value = "isAdmin") Boolean isAdmin);
}
