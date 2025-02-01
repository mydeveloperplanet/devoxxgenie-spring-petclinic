package org.springframework.samples.petclinic.pet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.owner.Pet;

public interface PetRepository extends Repository<Pet, Integer> {

	@Query("SELECT DISTINCT pet FROM Pet pet LEFT JOIN FETCH pet.owner WHERE LOWER(pet.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	Page<Pet> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
