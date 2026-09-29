package com.aguaalerta.agua_alerta.repository;

import com.aguaalerta.agua_alerta.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}