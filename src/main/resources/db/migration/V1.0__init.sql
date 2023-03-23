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
	x numeric(9,3) NOT NULL,
	y numeric(9,3) NOT NULL,
	z numeric(9,3) NOT null,
	model_id BIGINT not null,
	PRIMARY KEY(id),
	CONSTRAINT FK_NominalPmp_Model FOREIGN KEY(model_id) REFERENCES analyzer.model(id)
);

create table analyzer.nominal_axis_coordinate (
	id BIGSERIAL,
	name VARCHAR(255) not null,
	axis VARCHAR(1) not null,
	lower_tolerance numeric(9,3) not null,
	higher_tolerance numeric(9,3) not null,
	nominal_pmp_id BIGINT not null,
	PRIMARY KEY(id),
	CONSTRAINT FK_NominalAxisCoordinate_NominalPmp FOREIGN KEY(nominal_pmp_id) REFERENCES analyzer.nominal_pmp(id)
);

create table analyzer.nominal_fm (
	id BIGSERIAL,
	name VARCHAR(255) not null,
	lower_tolerance numeric(9,3) default 0.0,
	higher_tolerance numeric(9,3) default 0.0,
	default_value numeric(9,3) default 0.0,
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

create table analyzer.sample (
	id BIGSERIAL,
	uploaded_date TIMESTAMP not null,
	uploaded_user VARCHAR(45) not null,
	model_id BIGINT not null,
	equipment_id BIGINT not null,
	pin VARCHAR(10) not null,
	scan_init_date TIMESTAMP not null,
	scan_end_date TIMESTAMP not null,
	status VARCHAR(20) not null,
	file_name VARCHAR(255) not null,
	PRIMARY KEY(id),
	CONSTRAINT FK_Sample_Model FOREIGN KEY(model_id) REFERENCES analyzer.model(id),
	CONSTRAINT FK_Sample_Equipment FOREIGN KEY(equipment_id) REFERENCES analyzer.equipment(id)
);

create table analyzer.measurement_pmp (
	id BIGSERIAL,
	nominal_pmp_id BIGINT NOT NULL,
	x numeric(9,3) NOT NULL,
	y numeric(9,3) NOT NULL,
	z numeric(9,3) NOT null,
	sample_id BIGINT not null,
	PRIMARY KEY(id),
	CONSTRAINT FK_MeasurementPmp_NominalPmp FOREIGN KEY(nominal_pmp_id) REFERENCES analyzer.nominal_pmp(id),
	CONSTRAINT FK_MeasurementPmp_Sample FOREIGN KEY(sample_id) REFERENCES analyzer.sample(id)
);

create table analyzer.measurement_axis_coordinate (
	id BIGSERIAL,
	measurement_pmp_id BIGINT NOT NULL,
	value numeric(9,3) NOT null,
	tolerance_type VARCHAR(20) NOT NULL,
	PRIMARY KEY(id),
	CONSTRAINT FK_MeasurementAxisCoordinate_MeasurementPmp FOREIGN KEY(measurement_pmp_id) REFERENCES analyzer.measurement_pmp(id)
);


create table analyzer.measurement_fm (
	id BIGSERIAL,
	nominal_fm_id BIGINT NOT NULL,
	value numeric(9,3) NOT null, 
	tolerance_type VARCHAR(20) NOT NULL,
	sample_id BIGINT not null,
	PRIMARY KEY(id),
	CONSTRAINT FK_MeasurementFm_Sample FOREIGN KEY(sample_id) REFERENCES analyzer.sample(id),
	CONSTRAINT FK_MeasurementFm_NominalFm FOREIGN KEY(nominal_fm_id) REFERENCES analyzer.nominal_fm(id)
);

create table analyzer.measurement_pmp_fm (
	measurement_pmp_id BIGINT references analyzer.measurement_pmp,
	measurement_fm_id BIGINT references analyzer.measurement_fm
);