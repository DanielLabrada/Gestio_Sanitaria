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

	// On startup: if the table is empty, fill it with the data from the JSON file
	@Override
	public void run(String... args) throws Exception {
		if (nurseRepository.count() == 0) {
			InputStream file = new ClassPathResource("nursesData.json").getInputStream();
			Nurse[] nurseArray = mapper.readValue(file, Nurse[].class);
			for (Nurse nurse : nurseArray) {
				nurse.setId(null); // the database generates the id
				nurseRepository.save(nurse);
			}
		}
	}
}
