CREATE TABLE IF NOT EXISTS clientes (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(120) NOT NULL,
  cpf  VARCHAR(11) NOT NULL,
  data_nascimento DATE NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_cliente_cpf ON clientes(cpf);

CREATE TABLE IF NOT EXISTS cliente_enderecos (
  cliente_id BIGINT NOT NULL REFERENCES clientes(id) ON DELETE CASCADE,
  logradouro VARCHAR(150) NOT NULL,
  numero     VARCHAR(20)  NOT NULL,
  complemento VARCHAR(80),
  cep VARCHAR(15) NOT NULL,
  cidade VARCHAR(100) NOT NULL,
  uf VARCHAR(2) NOT NULL
);