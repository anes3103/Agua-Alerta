package br.com.aguaalerta.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class GeminiService {

    private static final String GEMINI_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.8-flash:generateContent";

    private static final int MAX_TENTATIVAS = 3;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public GeminiService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .build();
    }

    public String analisarDenuncia(String denuncia) {

        if (denuncia == null || denuncia.isBlank()) {
            throw new IllegalArgumentException(
                    "A denúncia não pode estar vazia."
            );
        }

        String chaveApi = System.getenv("GEMINI_API_KEY");

        if (chaveApi == null || chaveApi.isBlank()) {
            throw new IllegalStateException(
                    "A variável de ambiente GEMINI_API_KEY não foi configurada."
            );
        }

        String prompt = """
                Analise a denúncia abaixo e responda obrigatoriamente em 3 linhas,
                exatamente neste formato:

                Tipo: [tipo do problema]
                Gravidade: [Baixa, Média ou Alta]
                Resumo: [resumo curto]

                Não escreva nenhuma informação antes ou depois dessas 3 linhas.

                Denúncia:
                %s
                """.formatted(denuncia.trim());

        try {

            String requestJson = objectMapper.writeValueAsString(
                    new GeminiRequest(
                            new GeminiContent[]{
                                    new GeminiContent(
                                            new GeminiPart[]{
                                                    new GeminiPart(prompt)
                                            }
                                    )
                            }
                    )
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GEMINI_URL))
                    .timeout(Duration.ofSeconds(60))
                    .header("Content-Type", "application/json")
                    .header("x-goog-api-key", chaveApi)
                    .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                    .build();

            HttpResponse<String> response = enviarComRetry(request);

            if (response.statusCode() < 200 ||
                    response.statusCode() >= 300) {

                return "Erro ao consultar o Gemini. Código: "
                        + response.statusCode()
                        + ". Detalhes: "
                        + extrairMensagemErro(response.body());
            }

            return extrairTextoResposta(response.body());

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                    "A requisição ao Gemini foi interrompida.",
                    e
            );

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Não foi possível comunicar com o Gemini.",
                    e
            );
        }
    }

    private HttpResponse<String> enviarComRetry(HttpRequest request)
            throws IOException, InterruptedException {

        HttpResponse<String> response = null;

        for (int tentativa = 1;
             tentativa <= MAX_TENTATIVAS;
             tentativa++) {

            response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() < 500 ||
                    response.statusCode() >= 600) {

                return response;
            }

            if (tentativa < MAX_TENTATIVAS) {

                long espera = (long) Math.pow(2, tentativa) * 1000;

                Thread.sleep(espera);
            }
        }

        return response;
    }

    private String extrairTextoResposta(String json)
            throws IOException {

        JsonNode raiz = objectMapper.readTree(json);

        JsonNode candidatos = raiz.path("candidates");

        if (!candidatos.isArray() || candidatos.isEmpty()) {

            throw new IllegalStateException(
                    "O Gemini não retornou nenhum candidato na resposta."
            );
        }

        JsonNode partes = candidatos.get(0)
                .path("content")
                .path("parts");

        if (!partes.isArray() || partes.isEmpty()) {

            throw new IllegalStateException(
                    "O Gemini retornou uma resposta sem texto."
            );
        }

        String texto = partes.get(0)
                .path("text")
                .asText(null);

        if (texto == null || texto.isBlank()) {

            throw new IllegalStateException(
                    "Não foi possível encontrar o texto na resposta do Gemini."
            );
        }

        return texto.trim();
    }

    private String extrairMensagemErro(String json) {

        try {

            JsonNode raiz = objectMapper.readTree(json);

            String mensagem = raiz
                    .path("error")
                    .path("message")
                    .asText(null);

            if (mensagem != null && !mensagem.isBlank()) {
                return mensagem;
            }

        } catch (IOException ignored) {
        }

        return "Não foi possível obter detalhes do erro.";
    }

    private record GeminiRequest(
            GeminiContent[] contents
    ) {
    }

    private record GeminiContent(
            GeminiPart[] parts
    ) {
    }

    private record GeminiPart(
            String text
    ) {
    }
}