package org.springframework.samples.petclinic.pet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class PetSearchController {

	private final PetRepository pets;

	private static final int PAGE_SIZE = 5;

	public PetSearchController(PetRepository pets) {
		this.pets = pets;
	}

	@GetMapping("/pets/find")
	public String initFindForm(Model model) {
		model.addAttribute("pet", new Pet());
		return "pets/findPets";
	}

	@GetMapping("/pets")
	public String processFindForm(@RequestParam(defaultValue = "1") int page, Pet pet, BindingResult result,
			Model model) {
		// allow parameterless GET request for /pets to return all records
		if (pet.getName() == null) {
			pet.setName(""); // empty string signifies broadest possible search
		}

		// find pets by name
		Page<Pet> petsResults = findPaginatedForPetsName(page, pet.getName());
		if (petsResults.isEmpty()) {
			// no pets found
			result.rejectValue("name", "notFound", "not found");
			return "pets/findPets";
		}

		model.addAttribute("listPets", petsResults.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", petsResults.getTotalPages());
		model.addAttribute("totalItems", petsResults.getTotalElements());
		return "pets/petsList";
	}

	private Page<Pet> findPaginatedForPetsName(int page, String name) {
		Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE);
		return pets.findByNameContainingIgnoreCase(name, pageable);
	}

}
