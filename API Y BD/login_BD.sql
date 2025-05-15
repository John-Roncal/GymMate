CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password TEXT NOT NULL
);

INSERT INTO usuarios (username, password)
VALUES (
    'john',
    crypt('123', gen_salt('bf'))
);

SELECT * FROM usuarios 