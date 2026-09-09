# Agua-Alerta
Esse é um projeto de faculdade feito em grupo, com a ideia de criar uma plataforma onde as pessoas possam denunciar problemas relacionados à água e ao saneamento, como vazamentos, falta de água e poluição.

## Sobre o projeto

O **Água Alerta** é uma plataforma que permite que cidadãos denunciem problemas relacionados à água e ao saneamento básico em suas regiões. O objetivo é engajar a comunidade na identificação de falhas na infraestrutura e incentivar a busca por soluções junto aos órgãos responsáveis.

## Como funciona o registro de uma denúncia

O processo foi pensado para ser simples e rápido:

1. O usuário acessa o site e inicia a criação de um novo relato.
2. Informa os dados essenciais da ocorrência localização, descrição e fotos.
3. Envia o formulário para validação do sistema.
4. A denúncia é registrada de forma publica na plataforma, ficando disponível para acompanhamento e engajamento da comunidade local.

## Informações registradas no sistema

Cada denúncia pode conter:

| Campo | Descrição |
|---|---|
| **Localização geográfica** | Endereço aproximado ou coordenadas do local afetado |
| **Tipo de ocorrência** | Classificação do problema (veja abaixo) |
| **Descrição detalhada** | Texto explicativo fornecido pelo usuário |
| **Evidências visuais** | Foto(s) do local para comprovação do problema |

**Tipos de ocorrência:**
-  Vazamentos de água
-  Esgoto a céu aberto
-  Água contaminada ou alteração na qualidade
-  Falta de água ou desabastecimento
-  Poluição de rios e mananciais

## O papel da Inteligência Artificial

A IA atua como triagem, validação e organização dos dados recebidos pela plataforma.

**Análise e classificação de denúncias**
- **Processamento de imagens:** analisa as fotos enviadas para confirmar se correspondem a vazamentos, esgoto, contaminação ou poluição, reduzindo envios incorretos ou spam.
- **Categorização automática de texto:** processa a descrição digitada pelo usuário e categoriza automaticamente a gravidade e o tipo de problema.

**Por que a IA é fundamental**
- **Escalabilidade**  processa centenas de relatos simultâneos sem gargalos manuais.
- **Priorização**  identifica automaticamente ocorrências de alto risco.
- **Organização de dados**  padroniza as informações, facilitando a visualização em mapas de calor e relatórios comunitários.

## Escopo do MVP

Nesta fase inicial, o MVP do Água Alerta atende:

- [x] Formulário funcional de cadastro de denúncia (localização, tipo de problema, descrição e upload de foto)
- [x] Classificação simples acionada por IA (validação do texto e análise inicial do tipo de problema/imagem)
- [x] Feed/lista de denúncias registradas, exibindo informações da comunidade e status do relato

## Problema Real

O município de Aiuaba-CE enfrenta desafios como perdas na distribuição de água, problemas na infraestrutura de abastecimento, dificuldades no esgotamento sanitário e possíveis problemas relacionados à qualidade da água. Dados do SINISA 2024 apontam perdas de aproximadamente 40,8% da água distribuída, enquanto o Censo 2022 mostra que 48,4% da população utiliza fossa rudimentar ou buraco para o afastamento de dejetos.

Diante desse cenário, o Água Alerta busca resolver a dificuldade de registrar, localizar, organizar e acompanhar ocorrências identificadas pela população, como vazamentos, falta de água, esgoto a céu aberto, danos em tubulações e problemas de qualidade da água.

A plataforma permitirá que o cidadão registre uma ocorrência com tipo do problema, localização, descrição, data, fotografia e status de atendimento. Essas informações serão organizadas e apresentadas em um mapa, permitindo identificar regiões com maior concentração de ocorrências e fornecendo dados que podem auxiliar a gestão municipal.

Em resumo, o Água Alerta busca transformar:
Problema observado → Registro → Localização → Organização → Acompanhamento
em um processo simples e acessível para a população de Aiuaba.

## Público-alvo

O Água Alerta será desenvolvido para **moradores e pessoas da comunidade**, que desejam denunciar problemas relacionados à água e ao saneamento, como vazamentos, esgoto a céu aberto, água contaminada, falta de água e poluição de rios. Além disso, a plataforma contará com **administradores responsáveis pelo gerenciamento das informações e das denúncias registradas**.

Os usuários poderão registrar as ocorrências, informar o local, descrever o problema e anexar fotos. As informações beneficiarão a comunidade, que poderá acompanhar os problemas identificados, além de servir como apoio para órgãos públicos, empresas de saneamento e organizações ambientais na busca por soluções.

O sistema foi pensado para facilitar a comunicação dessas situações e incentivar a participação da população na melhoria das condições de água e saneamento.

## Diagrama de Classes

As classes do Água Alerta foram definidas com base nas principais funções do sistema. A classe **Usuário** representa as pessoas que utilizam a plataforma e podem registrar denúncias. A classe **Administrador** herda de Usuário e possui funções específicas para gerenciar as denúncias. A classe **Denúncia** representa os problemas registrados, enquanto **Localização** identifica onde o problema ocorreu e **Foto** permite anexar imagens à denúncia. As enumerações **StatusDenuncia** e **TipoProblema** foram criadas para definir, respectivamente, os possíveis estados da denúncia e os tipos de problemas que podem ser registrados.

Aqui estão as principais entidades, suas classes, atributos e como se relacionam:

```text
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
```
Aqui está como ficaria o Diagrama de classes na sua versão visual:

<img width="1076" height="1514" alt="image" src="https://github.com/user-attachments/assets/fdd1cc86-cc3f-430c-bf7d-aed3c6bd5ef9" />

