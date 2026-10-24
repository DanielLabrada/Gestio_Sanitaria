package com.example.Gestio_Sanitaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Gestio_Sanitaria.model.Nurse;

public interface NurseRepository extends JpaRepository<Nurse, Integer> {

	// Spring builds the queries from the method names
	boolean existsByUsernameAndPassword(String username, String password);

	Nurse findByFullNameIgnoreCase(String fullName);
}
