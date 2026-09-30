package br.com.aguaalerta.controller;

import br.com.aguaalerta.dto.DenunciaRequest;
import br.com.aguaalerta.service.GeminiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gemini")
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping(
            value = "/analisar",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> analisar(
            @RequestBody DenunciaRequest denuncia) {

        try {

            if (denuncia == null ||
                    denuncia.getDescricao() == null ||
                    denuncia.getDescricao().isBlank()) {

                return ResponseEntity.badRequest()
                        .body("A descrição da denúncia não pode estar vazia.");
            }

            String resultado = geminiService.analisarDenuncia(
                    denuncia.getDescricao()
            );

            return ResponseEntity.ok(resultado);

        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao analisar a denúncia.");
        }
    }
}