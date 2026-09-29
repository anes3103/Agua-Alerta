package com.aguaalerta.agua_alerta.repository;

import com.aguaalerta.agua_alerta.model.Localizacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {
}