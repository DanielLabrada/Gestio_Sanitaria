package com.example.Gestio_Sanitaria.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "nurses")
public class Nurse {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	// Database column names are kept as they were
	@Column(name = "nombre_completo", nullable = false, length = 150)
	private String fullName;

	@Column(name = "nombre_usuario", nullable = false, unique = true, length = 50)
	private String username;

	// The password can be received (login) but is never sent back in responses
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "contrasena", nullable = false, length = 255)
	private String password;

	public Nurse() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
