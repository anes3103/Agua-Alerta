package com.aguaalerta.agua_alerta.repository;

import com.aguaalerta.agua_alerta.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailIgnoreCase(String email);
    List<Usuario> findAllByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}