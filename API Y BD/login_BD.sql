CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password TEXT NOT NULL
);

--Falta identificar bien qué tipo de variables usar
CREATE TABLE guardar_perfil (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    talla TEXT NOT NULL,
	peso TEXT NOT NULL,
	edad CHAR(2) NOT NULL,
	genero TEXT NOT NULL,
	dias_semana CHAR(1) NOT NULL,
	nivel TEXT NOT NULL,
	observaciones TEXT NOT NULL
);

INSERT INTO usuarios (username, password)
VALUES (
    'admin',
    crypt('123', gen_salt('bf'))
);