package com.aguaalerta.agua_alerta.model;

import jakarta.persistence.Entity;

@Entity
public class Administrador extends Usuario {

    public Administrador() {
    }

    public Administrador(String nome, String email, String senha, String cpf, String telefone) {
        super(nome, email, senha, cpf, telefone);
    }

    public void visualizarDenuncias() {
        System.out.println("Visualizando todas as denúncias registradas.");
    }

    public void alterarStatus() {
        System.out.println("Status da denúncia alterado.");
    }

    public void editarDenuncia() {
        System.out.println("Denúncia editada.");
    }

    public void removerDenuncia() {
        System.out.println("Denúncia removida.");
    }
}