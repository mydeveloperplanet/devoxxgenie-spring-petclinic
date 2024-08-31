// src/main/java/org/springframework/samples/petclinic/pet/PetSearchController.java

package org.springframework.samples.petclinic.pet;

import java.util.List;

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

	public PetSearchController(PetRepository pets) {
		this.pets = pets;
	}

	@GetMapping("/pets/find")
	public String initFindForm(Model model) {
		model.addAttribute("pet", new Pet());
		return "pets/findPets";
	}

	@GetMapping("/pets")
	public String processFindForm(@RequestParam(defaultValue = "1") int page, Pet pet, BindingResult result, Model model) {
		if (pet.getName() == null) {
			pet.setName("");
		}

		Page<Pet> petsResults = findPaginatedForPetsName(page, pet.getName());
		if (petsResults.isEmpty()) {
			result.rejectValue("name", "notFound", "not found");
			return "pets/findPets";
		}

		if (petsResults.getTotalElements() == 1) {
			pet = petsResults.iterator().next();
			return "redirect:/owners/" + pet.getOwner().getId();
		}

		return addPaginationModel(page, model, petsResults);
	}

	private String addPaginationModel(int page, Model model, Page<Pet> paginated) {
		model.addAttribute("listPets", paginated);
		List<Pet> listPets = paginated.getContent();
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", paginated.getTotalPages());
		model.addAttribute("totalItems", paginated.getTotalElements());
		model.addAttribute("listPets", listPets);
		return "pets/petsList";
	}

	private Page<Pet> findPaginatedForPetsName(int page, String name) {
		int pageSize = 5;
		Pageable pageable = PageRequest.of(page - 1, pageSize);
		return pets.findByName(name, pageable);
	}
}
