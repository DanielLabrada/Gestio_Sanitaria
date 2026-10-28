package com.example.Gestio_Sanitaria.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<Boolean> login(@RequestBody Nurse credentials) {
		boolean authenticated = nurseRepository.existsByUsernameAndPassword(
				credentials.getUsername(), credentials.getPassword());
		return ResponseEntity.ok(authenticated);
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<Nurse> findByName(@PathVariable String name) {
		Nurse nurse = nurseRepository.findByFullNameIgnoreCase(name);
		return nurse == null
				? ResponseEntity.notFound().build()
				: ResponseEntity.ok(nurse);
	}

	@GetMapping("/index")
	public ResponseEntity<List<Nurse>> getAll() {
		return ResponseEntity.ok(nurseRepository.findAll());
	}

	
	@GetMapping("/id/{id}")
	public ResponseEntity<Nurse> getNurseById(@PathVariable Integer id) {
		return nurseRepository.findById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}

}
