package com.aguaalerta.agua_alerta.controller;

import com.aguaalerta.agua_alerta.model.Usuario;
import com.aguaalerta.agua_alerta.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UsuarioControllerTest {

    private final UsuarioRepository repository = mock(UsuarioRepository.class);
    private final UsuarioController controller = new UsuarioController(repository);

    @Test
    void loginSelecionaContaCorrespondenteQuandoEmailEstaDuplicado() {
        Usuario primeiraConta = new Usuario("Primeira", "mesmo@example.com", "senha-1", null, null);
        Usuario segundaConta = new Usuario("Segunda", "mesmo@example.com", "senha-2", null, null);
        when(repository.findAllByEmail("mesmo@example.com")).thenReturn(List.of(primeiraConta, segundaConta));

        ResponseEntity<?> resposta = controller.login(Map.of("email", "mesmo@example.com", "senha", "senha-2"));

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertSame(segundaConta, resposta.getBody());
    }

    @Test
    void loginRejeitaSenhaInvalidaMesmoComEmailDuplicado() {
        Usuario conta = new Usuario("Conta", "mesmo@example.com", "senha-certa", null, null);
        when(repository.findAllByEmail("mesmo@example.com")).thenReturn(List.of(conta, conta));

        ResponseEntity<?> resposta = controller.login(Map.of("email", "mesmo@example.com", "senha", "senha-errada"));

        assertEquals(HttpStatus.UNAUTHORIZED, resposta.getStatusCode());
    }

    @Test
    void cadastroRecusaEmailJaUtilizado() {
        Usuario usuario = new Usuario("Conta", "mesmo@example.com", "senha-segura", null, null);
        when(repository.existsByEmail("mesmo@example.com")).thenReturn(true);

        ResponseEntity<?> resposta = controller.cadastrar(usuario);

        assertEquals(HttpStatus.CONFLICT, resposta.getStatusCode());
    }
}
