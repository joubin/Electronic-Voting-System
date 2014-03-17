  drop database if EXISTS votingsystem ;
  create database votingsystem;
  use votingsystem;






-- ---
-- Globals
-- ---

-- SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
-- SET FOREIGN_KEY_CHECKS=0;

-- ---
-- Table 'candidates'
-- 
-- ---

DROP TABLE IF EXISTS `candidates`;
    
CREATE TABLE `candidates` (
  `id` INTEGER NULL AUTO_INCREMENT DEFAULT NULL,
  `full_name` VARBINARY(100) NULL DEFAULT NULL,
  `party_affiliation` VARCHAR(50) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ---
-- Table 'proposition'
-- 
-- ---

DROP TABLE IF EXISTS `proposition`;
    
CREATE TABLE `proposition` (
  `id` INTEGER NULL AUTO_INCREMENT DEFAULT NULL,
  `proposition_number` CHAR NULL DEFAULT NULL,
  `proposition_question` MEDIUMTEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ---
-- Table 'voters_of_america'
-- 
-- ---

DROP TABLE IF EXISTS `voters_of_america`;
    
CREATE TABLE `voters_of_america` (
  `SSN` VARCHAR(250) NULL DEFAULT NULL,
  `full_name` VARCHAR(250) NULL DEFAULT NULL,
  `address` MEDIUMTEXT NULL DEFAULT NULL,
  `allow_to_vote` INT NULL DEFAULT NULL,
  PRIMARY KEY (`SSN`)
);

-- ---
-- Table 'prop_votes'
-- 
-- ---

DROP TABLE IF EXISTS `prop_votes`;
    
CREATE TABLE `prop_votes` (
  `votes` INTEGER NULL DEFAULT NULL,
  `time` TIMESTAMP NULL DEFAULT NULL,
  `SSN_voters_of_america` VARCHAR(250) NULL DEFAULT NULL,
  `id_proposition` INTEGER NULL DEFAULT NULL,
  PRIMARY KEY (`SSN_voters_of_america`)
);

-- ---
-- Table 'candidate_votes'
-- 
-- ---

DROP TABLE IF EXISTS `candidate_votes`;
    
CREATE TABLE `candidate_votes` (
  `SSN_voters_of_america` VARCHAR(250) NULL DEFAULT NULL,
  `id_candidates` INTEGER NULL DEFAULT NULL,
  PRIMARY KEY (`SSN_voters_of_america`)
);

-- ---
-- Foreign Keys 
-- ---

ALTER TABLE `prop_votes` ADD FOREIGN KEY (SSN_voters_of_america) REFERENCES `voters_of_america` (`SSN`);
ALTER TABLE `prop_votes` ADD FOREIGN KEY (id_proposition) REFERENCES `proposition` (`id`);
ALTER TABLE `candidate_votes` ADD FOREIGN KEY (SSN_voters_of_america) REFERENCES `voters_of_america` (`SSN`);
ALTER TABLE `candidate_votes` ADD FOREIGN KEY (id_candidates) REFERENCES `candidates` (`id`);

-- ---
-- Table Properties
-- ---

-- ALTER TABLE `candidates` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `proposition` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `voters_of_america` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `prop_votes` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `candidate_votes` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ---
-- Test Data
-- ---

-- INSERT INTO `candidates` (`id`,`full_name`,`party_affiliation`) VALUES
-- ('','','');
-- INSERT INTO `proposition` (`id`,`proposition_number`,`proposition_question`) VALUES
-- ('','','');
-- INSERT INTO `voters_of_america` (`SSN`,`full_name`,`address`,`allow_to_vote`) VALUES
-- ('','','','');
-- INSERT INTO `prop_votes` (`votes`,`time`,`SSN_voters_of_america`,`id_proposition`) VALUES
-- ('','','','');
-- INSERT INTO `candidate_votes` (`SSN_voters_of_america`,`id_candidates`) VALUES
-- ('','');

