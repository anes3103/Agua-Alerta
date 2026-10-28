package br.com.aguaalerta.controller;

import br.com.aguaalerta.dto.DenunciaRequest;
import br.com.aguaalerta.service.GeminiIndisponivelException;
import br.com.aguaalerta.service.GeminiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/gemini")
@CrossOrigin("*")
public class GeminiController {

    private static final int MAX_DESCRICAO = 2000;

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping(
            value = "/analisar",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> analisar(
            @RequestBody DenunciaRequest denuncia) {

        try {

            if (denuncia == null ||
                    denuncia.getDescricao() == null ||
                    denuncia.getDescricao().isBlank()) {

                return erro(HttpStatus.BAD_REQUEST,
                        "A descrição da denúncia não pode estar vazia.");
            }

            if (denuncia.getDescricao().length() > MAX_DESCRICAO) {

                return erro(HttpStatus.BAD_REQUEST,
                        "A descrição é muito longa para a análise da IA.");
            }

            String resultado = geminiService.analisarDenuncia(
                    denuncia.getDescricao()
            );

            return ResponseEntity.ok(resultado);

        } catch (IllegalArgumentException e) {

            return erro(HttpStatus.BAD_REQUEST, e.getMessage());

        } catch (GeminiIndisponivelException e) {

            return erro(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());

        } catch (IllegalStateException e) {

            return erro(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

        } catch (Exception e) {

            return erro(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Erro interno ao analisar a denúncia.");
        }
    }

    private ResponseEntity<Map<String, String>> erro(
            HttpStatus status, String mensagem) {

        String texto = mensagem != null
                ? mensagem
                : "Erro ao analisar a denúncia.";

        return ResponseEntity.status(status)
                .body(Map.of("message", texto));
    }
}