
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

INSERT INTO `todo`.`users` (`username`, `password`, `enabled`) VALUES
  ('me@robert-novak.com', '$2a$10$/omUgl/U1ca9ayva221rUe1Pd.G/tDQmuFr1lCpwRqOfDS5OHXFcq', 1),
  ('example@robert-novak.com', '2a$10$G7Tgq.z6K96uSI9kGVAWEeUQwOr/UVU1fSfPKlIbAY0N1lsxrMHcC', 1);
INSERT INTO `todo`.`authorities` (`username`, `authority`) VALUES
  ('me@robert-novak.com', 'ROLE_USER'), ('me@robert-novak.com', 'ROLE_ADMIN'),
  ('example@robert-novak.com', 'ROLE_USER');


