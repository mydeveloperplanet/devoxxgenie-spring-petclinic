package org.springframework.samples.petclinic.pet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PetSearchControllerTest {

	@Mock
	private PetRepository petRepository;

	@Mock
	private Model model;

	@Mock
	private BindingResult bindingResult;

	@InjectMocks
	private PetSearchController petSearchController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testInitFindForm() {
		String result = petSearchController.initFindForm(model);
		assertEquals("pets/findPets", result);
		verify(model).addAttribute(eq("pet"), any(Pet.class));
	}

	@Test
	void testProcessFindFormWithNoPets() {
		Pet pet = new Pet();
		pet.setName("NonExistentPet");

		when(petRepository.findByName(eq("NonExistentPet"), any(PageRequest.class)))
			.thenReturn(Page.empty());

		String result = petSearchController.processFindForm(1, pet, bindingResult, model);

		assertEquals("pets/findPets", result);
		verify(bindingResult).rejectValue("name", "notFound", "not found");
	}

	@Test
	void testProcessFindFormWithOnePet() {
		Pet pet = new Pet();
		pet.setName("ExistingPet");
		Owner owner = new Owner();
		owner.setId(1);
		pet.setOwner(owner);

		List<Pet> pets = new ArrayList<>();
		pets.add(pet);
		Page<Pet> petPage = new PageImpl<>(pets);

		when(petRepository.findByName(eq("ExistingPet"), any(PageRequest.class)))
			.thenReturn(petPage);

		String result = petSearchController.processFindForm(1, pet, bindingResult, model);

		assertEquals("redirect:/owners/1", result);
	}

	@Test
	void testProcessFindFormWithMultiplePets() {
		Pet pet = new Pet();
		pet.setName("CommonPet");

		List<Pet> pets = new ArrayList<>();
		pets.add(new Pet());
		pets.add(new Pet());
		Page<Pet> petPage = new PageImpl<>(pets);

		when(petRepository.findByName(eq("CommonPet"), any(PageRequest.class)))
			.thenReturn(petPage);

		String result = petSearchController.processFindForm(1, pet, bindingResult, model);

		assertEquals("pets/petsList", result);
		verify(model).addAttribute(eq("listPets"), eq(petPage));
		verify(model).addAttribute(eq("currentPage"), eq(1));
		verify(model).addAttribute(eq("totalPages"), eq(1));
		verify(model).addAttribute(eq("totalItems"), eq(2L));
		verify(model).addAttribute(eq("listPets"), eq(pets));
	}
}
