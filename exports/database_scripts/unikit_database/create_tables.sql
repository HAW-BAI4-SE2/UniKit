CREATE TABLE `unikit_database`.`COURSE_REGISTRATION` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `student_number` VARCHAR(31) NOT NULL COMMENT '',
  `course_id` INT NOT NULL COMMENT '',
  `currently_assigned_to_team` BIT NOT NULL DEFAULT 0 COMMENT '',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `student_number_idx` (`student_number` ASC)  COMMENT '',
  INDEX `course_id_idx` (`course_id` ASC)  COMMENT '');

CREATE TABLE `unikit_database`.`TEAM` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `course_id` INT NOT NULL COMMENT '',
  `created_by_student_number` VARCHAR(31) NOT NULL COMMENT '',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `course_id_idx` (`course_id` ASC)  COMMENT '',
  INDEX `created_by_student_number_idx` (`created_by_student_number` ASC)  COMMENT '');

CREATE TABLE `unikit_database`.`TEAM_REGISTRATION` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `student_number` VARCHAR(31) NOT NULL COMMENT '',
  `team_id` INT NOT NULL COMMENT '',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `student_number_idx` (`student_number` ASC)  COMMENT '',
  INDEX `team_id_idx` (`team_id` ASC)  COMMENT '',
  CONSTRAINT `team_id_team_registration`
    FOREIGN KEY (`team_id`)
    REFERENCES `unikit_database`.`TEAM` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `unikit_database`.`TEAM_INVITATION` (
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
  CONSTRAINT `team_id_team_invitation`
    FOREIGN KEY (`team_id`)
    REFERENCES `unikit_database`.`TEAM` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `unikit_database`.`TEAM_APPLICATION` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '',
  `applicant_student_number` VARCHAR(31) NOT NULL COMMENT '',
  `team_id` INT NOT NULL COMMENT '',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `id_UNIQUE` (`id` ASC)  COMMENT '',
  INDEX `applicant_student_number_idx` (`applicant_student_number` ASC)  COMMENT '',
  INDEX `team_id_idx` (`team_id` ASC)  COMMENT '',
  CONSTRAINT `team_id_team_application`
    FOREIGN KEY (`team_id`)
    REFERENCES `unikit_database`.`TEAM` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
