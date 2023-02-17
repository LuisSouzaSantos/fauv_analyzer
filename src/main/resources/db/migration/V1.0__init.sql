create schema analyzer;

create table analyzer.unit (
	id BIGSERIAL,
	name VARCHAR(255),
	active BOOLEAN NOT NULL, 
	PRIMARY KEY(id)
);

create table analyzer.equipment (
	id BIGSERIAL,
	name VARCHAR(255),
	active BOOLEAN NOT NULL,
	unit_id BIGINT NOT NULL,
	PRIMARY KEY(id),
	CONSTRAINT FK_Equipment_Unit FOREIGN KEY(unit_id) REFERENCES analyzer.unit(id)
);

create table analyzer.car (
	id BIGSERIAL,
	name VARCHAR(255),
	unit_id BIGINT NOT NULL,
	PRIMARY KEY(id),
	CONSTRAINT FK_Car_Unit FOREIGN KEY(unit_id) REFERENCES analyzer.unit(id)
);