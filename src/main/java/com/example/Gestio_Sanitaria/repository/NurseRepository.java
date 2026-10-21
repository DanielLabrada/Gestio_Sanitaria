package com.example.Gestio_Sanitaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Gestio_Sanitaria.model.Nurse;

public interface NurseRepository extends JpaRepository<Nurse, Integer> {

	
	boolean existsByNombreUsuarioAndContrasena(String nombreUsuario, String contrasena);
}