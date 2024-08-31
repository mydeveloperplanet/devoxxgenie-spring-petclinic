// src/main/java/org/springframework/samples/petclinic/owner/PetRepository.java

package org.springframework.samples.petclinic.owner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PetRepository extends Repository<Pet, Integer> {

    // ... existing methods ...

    @Query("SELECT DISTINCT pet FROM Pet pet LEFT JOIN FETCH pet.owner WHERE pet.name LIKE :name%")
    @Transactional(readOnly = true)
    Page<Pet> findByName(@Param("name") String name, Pageable pageable);
}
