package com.aguaalerta.agua_alerta.controller;

import com.aguaalerta.agua_alerta.model.TipoOcorrencia;
import com.aguaalerta.agua_alerta.repository.TipoOcorrenciaRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-ocorrencia")
@CrossOrigin(origins = "*")
public class TipoOcorrenciaController {
    private final TipoOcorrenciaRepository repository;
    public TipoOcorrenciaController(TipoOcorrenciaRepository repository) { this.repository = repository; }
    @GetMapping
    public List<TipoOcorrencia> listar() { return repository.findAll(); }
}
