package org.springframework.samples.petclinic.pet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.owner.Pet;

public interface PetRepository extends Repository<Pet, Integer> {

	Page<Pet> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
