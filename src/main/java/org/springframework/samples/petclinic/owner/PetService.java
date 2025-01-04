package org.springframework.samples.petclinic.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

	@Autowired
	private PetRepository petRepository;

	public List<Pet> findByName(String name) {
		return petRepository.findByName(name);
	}

}
