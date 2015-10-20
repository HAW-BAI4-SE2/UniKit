CREATE TABLE `unikit_database`.`TEAM` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `course_id` INT NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `course_id_idx` (`course_id` ASC)  COMMENT '');

CREATE TABLE `unikit_database`.`TEAM_REGISTRATION` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `student_number` VARCHAR(31) NOT NULL COMMENT '',
  `team_id` INT NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `student_number_idx` (`student_number` ASC)  COMMENT '',
  INDEX `team_id_idx` (`team_id` ASC)  COMMENT '',
  CONSTRAINT `team_id_team_registration`
    FOREIGN KEY (`team_id`)
    REFERENCES `unikit_database`.`TEAM` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `unikit_database`.`COURSE_REGISTRATION` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `student_number` VARCHAR(31) NOT NULL COMMENT '',
  `course_id` INT NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `student_number_idx` (`student_number` ASC)  COMMENT '',
  INDEX `course_id_idx` (`course_id` ASC)  COMMENT '');
