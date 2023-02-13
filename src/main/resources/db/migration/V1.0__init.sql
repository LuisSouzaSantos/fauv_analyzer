create schema analyzer;

create table analyzer.unit (
	id BIGSERIAL,
	name VARCHAR(255),
	active BOOLEAN NOT NULL, 
	PRIMARY KEY(id)
) 