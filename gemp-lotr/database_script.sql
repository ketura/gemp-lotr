-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: ec2-176-34-225-252.eu-west-1.compute.amazonaws.com    Database: gemp-lotr
-- ------------------------------------------------------
-- Server version	5.1.73

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `collection`
--

DROP TABLE IF EXISTS `collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `collection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `collection` mediumblob NOT NULL,
  `type` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  KEY `player_collection` (`player_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `deck`
--

DROP TABLE IF EXISTS `deck`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deck` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `name` varchar(45) COLLATE utf8_bin NOT NULL DEFAULT 'Default',
  `type` varchar(45) COLLATE utf8_bin NOT NULL DEFAULT 'Default',
  `contents` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `player_deck` (`player_id`,`name`),
  KEY `player_id` (`player_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `game_history`
--

DROP TABLE IF EXISTS `game_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `game_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `winner` varchar(45) COLLATE utf8_bin NOT NULL,
  `loser` varchar(45) COLLATE utf8_bin NOT NULL,
  `win_reason` varchar(255) COLLATE utf8_bin NOT NULL,
  `lose_reason` varchar(255) COLLATE utf8_bin NOT NULL,
  `win_recording_id` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `lose_recording_id` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `start_date` decimal(20,0) NOT NULL,
  `end_date` decimal(20,0) NOT NULL,
  `format_name` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `winner_deck_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `loser_deck_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tournament` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `end_date` (`end_date`),
  KEY `index3` (`winner`),
  KEY `index4` (`loser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ignores`
--

DROP TABLE IF EXISTS `ignores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ignores` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `playerName` varchar(10) NOT NULL,
  `ignoredName` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `PLAYER_IGNORES` (`playerName`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ip_ban`
--

DROP TABLE IF EXISTS `ip_ban`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ip_ban` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(255) NOT NULL,
  `prefix` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `league`
--

DROP TABLE IF EXISTS `league`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `league` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_bin NOT NULL,
  `type` varchar(45) COLLATE utf8_bin NOT NULL,
  `class` varchar(255) COLLATE utf8_bin NOT NULL,
  `parameters` varchar(255) COLLATE utf8_bin NOT NULL,
  `start` int(11) NOT NULL,
  `end` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `cost` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `league_match`
--

DROP TABLE IF EXISTS `league_match`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `league_match` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `league_type` varchar(45) COLLATE utf8_bin NOT NULL,
  `season_type` varchar(45) COLLATE utf8_bin NOT NULL,
  `winner` varchar(45) COLLATE utf8_bin NOT NULL,
  `loser` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `league_participation`
--

DROP TABLE IF EXISTS `league_participation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `league_participation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `league_type` varchar(45) COLLATE utf8_bin NOT NULL,
  `player_name` varchar(45) COLLATE utf8_bin NOT NULL,
  `join_ip` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `player`
--

DROP TABLE IF EXISTS `player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `player` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(10) COLLATE utf8_bin NOT NULL,
  `password` varchar(64) COLLATE utf8_bin NOT NULL,
  `type` varchar(5) COLLATE utf8_bin NOT NULL DEFAULT 'u',
  `last_login_reward` int(11) DEFAULT NULL,
  `last_ip` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `create_ip` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `banned_until` decimal(20,0) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scheduled_tournament`
--

DROP TABLE IF EXISTS `scheduled_tournament`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduled_tournament` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tournament_id` varchar(45) COLLATE utf8_bin NOT NULL,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `format` varchar(45) COLLATE utf8_bin NOT NULL,
  `start` decimal(20,0) NOT NULL,
  `cost` decimal(10,0) NOT NULL,
  `playoff` varchar(45) COLLATE utf8_bin NOT NULL,
  `prizes` varchar(45) COLLATE utf8_bin NOT NULL,
  `minimum_players` decimal(3,0) NOT NULL,
  `started` decimal(1,0) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `started` (`started`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tournament`
--

DROP TABLE IF EXISTS `tournament`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tournament` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tournament_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `start` decimal(20,0) NOT NULL,
  `draft_type` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `format` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `collection` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `stage` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `pairing` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `round` int(3) DEFAULT NULL,
  `prizes` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tournament_match`
--

DROP TABLE IF EXISTS `tournament_match`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tournament_match` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tournament_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `round` decimal(2,0) NOT NULL,
  `player_one` varchar(45) COLLATE utf8_bin NOT NULL,
  `player_two` varchar(45) COLLATE utf8_bin NOT NULL,
  `winner` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tournament_player`
--

DROP TABLE IF EXISTS `tournament_player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tournament_player` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tournament_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `player` varchar(10) COLLATE utf8_bin NOT NULL,
  `deck_name` varchar(45) COLLATE utf8_bin NOT NULL,
  `deck` text COLLATE utf8_bin NOT NULL,
  `dropped` binary(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transfer`
--

DROP TABLE IF EXISTS `transfer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `transfer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `notify` int(11) NOT NULL,
  `player` varchar(45) COLLATE utf8_bin NOT NULL,
  `reason` varchar(255) COLLATE utf8_bin NOT NULL,
  `name` varchar(255) COLLATE utf8_bin NOT NULL,
  `currency` int(11) NOT NULL,
  `collection` text COLLATE utf8_bin NOT NULL,
  `transfer_date` decimal(20,0) NOT NULL,
  `direction` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  KEY `player` (`player`,`notify`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-01 18:53:03
