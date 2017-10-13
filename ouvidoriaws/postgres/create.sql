CREATE TABLE usuario (
	--User data
	id SERIAL,
	email VARCHAR(255) NOT NULL,
	nome VARCHAR(255) NOT NULL,
	tipo_id INT NOT NULL,
	--PK
	PRIMARY KEY(id)
);


CREATE TABLE ticket(
    id serial,
    createdin timestamp not null,
    updatedin timestamp not null,
    resume character varying(255),
    status integer,
    tipo integer,
  
    usuario_id bigint not null,
    primary key (id)
);

CREATE TABLE mensagem (
  id serial,
  text character varying(255),
  from_id bigint not null,
  ticket_id bigint not null,
  PRIMARY KEY (id),
  FOREIGN KEY (from_id) REFERENCES usuario (id)
);