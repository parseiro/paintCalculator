package com.vilelapinheiro.paintcalculator.repository;

import com.vilelapinheiro.paintcalculator.domain.Parede;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Parede entity.
 */
@Repository
public interface ParedeRepository extends JpaRepository<Parede, Long> {
    default Optional<Parede> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Parede> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Parede> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct parede from Parede parede left join fetch parede.sala",
        countQuery = "select count(distinct parede) from Parede parede"
    )
    Page<Parede> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct parede from Parede parede left join fetch parede.sala")
    List<Parede> findAllWithToOneRelationships();

    @Query("select parede from Parede parede left join fetch parede.sala where parede.id =:id")
    Optional<Parede> findOneWithToOneRelationships(@Param("id") Long id);
}
