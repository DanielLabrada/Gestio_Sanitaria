package com.example.Gestio_Sanitaria.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.example.Gestio_Sanitaria.model.Nurse;
import com.example.Gestio_Sanitaria.repository.NurseRepository;

@RestController
@RequestMapping("/nurse")
public class NurseController {

	@Autowired
	private NurseRepository nurseRepository;

	// Login: 200 OK if the credentials are correct, 401 if not, 400 if data is missing
	@PostMapping("/login")
	public ResponseEntity<Boolean> login(@RequestBody Nurse credentials) {
		if (credentials.getUsername() == null || credentials.getPassword() == null) {
			return ResponseEntity.badRequest().build();
		}

		boolean authenticated = nurseRepository.existsByUsernameAndPassword(
				credentials.getUsername(), credentials.getPassword());

		if (!authenticated) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
		}
		return ResponseEntity.ok(true);
	}

	@PostMapping({"/create", "/new", "/save", "/add"})
	public ResponseEntity<Nurse> create(@RequestBody Nurse nurse) {
		if (nurse == null || nurse.getFullName() == null || nurse.getUsername() == null || nurse.getPassword() == null) {
			return ResponseEntity.badRequest().build();
		}

		if (nurseRepository.existsByUsernameIgnoreCase(nurse.getUsername())) {
			return ResponseEntity.badRequest().build();
		}

		Nurse savedNurse = nurseRepository.save(nurse);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedNurse);
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
	
	
	@DeleteMapping("/id/{id}")
	public ResponseEntity<Void> deleteNurse(@PathVariable Integer id) {
		if (!nurseRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		nurseRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
