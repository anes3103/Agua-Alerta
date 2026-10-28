package br.com.aguaalerta.service;

public class GeminiIndisponivelException extends RuntimeException {

    public GeminiIndisponivelException(String mensagem) {
        super(mensagem);
    }
}