package org.springframework.samples.petclinic.owner;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PetSearchController {

	@Autowired
	private PetService petService;

	@GetMapping("/pets/search")
	public String searchPetsForm() {
		return "pets/search-pets";
	}

	@PostMapping("/pets/search")
	public String searchPets(@RequestParam(required = false) String name, Model model) {
		List<Pet> pets = petService.findByName(name);
		model.addAttribute("pets", pets);
		return "pets/list";
	}

}
