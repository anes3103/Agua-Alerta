package com.aguaalerta.agua_alerta.repository;

import com.aguaalerta.agua_alerta.model.Denuncia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DenunciaRepository extends JpaRepository<Denuncia, Long> {
}