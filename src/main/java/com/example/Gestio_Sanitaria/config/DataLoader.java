package com.example.Gestio_Sanitaria.config;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.example.Gestio_Sanitaria.model.Nurse;
import com.example.Gestio_Sanitaria.repository.NurseRepository;

import tools.jackson.databind.ObjectMapper;

@Component
public class DataLoader implements CommandLineRunner {

	@Autowired
	private NurseRepository nurseRepository;

	@Autowired
	private ObjectMapper mapper;

	
	@Override
	public void run(String... args) throws Exception {
		if (nurseRepository.count() == 0) {
			InputStream fichero = new ClassPathResource("nursesData.json").getInputStream();
			Nurse[] array = mapper.readValue(fichero, Nurse[].class);
			for (Nurse n : array) {
				n.setId(null); 
				nurseRepository.save(n);
			}
		}
	}
}