-- psql -U postgres

-- 1
CREATE DATABASE blooddo;

-- 2
CREATE USER blooddouser WITH ENCRYPTED PASSWORD 'blooddopass2';

-- 3
GRANT ALL PRIVILEGES ON DATABASE blooddo TO blooddouser;

-- \q
-- psql -d blooddo -U blooddouser
-- ===========================================================================
-- \c blooddo postgres
-- GRANT ALL ON SCHEMA public TO blooddouser;
-- \q
-- psql -d blooddo -U blooddouser
--

-- 4
CREATE TABLE users(
	username VARCHAR(50) NOT NULL PRIMARY KEY,
	password VARCHAR(256) NOT NULL,
	enabled BOOLEAN NOT NULL,
	created_by VARCHAR(50),
	ui_language VARCHAR(5) NOT NULL DEFAULT 'uk',
	pi_agreed BOOLEAN NOT NULL DEFAULT FALSE,
	CONSTRAINT fk_users_created_by FOREIGN KEY(created_by) REFERENCES users(username)
);

-- 5 
CREATE TABLE authorities (
	username VARCHAR(50) NOT NULL,
	authority VARCHAR(128) NOT NULL,
	CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username)
);

-- 6 
CREATE UNIQUE INDEX ix_auth_username ON authorities (username,authority);

-- 7
-- User admin/pass
INSERT INTO users (username, password, enabled)
	VALUES ('admin',
		'$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a',
		TRUE);

-- 8
INSERT INTO authorities (username, authority)
	VALUES ('admin', 'ROLE_ADMIN');

-- for testing users
INSERT INTO users (username, password, enabled)
	VALUES ('donor',
		'$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a',
		TRUE);
INSERT INTO authorities (username, authority)
	VALUES ('donor', 'ROLE_DONOR');
INSERT INTO users (username, password, enabled)
	VALUES ('medic',
		'$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a',
		TRUE);
INSERT INTO authorities (username, authority)
	VALUES ('medic', 'ROLE_MEDIC');


-- 9
CREATE TABLE donor_data (
	id SERIAL NOT NULL PRIMARY KEY,
	first_name VARCHAR(256) NOT NULL,
	last_name VARCHAR(256) NOT NULL,
	blood_type VARCHAR(2) NOT NULL,
	rhesus_factor BOOLEAN NOT NULL,
	phone_number VARCHAR(12) NOT NULL,
	username VARCHAR(50) NOT NULL UNIQUE,
	CONSTRAINT fk_donor_data_username FOREIGN KEY(username) REFERENCES users(username)
);

-- 10
CREATE TABLE donation_event (
	id SERIAL NOT NULL PRIMARY KEY,
	date_time TIMESTAMP NOT NULL,
	event_address VARCHAR(512) NOT NULL,
	event_status VARCHAR(24) NOT NULL DEFAULT 'PLANNED',
	notes VARCHAR(512),
	created_by VARCHAR(50),
	CONSTRAINT fk_donation_event_created_by FOREIGN KEY(created_by) REFERENCES users(username)
);

-- 11
CREATE TABLE donation (
	id SERIAL NOT NULL PRIMARY KEY,
	donor_id INT,
	event_id INT NOT NULL,
	created_by VARCHAR(50),
	CONSTRAINT fk_donation_donor FOREIGN KEY(donor_id) REFERENCES donor_data(id),
	CONSTRAINT fk_donation_event FOREIGN KEY(event_id) REFERENCES donation_event(id),
	CONSTRAINT fk_donation_created_by FOREIGN KEY(created_by) REFERENCES users(username)
);

-- 12
CREATE TABLE fridge (
	id SERIAL NOT NULL PRIMARY KEY,
	serial_number VARCHAR(32) NOT NULL,
	fridge_address VARCHAR(512) NOT NULL,
	notes VARCHAR(521),
	temp_celsius_min REAL,
	temp_celsius_max REAL,
	humidity_percent_min REAL,
	humidity_percent_max REAL,
	created_by VARCHAR(50),
	enabled BOOLEAN NOT NULL DEFAULT TRUE,
	CONSTRAINT fk_fridge_created_by FOREIGN KEY(created_by) REFERENCES users(username)
);

-- 13
CREATE TABLE blood (
	id SERIAL NOT NULL PRIMARY KEY,
	blood_type VARCHAR(2) NOT NULL,
	rhesus_factor BOOLEAN NOT NULL,
	spoiled BOOLEAN NOT NULL DEFAULT FALSE,
	barcode VARCHAR(12) NOT NULL,
	use_status VARCHAR(24) NOT NULL DEFAULT 'AVAILABLE',
	created_by VARCHAR(50),
	donation_id INT NOT NULL,
	fridge_id INT NOT NULL,
	CONSTRAINT fk_blood_created_by FOREIGN KEY(created_by) REFERENCES users(username),
	CONSTRAINT fk_blood_donation FOREIGN KEY(donation_id) REFERENCES donation(id),
	CONSTRAINT fk_blood_fridge FOREIGN KEY(fridge_id) REFERENCES fridge(id)
);

-- 14
CREATE TABLE fridge_metrics (
	id SERIAL NOT NULL PRIMARY KEY,
	temp_celsius REAL NOT NULL,
	humidity_percent REAL NOT NULL,
	fridge_id INT NOT NULL,
	date_time TIMESTAMP NOT NULL,
	CONSTRAINT fk_fridge_metrics_fridge FOREIGN KEY(fridge_id) REFERENCES fridge(id)
);

-- 15
CREATE TABLE hospital (
	id SERIAL NOT NULL PRIMARY KEY,
	name VARCHAR(256) NOT NULL,
	hospital_address VARCHAR(512) NOT NULL
);

-- 16
CREATE TABLE medic_user (
	id SERIAL NOT NULL PRIMARY KEY,
	first_name VARCHAR(256) NOT NULL,
	last_name VARCHAR(256) NOT NULL,
	phone_number VARCHAR(12) NOT NULL,
	hospital_id INT NOT NULL,
	username VARCHAR(50) NOT NULL UNIQUE,
	CONSTRAINT fk_medic_user_hospital FOREIGN KEY(hospital_id) REFERENCES hospital(id),
	CONSTRAINT fk_medic_user_username FOREIGN KEY(username) REFERENCES users(username)
);

-- 17
CREATE TABLE blood_needs (
	id SERIAL NOT NULL PRIMARY KEY,
	hospital_id INT NOT NULL UNIQUE,
	o_negative REAL NOT NULL DEFAULT 0.0,
	o_positive REAL NOT NULL DEFAULT 0.0,
	a_negative REAL NOT NULL DEFAULT 0.0,
	a_positive REAL NOT NULL DEFAULT 0.0,
	b_negative REAL NOT NULL DEFAULT 0.0,
	b_positive REAL NOT NULL DEFAULT 0.0,
	ab_negative REAL NOT NULL DEFAULT 0.0,
	ab_positive REAL NOT NULL DEFAULT 0.0,
	CONSTRAINT fk_blood_needs_hospital FOREIGN KEY(hospital_id) REFERENCES hospital(id)
);

-- 18
CREATE TABLE notifications (
	id SERIAL NOT NULL PRIMARY KEY,
	target_user VARCHAR(50) NOT NULL,
	created_by VARCHAR(50),
	notification_header VARCHAR(128) NOT NULL,
	notification_body VARCHAR(512),
	delivered BOOLEAN NOT NULL DEFAULT FALSE,
	CONSTRAINT fk_notifications_target_user FOREIGN KEY(target_user) REFERENCES users(username),
	CONSTRAINT fk_notifications_created_by FOREIGN KEY(target_user) REFERENCES users(username)
);

-- 19
CREATE TABLE donation_appointments (
	id SERIAL NOT NULL PRIMARY KEY,
	donor_id INT NOT NULL,
	donation_event_id INT NOT NULL,
	CONSTRAINT fk_donation_appointments_donor FOREIGN KEY(donor_id) REFERENCES donor_data(id),
	CONSTRAINT fk_donation_appointments_donation_event FOREIGN KEY(donation_event_id) REFERENCES donation_event(id)
);
