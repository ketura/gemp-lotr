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
 SET character_set_client = utf8mb4 ;
CREATE TABLE `collection` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `collection` mediumblob DEFAULT NULL,
  `type` varchar(45) COLLATE utf8_bin NOT NULL,
  `extra_info` varchar(5000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_collection_player_type` (`player_id`,`type`),
  KEY `player_collection` (`player_id`,`type`),
  CONSTRAINT `fk_collection_player_id` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=88474 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `collection_entries`
--

DROP TABLE IF EXISTS `collection_entries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `collection_entries` (
  `collection_id` int(11) NOT NULL,
  `quantity` int(2) DEFAULT 0,
  `product_type` varchar(50) NOT NULL,
  `product_variant` varchar(50) DEFAULT NULL,
  `product` varchar(50) NOT NULL,
  `source` varchar(50) NOT NULL,
  `created_date` datetime DEFAULT current_timestamp(),
  `modified_date` datetime DEFAULT NULL ON UPDATE current_timestamp(),
  `notes` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`collection_id`,`product`),
  CONSTRAINT `collection_entries_ibfk_1` FOREIGN KEY (`collection_id`) REFERENCES `collection` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `deck`
--

DROP TABLE IF EXISTS `deck`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `deck` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `player_id` int(11) NOT NULL,
  `name` varchar(100) COLLATE utf8_bin NOT NULL,
  `target_format` varchar(50) COLLATE utf8_bin NOT NULL DEFAULT 'Anything Goes',
  `type` varchar(45) COLLATE utf8_bin NOT NULL DEFAULT 'Default',
  `contents` text COLLATE utf8_bin NOT NULL,
  `notes` varchar(5000) COLLATE utf8_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `player_deck` (`player_id`,`name`),
  KEY `player_id` (`player_id`),
  CONSTRAINT `deck_ibfk_1` FOREIGN KEY (`player_id`) REFERENCES `player` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=222559 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `game_history`
--

DROP TABLE IF EXISTS `game_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `game_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `winner` varchar(45) COLLATE utf8_bin NOT NULL,
  `winnerId` int(11) NOT NULL DEFAULT 0,
  `loser` varchar(45) COLLATE utf8_bin NOT NULL,
  `loserId` int(11) NOT NULL DEFAULT 0,
  `win_reason` varchar(255) COLLATE utf8_bin NOT NULL,
  `lose_reason` varchar(255) COLLATE utf8_bin NOT NULL,
  `win_recording_id` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `lose_recording_id` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `start_date` datetime NOT NULL DEFAULT current_timestamp(),
  `end_date` datetime NOT NULL DEFAULT current_timestamp(),
  `format_name` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `winner_deck_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `loser_deck_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `tournament` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `replay_version` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `index3` (`winner`),
  KEY `index4` (`loser`),
  KEY `game_history_win_id_index` (`win_recording_id`),
  KEY `game_history_lose_id_index` (`lose_recording_id`),
  KEY `fk_winnerId` (`winnerId`),
  KEY `fk_loserId` (`loserId`),
  CONSTRAINT `fk_loserId` FOREIGN KEY (`loserId`) REFERENCES `player` (`id`),
  CONSTRAINT `fk_winnerId` FOREIGN KEY (`winnerId`) REFERENCES `player` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=986971 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ignores`
--

DROP TABLE IF EXISTS `ignores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ignores` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `playerName` varchar(30) DEFAULT NULL,
  `ignoredName` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `PLAYER_IGNORES` (`playerName`)
) ENGINE=MyISAM AUTO_INCREMENT=411 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ip_ban`
--

DROP TABLE IF EXISTS `ip_ban`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `ip_ban` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(255) NOT NULL,
  `prefix` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=241 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `league`
--

DROP TABLE IF EXISTS `league`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `league` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_bin NOT NULL,
  `type` varchar(45) COLLATE utf8_bin NOT NULL,
  `class` varchar(255) COLLATE utf8_bin NOT NULL,
  `parameters` varchar(255) COLLATE utf8_bin NOT NULL,
  `start` int(11) NOT NULL,
  `end` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `cost` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=478 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `league_match`
--

DROP TABLE IF EXISTS `league_match`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `league_match` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `league_type` varchar(45) COLLATE utf8_bin NOT NULL,
  `season_type` varchar(45) COLLATE utf8_bin NOT NULL,
  `winner` varchar(45) COLLATE utf8_bin NOT NULL,
  `loser` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  KEY `league_match_type` (`league_type`)
) ENGINE=InnoDB AUTO_INCREMENT=174464 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `league_participation`
--

DROP TABLE IF EXISTS `league_participation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `league_participation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `league_type` varchar(45) COLLATE utf8_bin NOT NULL,
  `player_name` varchar(45) COLLATE utf8_bin NOT NULL,
  `join_ip` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `league_participation_type` (`league_type`)
) ENGINE=InnoDB AUTO_INCREMENT=43090 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `player`
--

DROP TABLE IF EXISTS `player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `player` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(64) COLLATE utf8_bin NOT NULL,
  `type` varchar(5) COLLATE utf8_bin NOT NULL DEFAULT 'u',
  `last_login_reward` int(11) DEFAULT NULL,
  `last_ip` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `create_ip` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `banned_until` decimal(20,0) DEFAULT NULL,
  `email` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `verified` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=32845 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scheduled_tournament`
--

DROP TABLE IF EXISTS `scheduled_tournament`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
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
  `started` decimal(1,0) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `started` (`started`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tournament`
--

DROP TABLE IF EXISTS `tournament`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
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
) ENGINE=InnoDB AUTO_INCREMENT=1383 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tournament_match`
--

DROP TABLE IF EXISTS `tournament_match`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tournament_match` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tournament_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `round` decimal(2,0) NOT NULL,
  `player_one` varchar(45) COLLATE utf8_bin NOT NULL,
  `player_two` varchar(45) COLLATE utf8_bin NOT NULL,
  `winner` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14304 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tournament_player`
--

DROP TABLE IF EXISTS `tournament_player`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `tournament_player` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tournament_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `player` varchar(30) COLLATE utf8_bin DEFAULT NULL,
  `deck_name` varchar(45) COLLATE utf8_bin NOT NULL,
  `deck` text COLLATE utf8_bin NOT NULL,
  `dropped` binary(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10377 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transfer`
--

DROP TABLE IF EXISTS `transfer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
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
) ENGINE=InnoDB AUTO_INCREMENT=3644 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
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
