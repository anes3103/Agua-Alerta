package classes;

public class Administrador extends Usuario {

    public Administrador(int id, String nome, String email, String senha, String cpf, String telefone) {
        super(id, nome, email, senha, cpf, telefone);
    }

    public void visualizarDenuncia(){
        System.out.println("Visualizando todas as denúncias enviadas.");
    }

    public void alterarStatus(){
        System.out.println("Status de denúncia atualizados.");
    }

    public void editarDenuncia(){
        System.out.println("Denúncia editada.");
    }

    public void removerDenuncia(){
        System.out.println("Denúncia removida.");
    }

    
}