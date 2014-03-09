



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
-- proposition table holds all of the propositions for a given year
-- ---

DROP TABLE IF EXISTS `proposition`;
		
CREATE TABLE `proposition` (
  `id` INTEGER NULL AUTO_INCREMENT DEFAULT NULL,
  `proposition_number` CHAR(50) NULL DEFAULT NULL,
  `proposition_question` MEDIUMTEXT(2000) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) COMMENT 'proposition table holds all of the propositions for a given ';

-- ---
-- Table 'voters_of_america'
-- hold every voter. Every voter must register at government office
-- ---

DROP TABLE IF EXISTS `voters_of_america`;
		
CREATE TABLE `voters_of_america` (
  `SSN` INTEGER NULL DEFAULT NULL,
  `full_name` VARCHAR(50) NULL DEFAULT NULL,
  `address` CHAR(200) NULL DEFAULT NULL,
  `pin` INTEGER NULL DEFAULT NULL,
  `allow_to_vote` INT NULL DEFAULT NULL,
  PRIMARY KEY (`SSN`)
) COMMENT 'hold every voter. Every voter must register at government of';

-- ---
-- Table 'prop_votes'
-- 
-- ---

DROP TABLE IF EXISTS `prop_votes`;
		
CREATE TABLE `prop_votes` (
  `votes` INTEGER NULL DEFAULT NULL,
  `time` TIMESTAMP NULL DEFAULT NULL,
  `voa_id` INT NULL DEFAULT NULL,
  `pid` INT NULL DEFAULT NULL,
  PRIMARY KEY (`pid`, `voa_id`)
);

-- ---
-- Table 'candidate_votes'
-- 
-- ---

DROP TABLE IF EXISTS `candidate_votes`;
		
CREATE TABLE `candidate_votes` (
  `id` INTEGER NULL AUTO_INCREMENT DEFAULT NULL,
  `candidate_id` INTEGER NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ---
-- Foreign Keys 
-- ---

ALTER TABLE `proposition` ADD FOREIGN KEY (id) REFERENCES `prop_votes` (`pid`);
ALTER TABLE `voters_of_america` ADD FOREIGN KEY (SSN) REFERENCES `prop_votes` (`voa_id`);
ALTER TABLE `voters_of_america` ADD FOREIGN KEY (SSN) REFERENCES `candidate_votes` (`id`);
ALTER TABLE `candidate_votes` ADD FOREIGN KEY (candidate_id) REFERENCES `candidates` (`id`);

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
-- INSERT INTO `voters_of_america` (`SSN`,`full_name`,`address`,`pin`,`allow_to_vote`) VALUES
-- ('','','','','');
-- INSERT INTO `prop_votes` (`votes`,`time`,`voa_id`,`pid`) VALUES
-- ('','','','');
-- INSERT INTO `candidate_votes` (`id`,`candidate_id`) VALUES
-- ('','');


