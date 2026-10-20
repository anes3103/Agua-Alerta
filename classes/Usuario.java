package classes;

public class Usuario {
    
    private int id;
    private String nome; 
    private String email;
    private String senha;
    private String cpf;
    private String telefone;

    public Usuario(int id, String nome, String email, String senha, String cpf, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public void cadastrar() {
        System.out.println("Usuário " + nome + " cadastrado com sucesso.");
    }

    public boolean login() {
        System.out.println("Tentando login de " + email + " ");
        return true;
    }

    public Denuncia criarDenuncia(int id, String titulo, String descricao, TipoProblema tipoProblema, Localizaçao localizacao) {
    Denuncia novaDenuncia = new Denuncia(id, titulo, descricao, tipoProblema, localizacao);
    System.out.println(nome + " criou uma nova denúncia.");
    return novaDenuncia;
}

    public void editarPerfil() {
        System.out.println("Perfil de " + nome + " atualizado.");
    }

    public void acompanharDenuncia() {
        System.out.println(nome + " está acompanhando uma denúncia.");
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}
