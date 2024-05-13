DROP DATABASE IF EXISTS ibermatica_db;
CREATE DATABASE IF NOT EXISTS ibermatica_db;

USE ibermatica_db;

CREATE TABLE IF NOT EXISTS role (
    role_id INT(1),
    name ENUM('admin', 'occasional_employee'),
    PRIMARY KEY(role_id)
);

INSERT INTO role (role_id, name)
VALUES (0, 'admin'),
        (1, 'occasional_employee');

CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(10),
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    tlf_num INT(9) NOT NULL, -- All +34
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    type INT(1) NOT NULL, -- 0 admin / 1 occasional_employee
    PRIMARY KEY(user_id),
    FOREIGN KEY(type) REFERENCES role(role_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

INSERT INTO users (user_id, name, surname, email, tlf_num, username, password, register_date, type)
VALUES ('78565234T', 'Test_employee', 'Test_employee', 'test_employee@gmail.com', 123456789, 'test_employee', 'Admin123', '2024-13-05', 1),
        ('68565324T', 'Admin', 'Admin', 'admin@gmail.com', 987654321, 'admin', 'Admin123', '2024-13-05', 0);

CREATE TABLE IF NOT EXISTS machines (
    serial_num VARCHAR(50),
    name VARCHAR(50) NOT NULL,
    adquisition_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    type ENUM('tornos', 'embalaje', 'procesados') NOT NULL,
    status ENUM('operational', 'not_operational') NOT NULL,
    PRIMARY KEY(serial_num)
);

INSERT INTO machines (serial_num, name, adquisition_date, type, status)
VALUES ('637714620T', 'tornos1', '1995-10-10', 'tornos', 'operational'),
        ('184444622E', 'embalaje1', '2000-02-02', 'embalaje', 'operational'),
        ('294677233P', 'procesados1', '2023-06-12', 'procesados', 'operational');

CREATE TABLE IF NOT EXISTS reservation_machines (
    user_id VARCHAR(10) NOT NULL,
    serial_num VARCHAR(50),
    start_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    end_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    reservation_id INT AUTO_INCREMENT NOT NULL,
    PRIMARY KEY(reservation_id),
    FOREIGN KEY(user_id) REFERENCES users(user_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY(serial_num) REFERENCES machines(serial_num) ON DELETE RESTRICT ON UPDATE CASCADE
);

INSERT INTO reservation_machines (user_id, serial_num, start_date, end_date)
VALUES ('78565234T', '637714620T', '2024-13-05 10:00:00', '2024-14-05 08:30:00');

CREATE TABLE IF NOT EXISTS reservation_machines_cancelled (
    user_id VARCHAR(10) NOT NULL,
    serial_num VARCHAR(50),
    start_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    end_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    reservation_id INT AUTO_INCREMENT NOT NULL,
    cancelled_date DATE NOT NULL DEFAULT current_timestamp(),
    CONSTRAINT CU_reservation_machines_cancelled UNIQUE (user_id, serial_num),
    PRIMARY KEY(reservation_id)
);

CREATE TABLE IF NOT EXISTS users_deleted (
    user_id VARCHAR(10),
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    tlf_num INT(9) NOT NULL, -- All +34
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    type INT(1) NOT NULL,
    deleted_date DATE NOT NULL DEFAULT current_timestamp(), -- 0 admin / 1 occasional_employee
    PRIMARY KEY(user_id),
    FOREIGN KEY(type) REFERENCES role(role_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS breakdowns (
    serial_num VARCHAR(50),
    name VARCHAR(50) NOT NULL,
    type ENUM('tornos', 'embalaje', 'procesados') NOT NULL,
    breakdown_day DATE NOT NULL DEFAULT current_timestamp(),
    PRIMARY KEY(serial_num)
);

-----------------------------------Trigger----------------------------------------------------------
DELIMITER $$

CREATE TRIGGER `resservation_cancelled`
AFTER DELETE ON `reservation_machines`
FOR EACH ROW
BEGIN
    INSERT INTO reservation_machines_cancelled (user_id,serial_num,start_date,end_date,reservation_id) VALUES (OLD.user_id,OLD.serial_num,OLD.start_date,OLD.end_date,OLD.reservation_id);
END
$$
DELIMITER ;

DELIMITER $$

CREATE TRIGGER `user_deleted`
AFTER DELETE ON `users`
FOR EACH ROW
BEGIN
    INSERT INTO users_deleted (user_id,name,surname,email,tlf_num,username,password,register_date,type) VALUES (OLD.user_id,OLD.name,OLD.surname,OLD.email,OLD.tlf_num,OLD.username,OLD.password,OLD.register_date,OLD.type);
END
$$
DELIMITER ;



