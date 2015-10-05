
DROP DATABASE IF EXISTS todo;
CREATE DATABASE todo;

CREATE TABLE `todo`.`users` (
  `username` VARCHAR(60) NOT NULL UNIQUE,
  `password` VARCHAR(60) NOT NULL,
  `enabled` BOOLEAN NOT NULL DEFAULT FALSE,
  PRIMARY KEY (`username`)
) Engine = InnoDB, CHARACTER SET utf8;

CREATE TABLE `todo`.`authorities` (
  `username` VARCHAR(60) NOT NULL,
  `authority` VARCHAR(60) NOT NULL,
  FOREIGN KEY (`username`) REFERENCES `todo`.`users`(`username`),
  UNIQUE INDEX (`username`, `authority`)
) Engine = InnoDB, CHARACTER SET utf8;

CREATE TABLE `todo`.`tasks` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(60) NOT NULL,
  `completed` BOOLEAN NOT NULL DEFAULT FALSE,
  `archived` BOOLEAN NOT NULL DEFAULT FALSE,
  `title` VARCHAR(256) NOT NULL DEFAULT "",
  `details` TEXT,
  PRIMARY KEY (`id`),
  INDEX (`username`, `archived`),
  FOREIGN KEY (`username`) REFERENCES `todo`.`users`(`username`)
) Engine = InnoDB, CHARACTER SET utf8;

INSERT INTO `todo`.`users` (`username`, `password`, `enabled`) VALUES ('admin', '$2a$10$YML4a/EHXCxnAh.d8c8T6ePDRlhXbRtJVBKwbp5e6gJTl3jrnxPQm', 1);
INSERT INTO `todo`.`authorities` (`username`, `authority`) VALUES ('admin', 'ROLE_USER'), ('admin', 'ROLE_ADMIN');


