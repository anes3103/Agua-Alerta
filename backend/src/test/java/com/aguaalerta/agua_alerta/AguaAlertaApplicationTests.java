package com.aguaalerta.agua_alerta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.aguaalerta.agua_alerta.model.Usuario;
import com.aguaalerta.agua_alerta.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class AguaAlertaApplicationTests {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UsuarioRepository usuarioRepository;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Test
	void contextLoads() {
	}

	@Test
	void usuarioJsonDoesNotExposePassword() throws JsonProcessingException {
		Usuario usuario = new Usuario("Pessoa", "pessoa@example.com", "senha-de-teste", null, null);

		String json = objectMapper.writeValueAsString(usuario);

		assertFalse(json.contains("\"senha\""));
		assertFalse(json.contains("senha-de-teste"));
	}

	@Test
	void loginReturnsUserWithoutPassword() throws Exception {
		usuarioRepository.deleteAll();
		usuarioRepository.save(new Usuario("Pessoa", "pessoa@example.com", "senha-correta", null, null));

		mockMvc.perform(post("/api/usuarios/login")
						.contentType(APPLICATION_JSON)
						.content("""
								{"email":"PESSOA@example.com","senha":"senha-correta"}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email", is("pessoa@example.com")))
				.andExpect(jsonPath("$.senha").doesNotExist());

		String senhaAtualizada = usuarioRepository.findByEmailIgnoreCase("pessoa@example.com")
				.orElseThrow()
				.getSenha();
		assertTrue(passwordEncoder.matches("senha-correta", senhaAtualizada));
	}

	@Test
	void registrationStoresPasswordAsHash() throws Exception {
		String email = "nova-pessoa@example.com";
		usuarioRepository.deleteAll();

		mockMvc.perform(post("/api/usuarios")
						.contentType(APPLICATION_JSON)
						.content("""
								{"nome":"Pessoa","email":"nova-pessoa@example.com","senha":"senha-nova","cpf":null,"telefone":null}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.senha").doesNotExist());

		String senhaArmazenada = usuarioRepository.findByEmailIgnoreCase(email)
				.orElseThrow()
				.getSenha();
		assertTrue(passwordEncoder.matches("senha-nova", senhaArmazenada));
	}

	@Test
	void loginRejectsInvalidPassword() throws Exception {
		usuarioRepository.deleteAll();
		usuarioRepository.save(new Usuario("Pessoa", "pessoa@example.com", "senha-correta", null, null));

		mockMvc.perform(post("/api/usuarios/login")
						.contentType(APPLICATION_JSON)
						.content("""
								{"email":"pessoa@example.com","senha":"incorreta"}
								"""))
				.andExpect(status().isUnauthorized());
	}

}
