DROP DATABASE `external_database_test`;
DROP DATABASE `internal_database_test`;

CREATE SCHEMA `external_database_test`;
CREATE SCHEMA `internal_database_test`;

CREATE TABLE `external_database_test`.`FIELD_OF_STUDY` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `name` VARCHAR(63) NOT NULL COMMENT '',
  `abbreviation` VARCHAR(31) NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  UNIQUE INDEX `name_UNIQUE` (`name` ASC)  COMMENT '',
  UNIQUE INDEX `abbreviation_UNIQUE` (`abbreviation` ASC)  COMMENT '');

CREATE TABLE `external_database_test`.`COURSE` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `name` VARCHAR(63) NOT NULL COMMENT '',
  `abbreviation` VARCHAR(31) NOT NULL COMMENT '',
  `semester` INT NULL COMMENT '',
  `min_team_size` INT NOT NULL COMMENT '',
  `max_team_size` INT NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  UNIQUE INDEX `name_UNIQUE` (`name` ASC)  COMMENT '',
  UNIQUE INDEX `abbreviation_UNIQUE` (`abbreviation` ASC)  COMMENT '');

CREATE TABLE `external_database_test`.`COURSE_TO_FIELD_OF_STUDY` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `course_id` INT NOT NULL COMMENT '',
  `field_of_study_id` INT NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `course_id_idx` (`course_id` ASC)  COMMENT '',
  INDEX `field_of_study_id_idx` (`field_of_study_id` ASC)  COMMENT '',
  UNIQUE KEY `course_id__field_of_study_id_UNIQUE` (`course_id`, `field_of_study_id`),
  CONSTRAINT `course_id_course_to_field_of_study`
    FOREIGN KEY (`course_id`)
    REFERENCES `external_database_test`.`COURSE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `field_of_study_id_course_to_field_of_study`
    FOREIGN KEY (`field_of_study_id`)
    REFERENCES `external_database_test`.`FIELD_OF_STUDY` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `external_database_test`.`COURSE_GROUP` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `course_id` INT NOT NULL COMMENT '',
  `group_number` INT NOT NULL COMMENT '',
  `max_group_size` INT NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `course_id_idx` (`course_id` ASC)  COMMENT '',
  CONSTRAINT `course_id_course_group`
    FOREIGN KEY (`course_id`)
    REFERENCES `external_database_test`.`COURSE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `external_database_test`.`COURSE_GROUP_APPOINTMENT` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `course_group_id` INT NOT NULL COMMENT '',
  `start_date` DATETIME NOT NULL COMMENT '',
  `end_date` DATETIME NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `course_group_id_idx` (`course_group_id` ASC)  COMMENT '',
  UNIQUE KEY `course_group_id__start_date_UNIQUE` (`course_group_id`, `start_date`),
  UNIQUE KEY `course_group_id__end_date_UNIQUE` (`course_group_id`, `end_date`),
  CONSTRAINT `course_group_id_appointment`
    FOREIGN KEY (`course_group_id`)
    REFERENCES `external_database_test`.`COURSE_GROUP` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `external_database_test`.`COURSE_LECTURE` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `course_id` INT NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `course_id_idx` (`course_id` ASC)  COMMENT '',
  CONSTRAINT `course_id_course_lecture`
    FOREIGN KEY (`course_id`)
    REFERENCES `external_database_test`.`COURSE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `external_database_test`.`COURSE_LECTURE_APPOINTMENT` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `course_lecture_id` INT NOT NULL COMMENT '',
  `start_date` DATETIME NOT NULL COMMENT '',
  `end_date` DATETIME NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `course_lecture_id_idx` (`course_lecture_id` ASC)  COMMENT '',
  UNIQUE KEY `course_lecture_id__start_date_UNIQUE` (`course_lecture_id`, `start_date`),
  UNIQUE KEY `course_lecture_id__end_date_UNIQUE` (`course_lecture_id`, `end_date`),
  CONSTRAINT `course_lecture_id_appointment`
    FOREIGN KEY (`course_lecture_id`)
    REFERENCES `external_database_test`.`COURSE_LECTURE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `external_database_test`.`STUDENT` (
  `student_number` VARCHAR(31) NOT NULL COMMENT '',
  `first_name` VARCHAR(63) NOT NULL COMMENT '',
  `last_name` VARCHAR(63) NOT NULL COMMENT '',
  `email` VARCHAR(255) NOT NULL COMMENT '',
  `field_of_study_id` INT NOT NULL COMMENT '',
  `semester` INT NOT NULL COMMENT '',
  PRIMARY KEY (`student_number`)  COMMENT '',
  UNIQUE INDEX `student_number_UNIQUE` (`student_number` ASC)  COMMENT '',
  UNIQUE INDEX `email_UNIQUE` (`email` ASC)  COMMENT '',
  INDEX `field_of_study_id_idx` (`field_of_study_id` ASC)  COMMENT '',
  CONSTRAINT `field_of_study_id_student`
    FOREIGN KEY (`field_of_study_id`)
    REFERENCES `external_database_test`.`FIELD_OF_STUDY` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `external_database_test`.`COMPLETED_COURSE` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `student_number` VARCHAR(31) NOT NULL COMMENT '',
  `course_id` INT NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `course_id_idx` (`course_id` ASC)  COMMENT '',
  INDEX `student_number_idx` (`student_number` ASC)  COMMENT '',
  CONSTRAINT `course_id_completed_course`
    FOREIGN KEY (`course_id`)
    REFERENCES `external_database_test`.`COURSE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `student_number_completed_course`
    FOREIGN KEY (`student_number`)
    REFERENCES `external_database_test`.`STUDENT` (`student_number`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `internal_database_test`.`COURSE_REGISTRATION` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `student_number` VARCHAR(31) NOT NULL COMMENT '',
  `course_id` INT NOT NULL COMMENT '',
  `currently_assigned_to_team` BIT NOT NULL DEFAULT 0 COMMENT '',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  UNIQUE KEY `student_number__course_id_UNIQUE` (`student_number`, `course_id`),
  INDEX `student_number_idx` (`student_number` ASC)  COMMENT '',
  INDEX `course_id_idx` (`course_id` ASC)  COMMENT '');

CREATE TABLE `internal_database_test`.`TEAM` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `course_id` INT NOT NULL COMMENT '',
  `created_by_student_number` VARCHAR(31) NOT NULL COMMENT '',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `course_id_idx` (`course_id` ASC)  COMMENT '',
  INDEX `created_by_student_number_idx` (`created_by_student_number` ASC)  COMMENT '');

CREATE TABLE `internal_database_test`.`TEAM_REGISTRATION` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `student_number` VARCHAR(31) NOT NULL COMMENT '',
  `team_id` INT NOT NULL COMMENT '',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `student_number_idx` (`student_number` ASC)  COMMENT '',
  INDEX `team_id_idx` (`team_id` ASC)  COMMENT '',
  UNIQUE KEY `student_number__team_id_UNIQUE` (`student_number`, `team_id`),
  CONSTRAINT `team_id_team_registration`
    FOREIGN KEY (`team_id`)
    REFERENCES `internal_database_test`.`TEAM` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `internal_database_test`.`TEAM_INVITATION` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `invitee_student_number` VARCHAR(31) NOT NULL COMMENT '',
  `team_id` INT NOT NULL COMMENT '',
  `created_by_student_number` VARCHAR(31) NOT NULL COMMENT '',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `invitee_student_number_idx` (`invitee_student_number` ASC)  COMMENT '',
  INDEX `team_id_idx` (`team_id` ASC)  COMMENT '',
  INDEX `created_by_student_number_idx` (`created_by_student_number` ASC)  COMMENT '',
  UNIQUE KEY `invitee_student_number__team_id_UNIQUE` (`invitee_student_number`, `team_id`),
  CONSTRAINT `team_id_team_invitation`
    FOREIGN KEY (`team_id`)
    REFERENCES `internal_database_test`.`TEAM` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `internal_database_test`.`MEMBERSHIP_REQUEST` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `applicant_student_number` VARCHAR(31) NOT NULL COMMENT '',
  `team_id` INT NOT NULL COMMENT '',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `applicant_student_number_idx` (`applicant_student_number` ASC)  COMMENT '',
  INDEX `team_id_idx` (`team_id` ASC)  COMMENT '',
  UNIQUE KEY `applicant_student_number__team_id_UNIQUE` (`applicant_student_number`, `team_id`),
  CONSTRAINT `team_id_membership_request`
    FOREIGN KEY (`team_id`)
    REFERENCES `internal_database_test`.`TEAM` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



-- Create external test data
INSERT INTO `external_database_test`.`FIELD_OF_STUDY` (`name`, `abbreviation`) VALUES ('Bachelor Angewandte Infomatik', 'B-AI');
INSERT INTO `external_database_test`.`COURSE` (`name`, `abbreviation`, `semester`, `min_team_size`, `max_team_size`) VALUES ('Datenbanken', 'DB', '2', '2', '2');
INSERT INTO `external_database_test`.`COURSE` (`name`, `abbreviation`, `semester`, `min_team_size`, `max_team_size`) VALUES ('Logik und Berechenbarkeit', 'LB', '2', '2', '2');
INSERT INTO `external_database_test`.`COURSE` (`name`, `abbreviation`, `semester`, `min_team_size`, `max_team_size`) VALUES ('Betriebssysteme', 'BS', '3', '2', '2');
INSERT INTO `external_database_test`.`COURSE` (`name`, `abbreviation`, `semester`, `min_team_size`, `max_team_size`) VALUES ('Rechnernetze', 'RN', '4', '2', '2');
INSERT INTO `external_database_test`.`COURSE_TO_FIELD_OF_STUDY` (`course_id`, `field_of_study_id`) VALUES ('1', '1');
INSERT INTO `external_database_test`.`COURSE_TO_FIELD_OF_STUDY` (`course_id`, `field_of_study_id`) VALUES ('2', '1');
INSERT INTO `external_database_test`.`COURSE_TO_FIELD_OF_STUDY` (`course_id`, `field_of_study_id`) VALUES ('3', '1');
INSERT INTO `external_database_test`.`COURSE_TO_FIELD_OF_STUDY` (`course_id`, `field_of_study_id`) VALUES ('4', '1');
INSERT INTO `external_database_test`.`COURSE_GROUP` (`course_id`, `group_number`, `max_group_size`) VALUES ('1', '1', '30');
INSERT INTO `external_database_test`.`COURSE_GROUP` (`course_id`, `group_number`, `max_group_size`) VALUES ('1', '2', '30');
INSERT INTO `external_database_test`.`COURSE_GROUP` (`course_id`, `group_number`, `max_group_size`) VALUES ('2', '1', '30');
INSERT INTO `external_database_test`.`COURSE_GROUP` (`course_id`, `group_number`, `max_group_size`) VALUES ('2', '2', '30');
INSERT INTO `external_database_test`.`COURSE_GROUP` (`course_id`, `group_number`, `max_group_size`) VALUES ('3', '1', '30');
INSERT INTO `external_database_test`.`COURSE_GROUP` (`course_id`, `group_number`, `max_group_size`) VALUES ('3', '2', '30');
INSERT INTO `external_database_test`.`COURSE_GROUP` (`course_id`, `group_number`, `max_group_size`) VALUES ('4', '1', '30');
INSERT INTO `external_database_test`.`COURSE_GROUP` (`course_id`, `group_number`, `max_group_size`) VALUES ('4', '2', '30');
INSERT INTO `external_database_test`.`COURSE_GROUP_APPOINTMENT` (`course_group_id`, `start_date`, `end_date`) VALUES ('1', '2015-10-06 08:15:00', '2015-10-06 11:30:00');
INSERT INTO `external_database_test`.`COURSE_GROUP_APPOINTMENT` (`course_group_id`, `start_date`, `end_date`) VALUES ('1', '2015-10-27 08:15:00', '2015-10-27 11:30:00');
INSERT INTO `external_database_test`.`COURSE_GROUP_APPOINTMENT` (`course_group_id`, `start_date`, `end_date`) VALUES ('2', '2015-10-15 08:15:00', '2015-10-15 11:30:00');
INSERT INTO `external_database_test`.`COURSE_GROUP_APPOINTMENT` (`course_group_id`, `start_date`, `end_date`) VALUES ('2', '2015-10-29 08:15:00', '2015-10-29 11:30:00');
INSERT INTO `external_database_test`.`COURSE_GROUP_APPOINTMENT` (`course_group_id`, `start_date`, `end_date`) VALUES ('3', '2015-10-14 12:30:00', '2015-10-14 15:45:00');
INSERT INTO `external_database_test`.`COURSE_GROUP_APPOINTMENT` (`course_group_id`, `start_date`, `end_date`) VALUES ('3', '2015-10-28 12:30:00', '2015-10-28 15:45:00');
INSERT INTO `external_database_test`.`COURSE_GROUP_APPOINTMENT` (`course_group_id`, `start_date`, `end_date`) VALUES ('4', '2015-10-13 08:15:00', '2015-10-13 11:30:00');
INSERT INTO `external_database_test`.`COURSE_GROUP_APPOINTMENT` (`course_group_id`, `start_date`, `end_date`) VALUES ('4', '2015-11-03 08:15:00', '2015-11-03 11:30:00');
INSERT INTO `external_database_test`.`COURSE_GROUP_APPOINTMENT` (`course_group_id`, `start_date`, `end_date`) VALUES ('5', '2015-10-22 08:15:00', '2015-10-22 11:30:00');
INSERT INTO `external_database_test`.`COURSE_GROUP_APPOINTMENT` (`course_group_id`, `start_date`, `end_date`) VALUES ('5', '2015-11-05 08:15:00', '2015-11-05 11:30:00');
INSERT INTO `external_database_test`.`COURSE_GROUP_APPOINTMENT` (`course_group_id`, `start_date`, `end_date`) VALUES ('6', '2015-10-21 12:30:00', '2015-10-21 15:45:00');
INSERT INTO `external_database_test`.`COURSE_GROUP_APPOINTMENT` (`course_group_id`, `start_date`, `end_date`) VALUES ('6', '2015-11-04 12:30:00', '2015-11-04 15:45:00');
INSERT INTO `external_database_test`.`COURSE_GROUP_APPOINTMENT` (`course_group_id`, `start_date`, `end_date`) VALUES ('7', '2015-10-14 12:30:00', '2015-10-14 14:00:00');
INSERT INTO `external_database_test`.`COURSE_GROUP_APPOINTMENT` (`course_group_id`, `start_date`, `end_date`) VALUES ('7', '2015-10-28 12:30:00', '2015-10-28 14:00:00');
INSERT INTO `external_database_test`.`COURSE_GROUP_APPOINTMENT` (`course_group_id`, `start_date`, `end_date`) VALUES ('8', '2015-10-14 14:00:00', '2015-10-14 15:30:00');
INSERT INTO `external_database_test`.`COURSE_GROUP_APPOINTMENT` (`course_group_id`, `start_date`, `end_date`) VALUES ('8', '2015-10-28 14:00:00', '2015-10-28 15:30:00');
INSERT INTO `external_database_test`.`COURSE_LECTURE` (`course_id`) VALUES ('1');
INSERT INTO `external_database_test`.`COURSE_LECTURE` (`course_id`) VALUES ('2');
INSERT INTO `external_database_test`.`COURSE_LECTURE` (`course_id`) VALUES ('3');
INSERT INTO `external_database_test`.`COURSE_LECTURE` (`course_id`) VALUES ('4');
INSERT INTO `external_database_test`.`COURSE_LECTURE_APPOINTMENT` (`course_lecture_id`, `start_date`, `end_date`) VALUES ('1', '2015-10-25 08:15:00', '2015-10-25 11:30:00');
INSERT INTO `external_database_test`.`COURSE_LECTURE_APPOINTMENT` (`course_lecture_id`, `start_date`, `end_date`) VALUES ('1', '2015-11-15 08:15:00', '2015-11-15 11:30:00');
INSERT INTO `external_database_test`.`COURSE_LECTURE_APPOINTMENT` (`course_lecture_id`, `start_date`, `end_date`) VALUES ('1', '2015-12-06 08:15:00', '2015-12-06 11:30:00');
INSERT INTO `external_database_test`.`COURSE_LECTURE_APPOINTMENT` (`course_lecture_id`, `start_date`, `end_date`) VALUES ('1', '2016-01-08 08:15:00', '2016-01-08 11:30:00');
INSERT INTO `external_database_test`.`COURSE_LECTURE_APPOINTMENT` (`course_lecture_id`, `start_date`, `end_date`) VALUES ('2', '2015-10-21 14:00:00', '2015-10-21 15:30:00');
INSERT INTO `external_database_test`.`COURSE_LECTURE_APPOINTMENT` (`course_lecture_id`, `start_date`, `end_date`) VALUES ('2', '2015-11-04 14:00:00', '2015-11-04 15:30:00');
INSERT INTO `external_database_test`.`COURSE_LECTURE_APPOINTMENT` (`course_lecture_id`, `start_date`, `end_date`) VALUES ('2', '2015-11-18 14:00:00', '2015-11-18 15:30:00');
INSERT INTO `external_database_test`.`COURSE_LECTURE_APPOINTMENT` (`course_lecture_id`, `start_date`, `end_date`) VALUES ('2', '2015-12-02 14:00:00', '2015-12-02 15:30:00');
INSERT INTO `external_database_test`.`COURSE_LECTURE_APPOINTMENT` (`course_lecture_id`, `start_date`, `end_date`) VALUES ('3', '2015-10-26 08:15:00', '2015-10-26 11:30:00');
INSERT INTO `external_database_test`.`COURSE_LECTURE_APPOINTMENT` (`course_lecture_id`, `start_date`, `end_date`) VALUES ('3', '2015-11-16 08:15:00', '2015-11-16 11:30:00');
INSERT INTO `external_database_test`.`COURSE_LECTURE_APPOINTMENT` (`course_lecture_id`, `start_date`, `end_date`) VALUES ('3', '2015-12-07 08:15:00', '2015-12-07 11:30:00');
INSERT INTO `external_database_test`.`COURSE_LECTURE_APPOINTMENT` (`course_lecture_id`, `start_date`, `end_date`) VALUES ('3', '2016-01-09 08:15:00', '2016-01-09 11:30:00');
INSERT INTO `external_database_test`.`COURSE_LECTURE_APPOINTMENT` (`course_lecture_id`, `start_date`, `end_date`) VALUES ('4', '2015-10-25 08:15:00', '2015-10-25 11:30:00');
INSERT INTO `external_database_test`.`COURSE_LECTURE_APPOINTMENT` (`course_lecture_id`, `start_date`, `end_date`) VALUES ('4', '2015-11-15 08:15:00', '2015-11-15 11:30:00');
INSERT INTO `external_database_test`.`COURSE_LECTURE_APPOINTMENT` (`course_lecture_id`, `start_date`, `end_date`) VALUES ('4', '2015-12-06 08:15:00', '2015-12-06 11:30:00');
INSERT INTO `external_database_test`.`COURSE_LECTURE_APPOINTMENT` (`course_lecture_id`, `start_date`, `end_date`) VALUES ('4', '2016-01-08 08:15:00', '2016-01-08 11:30:00');
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055120','Ralf','Stoeckmann','ralf.stoeckmann@haw-hamburg.de',1,2);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055121','Gordon','Dohrmann','gordon.dohrmann@haw-hamburg.de',1,2);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055129','Lokal','Horst','lokal.horst@haw-hamburg.de',1,2);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055140','Marie Sophie','Einhorn','marie-sophie.einhorn@haw-hamburg.de',1,2);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055143','Klara','Fall','klara.fall@haw-hamburg.de',1,2);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055153','Dominik','Koelzer','dominik.koelzer@haw-hamburg.de',1,2);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055163','Angela','Merkel','angela.merkel@haw-hamburg.de',1,2);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055173','Lukas','Wewer','lukas.wewer@haw-hamburg.de',1,2);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055174','Alexis','Hechler','alexis.hechler@haw-hamburg.de',1,2);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055176','Marko','Henningsen','marko.henningsen@haw-hamburg.de',1,2);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055178','Steven','Alaei Mofrad','steven.alaei-mofrad@haw-hamburg.de',1,2);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055192','Jonas','Dariti','jonas.dariti@haw-hamburg.de',1,2);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055195','Joshua','Demirel','joshua.demirel@haw-hamburg.de',1,3);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055197','Martin','Hedhli','martin.hedhli@haw-hamburg.de',1,3);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055205','Tim','Prigge','tim.prigge@haw-hamburg.de',1,3);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055212','Rainer','Zufall','rainer.zufall@haw-hamburg.de',1,3);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055215','Jesper','Hinrichs','jesper.hinrichs@haw-hamburg.de',1,4);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055220','Nikolay Nicolaev','Katze','nikolay-nicolaev.katze@haw-hamburg.de',1,4);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055221','Bastian','Osman','bastian.osman@haw-hamburg.de',1,4);
INSERT INTO `external_database_test`.`STUDENT` (`student_number`,`first_name`,`last_name`,`email`,`field_of_study_id`,`semester`) VALUES ('2055223','Joern','Truempelmann','joern.truempelmann@haw-hamburg.de',1,4);
INSERT INTO `external_database_test`.`COMPLETED_COURSE` (`student_number`, `course_id`) VALUES ('2055195', '1');
INSERT INTO `external_database_test`.`COMPLETED_COURSE` (`student_number`, `course_id`) VALUES ('2055195', '2');
INSERT INTO `external_database_test`.`COMPLETED_COURSE` (`student_number`, `course_id`) VALUES ('2055197', '1');
INSERT INTO `external_database_test`.`COMPLETED_COURSE` (`student_number`, `course_id`) VALUES ('2055197', '2');
INSERT INTO `external_database_test`.`COMPLETED_COURSE` (`student_number`, `course_id`) VALUES ('2055205', '1');
INSERT INTO `external_database_test`.`COMPLETED_COURSE` (`student_number`, `course_id`) VALUES ('2055212', '1');
INSERT INTO `external_database_test`.`COMPLETED_COURSE` (`student_number`, `course_id`) VALUES ('2055212', '2');
INSERT INTO `external_database_test`.`COMPLETED_COURSE` (`student_number`, `course_id`) VALUES ('2055215', '1');
INSERT INTO `external_database_test`.`COMPLETED_COURSE` (`student_number`, `course_id`) VALUES ('2055215', '2');
INSERT INTO `external_database_test`.`COMPLETED_COURSE` (`student_number`, `course_id`) VALUES ('2055215', '3');
INSERT INTO `external_database_test`.`COMPLETED_COURSE` (`student_number`, `course_id`) VALUES ('2055220', '1');
INSERT INTO `external_database_test`.`COMPLETED_COURSE` (`student_number`, `course_id`) VALUES ('2055220', '2');
INSERT INTO `external_database_test`.`COMPLETED_COURSE` (`student_number`, `course_id`) VALUES ('2055220', '3');
INSERT INTO `external_database_test`.`COMPLETED_COURSE` (`student_number`, `course_id`) VALUES ('2055221', '1');
INSERT INTO `external_database_test`.`COMPLETED_COURSE` (`student_number`, `course_id`) VALUES ('2055221', '2');
INSERT INTO `external_database_test`.`COMPLETED_COURSE` (`student_number`, `course_id`) VALUES ('2055223', '1');
INSERT INTO `external_database_test`.`COMPLETED_COURSE` (`student_number`, `course_id`) VALUES ('2055223', '2');
INSERT INTO `external_database_test`.`COMPLETED_COURSE` (`student_number`, `course_id`) VALUES ('2055223', '3');

-- Create internal test data
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055120', '1', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055120', '2', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055121', '1', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055121', '2', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055129', '1', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055140', '1', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055140', '2', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055143', '1', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055143', '2', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055153', '1', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055153', '2', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055163', '1', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055163', '2', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055163', '3', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055174', '1', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055176', '1', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055176', '2', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055178', '1', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055178', '2', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055192', '1', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055192', '2', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055195', '3', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055197', '3', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055205', '3', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055212', '3', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055215', '3', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055215', '4', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055220', '4', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055221', '4', 0);
INSERT INTO `internal_database_test`.`COURSE_REGISTRATION` (`student_number`, `course_id`, `currently_assigned_to_team`) VALUES ('2055223', '4', 0);
INSERT INTO `internal_database_test`.`TEAM` (`course_id`, `created_by_student_number`) VALUES (1, '2055120');
INSERT INTO `internal_database_test`.`TEAM` (`course_id`, `created_by_student_number`) VALUES (2, '2055120');
INSERT INTO `internal_database_test`.`TEAM` (`course_id`, `created_by_student_number`) VALUES (2, '2055121');
INSERT INTO `internal_database_test`.`TEAM` (`course_id`, `created_by_student_number`) VALUES (1, '2055129');
INSERT INTO `internal_database_test`.`TEAM` (`course_id`, `created_by_student_number`) VALUES (2, '2055140');
INSERT INTO `internal_database_test`.`TEAM` (`course_id`, `created_by_student_number`) VALUES (1, '2055176');
INSERT INTO `internal_database_test`.`TEAM` (`course_id`, `created_by_student_number`) VALUES (1, '2055143');
INSERT INTO `internal_database_test`.`TEAM` (`course_id`, `created_by_student_number`) VALUES (2, '2055143');
INSERT INTO `internal_database_test`.`TEAM` (`course_id`, `created_by_student_number`) VALUES (1, '2055163');
INSERT INTO `internal_database_test`.`TEAM` (`course_id`, `created_by_student_number`) VALUES (2, '2055163');
INSERT INTO `internal_database_test`.`TEAM` (`course_id`, `created_by_student_number`) VALUES (3, '2055163');
INSERT INTO `internal_database_test`.`TEAM` (`course_id`, `created_by_student_number`) VALUES (4, '2055220');
INSERT INTO `internal_database_test`.`TEAM_REGISTRATION` (`student_number`, `team_id`) VALUES ('2055120', 1);
INSERT INTO `internal_database_test`.`TEAM_REGISTRATION` (`student_number`, `team_id`) VALUES ('2055120', 2);
INSERT INTO `internal_database_test`.`TEAM_REGISTRATION` (`student_number`, `team_id`) VALUES ('2055121', 1);
INSERT INTO `internal_database_test`.`TEAM_REGISTRATION` (`student_number`, `team_id`) VALUES ('2055121', 3);
INSERT INTO `internal_database_test`.`TEAM_REGISTRATION` (`student_number`, `team_id`) VALUES ('2055129', 4);
INSERT INTO `internal_database_test`.`TEAM_REGISTRATION` (`student_number`, `team_id`) VALUES ('2055140', 5);
INSERT INTO `internal_database_test`.`TEAM_REGISTRATION` (`student_number`, `team_id`) VALUES ('2055176', 5);
INSERT INTO `internal_database_test`.`TEAM_REGISTRATION` (`student_number`, `team_id`) VALUES ('2055176', 6);
INSERT INTO `internal_database_test`.`TEAM_REGISTRATION` (`student_number`, `team_id`) VALUES ('2055140', 6);
INSERT INTO `internal_database_test`.`TEAM_REGISTRATION` (`student_number`, `team_id`) VALUES ('2055143', 7);
INSERT INTO `internal_database_test`.`TEAM_REGISTRATION` (`student_number`, `team_id`) VALUES ('2055143', 8);
INSERT INTO `internal_database_test`.`TEAM_REGISTRATION` (`student_number`, `team_id`) VALUES ('2055153', 7);
INSERT INTO `internal_database_test`.`TEAM_REGISTRATION` (`student_number`, `team_id`) VALUES ('2055153', 8);
INSERT INTO `internal_database_test`.`TEAM_REGISTRATION` (`student_number`, `team_id`) VALUES ('2055163', 9);
INSERT INTO `internal_database_test`.`TEAM_REGISTRATION` (`student_number`, `team_id`) VALUES ('2055163', 10);
INSERT INTO `internal_database_test`.`TEAM_REGISTRATION` (`student_number`, `team_id`) VALUES ('2055163', 11);
INSERT INTO `internal_database_test`.`TEAM_REGISTRATION` (`student_number`, `team_id`) VALUES ('2055220', 12);
INSERT INTO `internal_database_test`.`TEAM_INVITATION` (`invitee_student_number`, `team_id`, `created_by_student_number`) VALUES ('2055121', '2', '2055120');
INSERT INTO `internal_database_test`.`TEAM_INVITATION` (`invitee_student_number`, `team_id`, `created_by_student_number`) VALUES ('2055174', '4', '2055129');
INSERT INTO `internal_database_test`.`TEAM_INVITATION` (`invitee_student_number`, `team_id`, `created_by_student_number`) VALUES ('2055192', '9', '2055163');
INSERT INTO `internal_database_test`.`TEAM_INVITATION` (`invitee_student_number`, `team_id`, `created_by_student_number`) VALUES ('2055221', '9', '2055163');
INSERT INTO `internal_database_test`.`TEAM_INVITATION` (`invitee_student_number`, `team_id`, `created_by_student_number`) VALUES ('2055223', '9', '2055163');
INSERT INTO `internal_database_test`.`MEMBERSHIP_REQUEST` (`applicant_student_number`, `team_id`) VALUES ('2055120', '3');
INSERT INTO `internal_database_test`.`MEMBERSHIP_REQUEST` (`applicant_student_number`, `team_id`) VALUES ('2055178', '9');
INSERT INTO `internal_database_test`.`MEMBERSHIP_REQUEST` (`applicant_student_number`, `team_id`) VALUES ('2055178', '12');
INSERT INTO `internal_database_test`.`MEMBERSHIP_REQUEST` (`applicant_student_number`, `team_id`) VALUES ('2055215', '12');
