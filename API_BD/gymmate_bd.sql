CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE perfil (
    id SERIAL PRIMARY KEY,
	usuario_id INTEGER NOT NULL REFERENCES usuarios(id),
    nombre VARCHAR(50) NOT NULL,
    edad smallint NOT NULL,
	peso decimal(10,2) NOT NULL,
	talla decimal(10,2) NOT NULL,
	genero TEXT NOT NULL,
	meta TEXT NOT NULL,
	dias_gym smallint NOT NULL,
	nivel TEXT NOT NULL,
	observaciones TEXT NULL
);

CREATE TABLE plan_entrenamiento (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER NOT NULL REFERENCES usuarios(id),
    rutina JSONB NOT NULL,
    nutricion TEXT NOT NULL
);


INSERT INTO usuarios (username, password)
VALUES (
    'admin',
    crypt('123', gen_salt('bf'))
);