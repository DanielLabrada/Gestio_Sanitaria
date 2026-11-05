package com.example.Gestio_Sanitaria.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.Gestio_Sanitaria.model.Nurse;
import com.example.Gestio_Sanitaria.repository.NurseRepository;

@WebMvcTest(NurseController.class)
class NurseControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private NurseRepository nurseRepository;

	@Test
	void loginReturnsSuccessWhenCredentialsAreValid() throws Exception {
		when(nurseRepository.existsByUsernameAndPassword("ana", "secret")).thenReturn(true);

		mockMvc.perform(post("/nurse/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"username":"ana","password":"secret"}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").value(true));
	}

	@Test
	void loginReturnsUnauthorizedWhenCredentialsAreInvalid() throws Exception {
		when(nurseRepository.existsByUsernameAndPassword("ana", "wrong")).thenReturn(false);

		mockMvc.perform(post("/nurse/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"username":"ana","password":"wrong"}
						"""))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$").value(false));
	}

	@Test
	void loginReturnsBadRequestWhenCredentialsAreMissing() throws Exception {
		mockMvc.perform(post("/nurse/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"username":"ana"}
						"""))
				.andExpect(status().isBadRequest());
	}

	@ParameterizedTest
	@ValueSource(strings = {"create", "new", "save", "add"})
	void createAliasesSaveANurse(String endpoint) throws Exception {
		when(nurseRepository.existsByUsernameIgnoreCase("ana")).thenReturn(false);
		when(nurseRepository.save(any(Nurse.class))).thenAnswer(invocation -> {
			Nurse saved = invocation.getArgument(0);
			saved.setId(7);
			return saved;
		});

		mockMvc.perform(post("/nurse/" + endpoint)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"fullName":"Ana Pérez","username":"ana","password":"secret"}
						"""))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(7))
				.andExpect(jsonPath("$.fullName").value("Ana Pérez"))
				.andExpect(jsonPath("$.username").value("ana"))
				.andExpect(jsonPath("$.password").doesNotExist());
	}

	@Test
	void createReturnsBadRequestWhenRequiredDataIsMissing() throws Exception {
		mockMvc.perform(post("/nurse/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"fullName":"Ana Pérez","username":"ana"}
						"""))
				.andExpect(status().isBadRequest());
	}

	@Test
	void createReturnsBadRequestWhenUsernameAlreadyExists() throws Exception {
		when(nurseRepository.existsByUsernameIgnoreCase("ana")).thenReturn(true);

		mockMvc.perform(post("/nurse/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"fullName":"Ana Pérez","username":"ana","password":"secret"}
						"""))
				.andExpect(status().isBadRequest());
	}

	@Test
	void findByNameReturnsNurseWhenFound() throws Exception {
		when(nurseRepository.findByFullNameIgnoreCase("Ana")).thenReturn(nurse(7, "Ana Pérez", "ana"));

		mockMvc.perform(get("/nurse/name/Ana"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(7))
				.andExpect(jsonPath("$.fullName").value("Ana Pérez"))
				.andExpect(jsonPath("$.password").doesNotExist());
	}

	@Test
	void findByNameReturnsNotFoundWhenMissing() throws Exception {
		when(nurseRepository.findByFullNameIgnoreCase("Unknown")).thenReturn(null);

		mockMvc.perform(get("/nurse/name/Unknown"))
				.andExpect(status().isNotFound());
	}

	@Test
	void getAllReturnsNurses() throws Exception {
		when(nurseRepository.findAll()).thenReturn(List.of(
				nurse(7, "Ana Pérez", "ana"),
				nurse(8, "Luis García", "luis")));

		mockMvc.perform(get("/nurse/index"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(7))
				.andExpect(jsonPath("$[1].username").value("luis"))
				.andExpect(jsonPath("$[0].password").doesNotExist());
	}

	@Test
	void getByIdReturnsNurseWhenFound() throws Exception {
		when(nurseRepository.findById(7)).thenReturn(Optional.of(nurse(7, "Ana Pérez", "ana")));

		mockMvc.perform(get("/nurse/id/7"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(7))
				.andExpect(jsonPath("$.username").value("ana"));
	}

	@Test
	void getByIdReturnsNotFoundWhenMissing() throws Exception {
		when(nurseRepository.findById(7)).thenReturn(Optional.empty());

		mockMvc.perform(get("/nurse/id/7"))
				.andExpect(status().isNotFound());
	}

	@Test
	void updateChangesNurseAndPreservesPasswordWhenOmitted() throws Exception {
		Nurse existing = nurse(7, "Ana Pérez", "ana");
		existing.setPassword("original");
		when(nurseRepository.existsById(7)).thenReturn(true);
		when(nurseRepository.findById(7)).thenReturn(Optional.of(existing));
		when(nurseRepository.save(any(Nurse.class))).thenAnswer(invocation -> invocation.getArgument(0));

		mockMvc.perform(put("/nurse/id/7")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"fullName":"Ana Ruiz","username":"ana.ruiz"}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.fullName").value("Ana Ruiz"))
				.andExpect(jsonPath("$.username").value("ana.ruiz"))
				.andExpect(jsonPath("$.password").doesNotExist());

		verify(nurseRepository).save(existing);
		org.junit.jupiter.api.Assertions.assertEquals("original", existing.getPassword());
	}

	@Test
	void updatePasswordWhenProvided() throws Exception {
		Nurse existing = nurse(7, "Ana Pérez", "ana");
		existing.setPassword("original");
		when(nurseRepository.existsById(7)).thenReturn(true);
		when(nurseRepository.findById(7)).thenReturn(Optional.of(existing));
		when(nurseRepository.save(any(Nurse.class))).thenAnswer(invocation -> invocation.getArgument(0));

		mockMvc.perform(put("/nurse/id/7")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"fullName":"Ana Pérez","username":"ana","password":"new-secret"}
						"""))
				.andExpect(status().isOk());

		org.junit.jupiter.api.Assertions.assertEquals("new-secret", existing.getPassword());
	}

	@Test
	void updateReturnsNotFoundWhenNurseDoesNotExist() throws Exception {
		when(nurseRepository.existsById(7)).thenReturn(false);

		mockMvc.perform(put("/nurse/id/7")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"fullName":"Ana Pérez","username":"ana"}
						"""))
				.andExpect(status().isNotFound());
	}

	@Test
	void updateReturnsBadRequestWhenRequiredDataIsBlank() throws Exception {
		when(nurseRepository.existsById(7)).thenReturn(true);

		mockMvc.perform(put("/nurse/id/7")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"fullName":" ","username":"ana"}
						"""))
				.andExpect(status().isBadRequest());
	}

	@Test
	void deleteRemovesNurseWhenFound() throws Exception {
		when(nurseRepository.existsById(7)).thenReturn(true);

		mockMvc.perform(delete("/nurse/id/7"))
				.andExpect(status().isOk());

		verify(nurseRepository).deleteById(7);
	}

	@Test
	void deleteReturnsNotFoundWhenNurseDoesNotExist() throws Exception {
		when(nurseRepository.existsById(7)).thenReturn(false);

		mockMvc.perform(delete("/nurse/id/7"))
				.andExpect(status().isNotFound());
	}

	private Nurse nurse(Integer id, String fullName, String username) {
		Nurse nurse = new Nurse();
		nurse.setId(id);
		nurse.setFullName(fullName);
		nurse.setUsername(username);
		return nurse;
	}
}
