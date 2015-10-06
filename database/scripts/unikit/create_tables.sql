CREATE TABLE `haw_se2`.`TEAM` (
  `id` INT NOT NULL COMMENT '',
  `course_id` VARCHAR(45) NOT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '');

CREATE TABLE `haw_se2`.`STUDENT_TO_TEAM` (
  `student_number` VARCHAR(31) NOT NULL COMMENT '',
  `team_id` INT NOT NULL COMMENT '',
  INDEX `team_id_idx` (`team_id` ASC)  COMMENT '',
  CONSTRAINT `team_id`
    FOREIGN KEY (`team_id`)
    REFERENCES `haw_se2`.`team` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `haw_se2`.`COURSE_REGISTRATIONS` (
  `student_number` VARCHAR(31) NOT NULL COMMENT '',
  `course_id` INT NOT NULL COMMENT '');
