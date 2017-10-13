
-- criando usuários
INSERT INTO usuario(email, nome, tipo_id) VALUES ('natarajan@gmail.com','Natarajan Rodrigues', 2);
INSERT INTO usuario(email, nome, tipo_id) VALUES ('nabila@gmail.com','Nabila Rodrigues', 1);
INSERT INTO usuario(email, nome, tipo_id) VALUES ('aristofanio@gmail.com','Ari Garcia', 1);

-- criando tickets inativos
INSERT INTO ticket(createdin, updatedin, resume, status, tipo, usuario_id) VALUES (timestamp '09-25-2017 16:20', timestamp '10-01-2017 17:25', 'exemplo de ticket inativo', 1, 1, 2 );
