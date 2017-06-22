package bj.softit.gssft.repository;

import bj.softit.gssft.domain.OutStock;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the OutStock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OutStockRepository extends JpaRepository<OutStock,Long> {
    
}
