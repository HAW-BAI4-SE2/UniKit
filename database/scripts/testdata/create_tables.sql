DROP TABLE `haw_se2_testdata`.`APPOINTMENT`;
DROP TABLE `haw_se2_testdata`.`GROUP`;
DROP TABLE `haw_se2_testdata`.`COMPLETED_COURSE`;
DROP TABLE `haw_se2_testdata`.`COURSE`;
DROP TABLE `haw_se2_testdata`.`STUDENT`;

CREATE TABLE `haw_se2_testdata`.`STUDENT` (
  `student_number` VARCHAR(31) NOT NULL COMMENT '',
  `first_name` VARCHAR(63) NOT NULL COMMENT '',
  `last_name` VARCHAR(63) NOT NULL COMMENT '',
  `email` VARCHAR(255) NOT NULL COMMENT '',
  `semester` INT NOT NULL COMMENT '',
  PRIMARY KEY (`student_number`)  COMMENT '',
  UNIQUE INDEX `email_UNIQUE` (`email` ASC)  COMMENT '');

CREATE TABLE `haw_se2_testdata`.`COURSE` (
  `id` INT NOT NULL COMMENT '',
  `name` VARCHAR(127) NOT NULL COMMENT '',
  `min_team_size` INT NULL COMMENT '',
  `max_team_size` INT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `name_UNIQUE` (`name` ASC)  COMMENT '');

CREATE TABLE `haw_se2_testdata`.`COMPLETED_COURSE` (
  `student_number` VARCHAR(31) NOT NULL COMMENT '',
  `course_id` INT NOT NULL COMMENT '',
  INDEX `student_number_idx` (`student_number` ASC)  COMMENT '',
  INDEX `course_id_idx` (`course_id` ASC)  COMMENT '',
  CONSTRAINT `student_number`
    FOREIGN KEY (`student_number`)
    REFERENCES `haw_se2_testdata`.`student` (`student_number`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `course_id`
    FOREIGN KEY (`course_id`)
    REFERENCES `haw_se2_testdata`.`course` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `haw_se2_testdata`.`GROUP` (
  `id` INT NOT NULL COMMENT '',
  `group_number` INT NOT NULL COMMENT '',
  `course_id` INT NOT NULL COMMENT '',
  `max_size` INT NULL COMMENT '',
  PRIMARY KEY (`id`)  COMMENT '',
  UNIQUE INDEX `course_id_UNIQUE` (`group_number` ASC, `course_id` ASC)  COMMENT '');

CREATE TABLE `haw_se2_testdata`.`APPOINTMENT` (
  `group_id` INT NOT NULL COMMENT '',
  `start` DATETIME NOT NULL COMMENT '',
  `end` DATETIME NOT NULL COMMENT '',
  INDEX `group_id_idx` (`group_id` ASC)  COMMENT '',
  CONSTRAINT `group_id`
    FOREIGN KEY (`group_id`)
    REFERENCES `haw_se2_testdata`.`group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
