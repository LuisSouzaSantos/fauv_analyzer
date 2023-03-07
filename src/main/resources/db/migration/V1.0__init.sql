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
	active BOOLEAN NOT NULL,
	unit_id BIGINT NOT NULL,
	PRIMARY KEY(id),
	CONSTRAINT FK_Car_Unit FOREIGN KEY(unit_id) REFERENCES analyzer.unit(id)
);


create table analyzer.model (
	id BIGSERIAL,
	part_number VARCHAR(255) not null,
	car_id BIGINT not null,
	step_description VARCHAR(255) not null,
	PRIMARY KEY(id),
	CONSTRAINT FK_Model_Car FOREIGN KEY(car_id) REFERENCES analyzer.car(id)
);

create table analyzer.nominal_pmp (
	id BIGSERIAL,
	name VARCHAR(255) not null,
	axis VARCHAR(1) not null,
	active BOOLEAN not null,
	x decimal(9,3) NOT NULL,
	y decimal(9,3) NOT NULL,
	z decimal(9,3) NOT null,
	model_id BIGINT not null,
	PRIMARY KEY(id),
	CONSTRAINT FK_NominalPmp_Model FOREIGN KEY(model_id) REFERENCES analyzer.model(id)
);

create table analyzer.nominal_axis_coordinate (
	id BIGSERIAL,
	name VARCHAR(255) not null,
	axis VARCHAR(1) not null,
	lower_tolerance decimal(9,3) not null,
	higher_tolerance decimal(9,3) not null,
	nominal_pmp_id BIGINT not null,
	PRIMARY KEY(id),
	CONSTRAINT FK_NominalAxisCoordinate_NominalPmp FOREIGN KEY(nominal_pmp_id) REFERENCES analyzer.nominal_pmp(id)
);

create table analyzer.nominal_fm (
	id BIGSERIAL,
	name VARCHAR(255) not null,
	lower_tolerance decimal(9,3) default 0.0,
	higher_tolerance decimal(9,3) default 0.0,
	default_value decimal(9,3) default 0.0,
	axis VARCHAR(1) not null,
	catalog_type VARCHAR(255) not null,
	level VARCHAR(255) not null,
	active BOOLEAN not null,
	model_id BIGINT not null,
	PRIMARY KEY(id),
	CONSTRAINT FK_NominalFm_Model FOREIGN KEY(model_id) REFERENCES analyzer.model(id)
);

create table analyzer.fm_impact ( 
	id BIGSERIAL,
	info VARCHAR(255) not null,
	nominal_fm_id BIGINT not null,
	PRIMARY KEY(id),
	CONSTRAINT FK_FmImpact_NominalFm FOREIGN KEY(nominal_fm_id) REFERENCES analyzer.nominal_fm(id)
);

create table analyzer.nominal_pmp_fm (
	nominal_pmp_id BIGINT references analyzer.nominal_pmp,
	nominal_fm_id BIGINT references analyzer.nominal_fm
);