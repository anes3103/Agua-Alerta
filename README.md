# Agua-Alerta
Esse é um projeto de faculdade feito em grupo, com a ideia de criar uma plataforma onde as pessoas possam denunciar problemas relacionados à água e ao saneamento, como vazamentos, falta de água e poluição.


## Diagrama de Classes

As classes do Água Alerta foram definidas com base nas principais funções do sistema. A classe **Usuário** representa as pessoas que utilizam a plataforma e podem registrar denúncias. A classe **Administrador** herda de Usuário e possui funções específicas para gerenciar as denúncias. A classe **Denúncia** representa os problemas registrados, enquanto **Localização** identifica onde o problema ocorreu e **Foto** permite anexar imagens à denúncia. As enumerações **StatusDenuncia** e **TipoProblema** foram criadas para definir, respectivamente, os possíveis estados da denúncia e os tipos de problemas que podem ser registrados. O sistema será inicialmente desenvolvido para atender uma cidade específica.


classDiagram

    class Usuario {
        -int id
        -String nome
        -String email
        -String senha
        -String cpf
        -String telefone
        +cadastrar() void
        +login() boolean
        +editarPerfil() void
        +criarDenuncia() Denuncia
        +acompanharDenuncia() void
    }

    class Administrador {
        +visualizarDenuncias() void
        +alterarStatus() void
        +editarDenuncia() void
        +removerDenuncia() void
    }

    class Denuncia {
        -int id
        -String titulo
        -String descricao
        -Date data
        -StatusDenuncia status
        -TipoProblema tipoProblema
        +registrar() void
        +editar() void
        +alterarStatus() void
        +adicionarFoto() void
        +consultarStatus() StatusDenuncia
    }

    class Localizacao {
        -int id
        -String endereco
        -String bairro
        -String cidade
        -String estado
        -String cep
        -double latitude
        -double longitude
        +cadastrarLocalizacao() void
        +atualizarLocalizacao() void
        +obterCoordenadas() String
    }

    class Foto {
        -int id
        -String nomeArquivo
        -String url
        -Date dataUpload
        +enviar() void
        +excluir() void
        +visualizar() void
    }

    class StatusDenuncia {
        <<enumeration>>
        PENDENTE
        EM_ANALISE
        EM_ANDAMENTO
        RESOLVIDA
        CANCELADA
    }

    class TipoProblema {
        <<enumeration>>
        VAZAMENTO
        ESGOTO_A_CEU_ABERTO
        FALTA_DE_AGUA
        AGUA_CONTAMINADA
        POLUICAO_DE_RIO
    }

    Usuario "1" --> "0..*" Denuncia : cria
    Administrador --|> Usuario : herda
    Administrador "1" --> "0..*" Denuncia : gerencia
    Denuncia "1" --> "1" Localizacao : possui
    Denuncia "1" *-- "0..*" Foto : possui
