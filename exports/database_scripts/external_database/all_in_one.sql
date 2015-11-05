DROP TABLE `external_database`.`COMPLETED_COURSE`;
DROP TABLE `external_database`.`STUDENT`;
DROP TABLE `external_database`.`COURSE_LECTURE`;
DROP TABLE `external_database`.`COURSE_GROUP`;
DROP TABLE `external_database`.`APPOINTMENT`;
DROP TABLE `external_database`.`DIDACTIC_UNIT`;
DROP TABLE `external_database`.`COURSE_TO_FIELD_OF_STUDY`;
DROP TABLE `external_database`.`COURSE`;
DROP TABLE `external_database`.`FIELD_OF_STUDY`;

CREATE TABLE `external_database`.`FIELD_OF_STUDY` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `name` VARCHAR(63) NOT NULL COMMENT '',
  `abbreviation` VARCHAR(31) NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  UNIQUE INDEX `name_UNIQUE` (`name` ASC)  COMMENT '',
  UNIQUE INDEX `abbreviation_UNIQUE` (`abbreviation` ASC)  COMMENT '');

CREATE TABLE `external_database`.`COURSE` (
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

CREATE TABLE `external_database`.`COURSE_TO_FIELD_OF_STUDY` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `course_id` INT NOT NULL COMMENT '',
  `field_of_study_id` INT NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `course_id_idx` (`course_id` ASC)  COMMENT '',
  INDEX `field_of_study_id_idx` (`field_of_study_id` ASC)  COMMENT '',
  CONSTRAINT `course_id_course_to_field_of_study`
    FOREIGN KEY (`course_id`)
    REFERENCES `external_database`.`COURSE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `field_of_study_id_course_to_field_of_study`
    FOREIGN KEY (`field_of_study_id`)
    REFERENCES `external_database`.`FIELD_OF_STUDY` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `external_database`.`DIDACTIC_UNIT` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `course_id` INT NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `course_id_idx` (`course_id` ASC)  COMMENT '',
  CONSTRAINT `course_id_didactic_unit`
    FOREIGN KEY (`course_id`)
    REFERENCES `external_database`.`COURSE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `external_database`.`APPOINTMENT` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `didactic_unit_id` INT NOT NULL COMMENT '',
  `start_date` DATETIME NOT NULL COMMENT '',
  `end_date` DATETIME NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `didactic_unit_id_idx` (`didactic_unit_id` ASC)  COMMENT '',
  CONSTRAINT `didactic_unit_id_appointment`
    FOREIGN KEY (`didactic_unit_id`)
    REFERENCES `external_database`.`DIDACTIC_UNIT` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `external_database`.`COURSE_GROUP` (
  `id` INT NOT NULL COMMENT '',
  `group_number` INT NOT NULL COMMENT '',
  `max_group_size` INT NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  CONSTRAINT `id_course_group`
    FOREIGN KEY (`id`)
    REFERENCES `external_database`.`DIDACTIC_UNIT` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
    
CREATE TABLE `external_database`.`COURSE_LECTURE` (
  `id` INT NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  CONSTRAINT `id_course_lecture`
    FOREIGN KEY (`id`)
    REFERENCES `external_database`.`DIDACTIC_UNIT` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `external_database`.`STUDENT` (
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
    REFERENCES `external_database`.`FIELD_OF_STUDY` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `external_database`.`COMPLETED_COURSE` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `student_number` VARCHAR(31) NOT NULL COMMENT '',
  `course_id` INT NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `course_id_idx` (`course_id` ASC)  COMMENT '',
  INDEX `student_number_idx` (`student_number` ASC)  COMMENT '',
  CONSTRAINT `course_id_completed_course`
    FOREIGN KEY (`course_id`)
    REFERENCES `external_database`.`COURSE` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `student_number_completed_course`
    FOREIGN KEY (`student_number`)
    REFERENCES `external_database`.`STUDENT` (`student_number`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
