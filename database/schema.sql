/* Tabela de Usuários */
CREATE TABLE usuario (
    id SERIAL PRIMARY KEY, -- Cria um ID automático e único.
    nome VARCHAR(100) NOT NULL, -- VARCHAR armazena texto de até 100 caracteres.
    email VARCHAR(150) NOT NULL UNIQUE, -- UNIQUE impede e-mails repetidos.
    senha VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) UNIQUE, -- Armazena até 11 caracteres e não permite repetição.
    telefone VARCHAR(20), -- Por não ter o NOT NULL, permite que o campo fique em branco.
    tipo_usuario VARCHAR(20) NOT NULL DEFAULT 'USUARIO' -- Define "USUARIO" como valor padrão.
);


/* Tabela de Localização */
CREATE TABLE localizacao (
    id SERIAL PRIMARY KEY,
    endereco VARCHAR(255), -- Armazena texto de até 255 caracteres.
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(50),
    cep VARCHAR(10),
    latitude DOUBLE PRECISION, -- Armazena números decimais da latitude.
    longitude DOUBLE PRECISION -- Armazena números decimais da longitude.
);


/* Tabela de Denúncia */
CREATE TABLE denuncia (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(150),
    descricao TEXT NOT NULL, -- TEXT permite armazenar textos maiores.
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Armazena data e hora automaticamente.
    status VARCHAR(30) NOT NULL DEFAULT 'PENDENTE',-- Define "PENDENTE" como status inicial.
    tipo_problema VARCHAR(50) NOT NULL, -- Armazena o tipo do problema.

    usuario_id INT NOT NULL, -- Armazena o ID do usuário.
    localizacao_id INT NOT NULL, -- Armazena o ID da localização.

    FOREIGN KEY (usuario_id) REFERENCES usuario(id), -- Liga a denúncia ao usuário.
    FOREIGN KEY (localizacao_id) REFERENCES localizacao(id) -- Liga a denúncia à localização.
);


/* Tabela de Fotos */
CREATE TABLE foto (
    id SERIAL PRIMARY KEY,
    nome_arquivo VARCHAR(255) NOT NULL, -- Armazena o nome do arquivo.
    url VARCHAR(500) NOT NULL, -- Armazena o endereço da imagem.
    data_upload TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Registra data e hora do envio.

    denuncia_id INT NOT NULL, -- Armazena o ID da denúncia.

    FOREIGN KEY (denuncia_id) REFERENCES denuncia(id) -- Liga a foto à denúncia.
);