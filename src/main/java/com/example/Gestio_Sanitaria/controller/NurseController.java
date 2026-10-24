package com.example.Gestio_Sanitaria.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Gestio_Sanitaria.model.Nurse;
import com.example.Gestio_Sanitaria.repository.NurseRepository;

import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/nurse")
public class NurseController {

	@Autowired
	private NurseRepository nurseRepository;
	private List<Nurse> nurses;

	public NurseController(ObjectMapper mapper) throws IOException {
		InputStream file = new ClassPathResource("nursesData.json").getInputStream();
		Nurse[] nurseArray = mapper.readValue(file, Nurse[].class);
		nurses = Arrays.asList(nurseArray);
	}

	// Returns true if a nurse with that username and password exists in the database
	@PostMapping("/login")
	public boolean login(@RequestBody Nurse credentials) {
		return nurseRepository.existsByUsernameAndPassword(
				credentials.getUsername(), credentials.getPassword());
	}

	// Finds a nurse by full name, ignoring upper/lower case
	@GetMapping("/name/{name}")
	public Nurse findByName(@PathVariable String name) {
		return nurseRepository.findByFullNameIgnoreCase(name);
	}

	// Returns all the nurses stored in the database
	@GetMapping("/index")
	public List<Nurse> getAll() {
		return nurseRepository.findAll();
	}
}
