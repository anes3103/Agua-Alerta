CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) UNIQUE,
    telefone VARCHAR(20),
    tipo_usuario VARCHAR(20) NOT NULL DEFAULT 'USUARIO'
);

CREATE TABLE localizacao (
    id SERIAL PRIMARY KEY,
    endereco VARCHAR(255),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    estado VARCHAR(50),
    cep VARCHAR(10),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION
);

CREATE TABLE denuncia (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(150),
    descricao TEXT NOT NULL,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDENTE',
    tipo_problema VARCHAR(50) NOT NULL,

    usuario_id INT NOT NULL,
    localizacao_id INT NOT NULL,

    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (localizacao_id) REFERENCES localizacao(id)
);

CREATE TABLE foto (
    id SERIAL PRIMARY KEY,
    nome_arquivo VARCHAR(255) NOT NULL,
    url VARCHAR(500) NOT NULL,
    data_upload TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    denuncia_id INT NOT NULL,

    FOREIGN KEY (denuncia_id) REFERENCES denuncia(id)
);