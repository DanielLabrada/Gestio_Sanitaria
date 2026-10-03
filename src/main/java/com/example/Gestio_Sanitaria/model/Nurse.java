package com.example.Gestio_Sanitaria.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Nurse {

	private int id;

	@JsonProperty("nombre_completo")
	private String nombreCompleto;

	@JsonProperty("nombre_usuario")
	private String nombreUsuario;

	private String contrasena;

	public Nurse() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getContrasena() {
		return contrasena;
	}

	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}
}
