package com.example.Gestio_Sanitaria.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Gestio_Sanitaria.model.Nurse;

import tools.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
@RestController
@RequestMapping("/nurse")
public class NurseController {

	private List<Nurse> nurses;

	
	public NurseController(ObjectMapper mapper) throws IOException {
		InputStream fichero = new ClassPathResource("nursesData.json").getInputStream();
		Nurse[] array = mapper.readValue(fichero, Nurse[].class);
		nurses = Arrays.asList(array);
	}

	
	@PostMapping("/login")
	public boolean login(@RequestBody Nurse datos) {
		for (Nurse n : nurses) {
			if (n.getNombreUsuario().equals(datos.getNombreUsuario())
					&& n.getContrasena().equals(datos.getContrasena())) {
				return true;
			}
		}
		return false;
	}
	
	@GetMapping("/all")
	public List<Nurse> getAllNurses() {
		return nurses;
	}
	
	
}