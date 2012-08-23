SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `gemp-lotr` DEFAULT CHARACTER SET utf8 ;
USE `gemp-lotr` ;

-- -----------------------------------------------------
-- Table `gemp-lotr`.`collection`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `gemp-lotr`.`collection` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `player_id` INT(11) NOT NULL ,
  `collection` MEDIUMBLOB NOT NULL ,
  `type` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 3507
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `gemp-lotr`.`deck`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `gemp-lotr`.`deck` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `player_id` INT(11) NOT NULL ,
  `name` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL DEFAULT 'Default' ,
  `type` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL DEFAULT 'Default' ,
  `contents` TEXT CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `player_deck` (`player_id` ASC, `name` ASC) ,
  INDEX `player_id` (`id` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 15841
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `gemp-lotr`.`game_history`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `gemp-lotr`.`game_history` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `winner` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `loser` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `win_reason` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `lose_reason` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `win_recording_id` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ,
  `lose_recording_id` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ,
  `start_date` DECIMAL(20,0) NOT NULL ,
  `end_date` DECIMAL(20,0) NOT NULL ,
  `format_name` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ,
  `winner_deck_name` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ,
  `loser_deck_name` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ,
  `tournament` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 38908
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `gemp-lotr`.`league`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `gemp-lotr`.`league` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `type` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `class` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `parameters` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `start` INT(11) NOT NULL ,
  `end` INT(11) NOT NULL ,
  `status` INT(11) NOT NULL ,
  `cost` INT(11) NOT NULL DEFAULT '0' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 26
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `gemp-lotr`.`league_match`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `gemp-lotr`.`league_match` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `league_type` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `season_type` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `winner` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `loser` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 11138
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `gemp-lotr`.`league_participation`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `gemp-lotr`.`league_participation` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `league_type` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `player_name` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `join_ip` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 3237
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `gemp-lotr`.`merchant_data`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `gemp-lotr`.`merchant_data` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `blueprint_id` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `transaction_date` DATETIME NOT NULL ,
  `transaction_price` FLOAT NOT NULL ,
  `transaction_type` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `sell_count` INT(11) NOT NULL ,
  `buy_count` INT(11) NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `blueprintId_UNIQUE` (`blueprint_id` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 3042
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `gemp-lotr`.`player`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `gemp-lotr`.`player` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(10) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `password` VARCHAR(64) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `type` VARCHAR(5) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL DEFAULT 'u' ,
  `last_login_reward` INT(11) NULL DEFAULT NULL ,
  `last_ip` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ,
  `create_ip` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 2379
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `gemp-lotr`.`tournament`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `gemp-lotr`.`tournament` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `tournament_id` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `class` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `parameters` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `start` DECIMAL(20,0) NOT NULL ,
  `finished` BINARY(1) NOT NULL DEFAULT '0' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 18
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `gemp-lotr`.`tournament_match`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `gemp-lotr`.`tournament_match` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `tournament_id` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `round` DECIMAL(2,0) NOT NULL ,
  `player_one` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `player_two` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `winner` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 53
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `gemp-lotr`.`tournament_player`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `gemp-lotr`.`tournament_player` (
  `id` INT(11) NOT NULL AUTO_INCREMENT ,
  `tournament_id` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `player` VARCHAR(10) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `deck_name` VARCHAR(45) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `deck` TEXT CHARACTER SET 'utf8' COLLATE 'utf8_bin' NOT NULL ,
  `dropped` BINARY(1) NOT NULL DEFAULT '0' ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB
AUTO_INCREMENT = 69
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
