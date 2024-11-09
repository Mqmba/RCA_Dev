CREATE DATABASE  IF NOT EXISTS `RCA` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `RCA`;
-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: RCA
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Admin`
--

DROP TABLE IF EXISTS `Admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Admin` (
  `AdminId` int NOT NULL AUTO_INCREMENT,
  `UserId` int NOT NULL,
  `CreatedAt` datetime(6) DEFAULT NULL,
  `UpdatedAt` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`AdminId`),
  KEY `UserId` (`UserId`),
  CONSTRAINT `Admin_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `User` (`UserId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Admin`
--

LOCK TABLES `Admin` WRITE;
/*!40000 ALTER TABLE `Admin` DISABLE KEYS */;
INSERT INTO `Admin` VALUES (1,1,'2024-10-31 00:00:00.000000','2024-10-31 00:00:00.000000');
/*!40000 ALTER TABLE `Admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Apartment`
--

DROP TABLE IF EXISTS `Apartment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Apartment` (
  `ApartmentId` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) DEFAULT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `BuildingId` int DEFAULT NULL,
  `CreatedAt` datetime DEFAULT NULL,
  `UpdatedAt` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`ApartmentId`),
  KEY `BuildingId` (`BuildingId`),
  CONSTRAINT `Apartment_ibfk_1` FOREIGN KEY (`BuildingId`) REFERENCES `Building` (`BuildingId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Apartment`
--

LOCK TABLES `Apartment` WRITE;
/*!40000 ALTER TABLE `Apartment` DISABLE KEYS */;
/*!40000 ALTER TABLE `Apartment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Building`
--

DROP TABLE IF EXISTS `Building`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Building` (
  `BuildingId` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) DEFAULT NULL,
  `Location` varchar(255) DEFAULT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `CreatedAt` datetime DEFAULT NULL,
  `UpdatedAt` datetime DEFAULT NULL,
  PRIMARY KEY (`BuildingId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Building`
--

LOCK TABLES `Building` WRITE;
/*!40000 ALTER TABLE `Building` DISABLE KEYS */;
/*!40000 ALTER TABLE `Building` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Chat`
--

DROP TABLE IF EXISTS `Chat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Chat` (
  `ChatId` int NOT NULL AUTO_INCREMENT,
  `ResidentId` int NOT NULL,
  `CollectorId` int NOT NULL,
  `CreatedAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UpdatedAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `Status` enum('ACTIVE','ARCHIVED') NOT NULL DEFAULT 'ACTIVE',
  PRIMARY KEY (`ChatId`),
  UNIQUE KEY `unique_chat` (`ResidentId`,`CollectorId`),
  KEY `CollectorId` (`CollectorId`),
  CONSTRAINT `Chat_ibfk_1` FOREIGN KEY (`ResidentId`) REFERENCES `Resident` (`ResidentId`),
  CONSTRAINT `Chat_ibfk_2` FOREIGN KEY (`CollectorId`) REFERENCES `Collector` (`CollectorId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Chat`
--

LOCK TABLES `Chat` WRITE;
/*!40000 ALTER TABLE `Chat` DISABLE KEYS */;
/*!40000 ALTER TABLE `Chat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CollectionSchedule`
--

DROP TABLE IF EXISTS `CollectionSchedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CollectionSchedule` (
  `CollectionScheduleId` int NOT NULL AUTO_INCREMENT,
  `CreatedAt` datetime(6) DEFAULT NULL,
  `UpdatedAt` datetime(6) DEFAULT NULL,
  `scheduleDate` datetime(6) DEFAULT NULL,
  `IsActive` bit(1) DEFAULT NULL,
  PRIMARY KEY (`CollectionScheduleId`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CollectionSchedule`
--

LOCK TABLES `CollectionSchedule` WRITE;
/*!40000 ALTER TABLE `CollectionSchedule` DISABLE KEYS */;
INSERT INTO `CollectionSchedule` VALUES (2,'2024-11-03 14:41:18.028000','2024-11-03 14:41:18.028000','2024-11-03 14:41:11.522000',NULL),(3,'2024-11-03 14:43:41.026000','2024-11-03 17:22:31.986000','2024-11-11 17:22:24.679000',NULL),(4,'2024-11-03 17:14:28.120000','2024-11-03 17:14:28.120000','2024-11-05 17:13:59.676000',NULL),(5,'2024-11-03 17:15:18.385000','2024-11-03 17:15:18.385000','2024-11-05 17:13:59.676000',NULL),(9,'2024-11-03 18:08:31.938000','2024-11-03 18:08:31.938000','2024-11-03 18:08:30.878000',_binary ''),(10,'2024-11-03 18:08:33.430000','2024-11-03 18:08:43.663000','2024-11-03 18:08:30.878000',_binary '\0');
/*!40000 ALTER TABLE `CollectionSchedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CollectionSchedule_Register`
--

DROP TABLE IF EXISTS `CollectionSchedule_Register`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CollectionSchedule_Register` (
  `CollectionScheduleRegisterId` int NOT NULL AUTO_INCREMENT,
  `RecyclingDepotId` int NOT NULL,
  `CollectionScheduleId` int NOT NULL,
  `CollectorId` int NOT NULL,
  `ResidentId` int NOT NULL,
  `BuildingId` int DEFAULT NULL,
  `Status` int DEFAULT NULL,
  `CreatedAt` datetime(6) DEFAULT NULL,
  `UpdatedAt` datetime(6) DEFAULT NULL,
  `Time` int DEFAULT NULL,
  `MaterialType` varchar(255) DEFAULT NULL,
  `Building` int DEFAULT NULL,
  `CollectionSchedule` int DEFAULT NULL,
  `Collector` int DEFAULT NULL,
  `RecycleDepot` int DEFAULT NULL,
  `Resident` int DEFAULT NULL,
  PRIMARY KEY (`CollectionScheduleRegisterId`),
  KEY `RecyclingDepotId` (`RecyclingDepotId`),
  KEY `CollectionScheduleId` (`CollectionScheduleId`),
  KEY `CollectorId` (`CollectorId`),
  KEY `ResidentId` (`ResidentId`),
  KEY `BuildingId` (`BuildingId`),
  KEY `FKon0jcvm308msw8u8inh826ufo` (`Building`),
  KEY `FKtmwfia7e987j89x3ki2kg2y4l` (`CollectionSchedule`),
  KEY `FK6u8jc6bg8193lyy92i2kq8ruu` (`Collector`),
  KEY `FKjlb7fih2ucv9w6angb5ufnudj` (`RecycleDepot`),
  KEY `FK5okm89sdeupqdrh92g1yfqao7` (`Resident`),
  CONSTRAINT `CollectionSchedule_Register_ibfk_1` FOREIGN KEY (`RecyclingDepotId`) REFERENCES `RecyclingDepot` (`RecyclingDepotId`),
  CONSTRAINT `CollectionSchedule_Register_ibfk_2` FOREIGN KEY (`CollectionScheduleId`) REFERENCES `CollectionSchedule` (`CollectionScheduleId`),
  CONSTRAINT `CollectionSchedule_Register_ibfk_3` FOREIGN KEY (`CollectorId`) REFERENCES `Collector` (`CollectorId`),
  CONSTRAINT `CollectionSchedule_Register_ibfk_4` FOREIGN KEY (`ResidentId`) REFERENCES `Resident` (`ResidentId`),
  CONSTRAINT `CollectionSchedule_Register_ibfk_5` FOREIGN KEY (`BuildingId`) REFERENCES `Building` (`BuildingId`),
  CONSTRAINT `FK5okm89sdeupqdrh92g1yfqao7` FOREIGN KEY (`Resident`) REFERENCES `Resident` (`ResidentId`),
  CONSTRAINT `FK6u8jc6bg8193lyy92i2kq8ruu` FOREIGN KEY (`Collector`) REFERENCES `Collector` (`CollectorId`),
  CONSTRAINT `FKjlb7fih2ucv9w6angb5ufnudj` FOREIGN KEY (`RecycleDepot`) REFERENCES `RecyclingDepot` (`RecyclingDepotId`),
  CONSTRAINT `FKon0jcvm308msw8u8inh826ufo` FOREIGN KEY (`Building`) REFERENCES `Building` (`BuildingId`),
  CONSTRAINT `FKtmwfia7e987j89x3ki2kg2y4l` FOREIGN KEY (`CollectionSchedule`) REFERENCES `CollectionSchedule` (`CollectionScheduleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CollectionSchedule_Register`
--

LOCK TABLES `CollectionSchedule_Register` WRITE;
/*!40000 ALTER TABLE `CollectionSchedule_Register` DISABLE KEYS */;
/*!40000 ALTER TABLE `CollectionSchedule_Register` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Collector`
--

DROP TABLE IF EXISTS `Collector`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Collector` (
  `CollectorId` int NOT NULL AUTO_INCREMENT,
  `VehicleType` int DEFAULT NULL,
  `VehicleLicensePlate` varchar(255) DEFAULT NULL,
  `Rate` int DEFAULT NULL,
  `NumberPoint` int DEFAULT NULL,
  `IsWorking` bit(1) DEFAULT NULL,
  `UserId` int NOT NULL,
  `CreatedAt` datetime(6) DEFAULT NULL,
  `UpdatedAt` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`CollectorId`),
  KEY `UserId` (`UserId`),
  CONSTRAINT `Collector_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `User` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Collector`
--

LOCK TABLES `Collector` WRITE;
/*!40000 ALTER TABLE `Collector` DISABLE KEYS */;
/*!40000 ALTER TABLE `Collector` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CollectorDepot_Payment`
--

DROP TABLE IF EXISTS `CollectorDepot_Payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CollectorDepot_Payment` (
  `CollectorDepotPaymentId` int NOT NULL AUTO_INCREMENT,
  `CollectorId` int NOT NULL,
  `RecyclingDepotId` int NOT NULL,
  `Amount` int DEFAULT NULL,
  `PaymentMethod` int DEFAULT NULL,
  `CreatedAt` date DEFAULT NULL,
  `PaymentStatus` int DEFAULT NULL,
  PRIMARY KEY (`CollectorDepotPaymentId`),
  KEY `CollectorId` (`CollectorId`),
  KEY `RecyclingDepotId` (`RecyclingDepotId`),
  CONSTRAINT `CollectorDepot_Payment_ibfk_1` FOREIGN KEY (`CollectorId`) REFERENCES `Collector` (`CollectorId`),
  CONSTRAINT `CollectorDepot_Payment_ibfk_2` FOREIGN KEY (`RecyclingDepotId`) REFERENCES `RecyclingDepot` (`RecyclingDepotId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CollectorDepot_Payment`
--

LOCK TABLES `CollectorDepot_Payment` WRITE;
/*!40000 ALTER TABLE `CollectorDepot_Payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `CollectorDepot_Payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CollectorResident_Payment`
--

DROP TABLE IF EXISTS `CollectorResident_Payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CollectorResident_Payment` (
  `CRPaymentId` int NOT NULL AUTO_INCREMENT,
  `CollectorId` int NOT NULL,
  `ResidentId` int NOT NULL,
  `AmountPoint` int DEFAULT NULL,
  `CreatedAt` date DEFAULT NULL,
  `PaymentStatus` int DEFAULT NULL,
  PRIMARY KEY (`CRPaymentId`),
  KEY `CollectorId` (`CollectorId`),
  KEY `ResidentId` (`ResidentId`),
  CONSTRAINT `CollectorResident_Payment_ibfk_1` FOREIGN KEY (`CollectorId`) REFERENCES `Collector` (`CollectorId`),
  CONSTRAINT `CollectorResident_Payment_ibfk_2` FOREIGN KEY (`ResidentId`) REFERENCES `Resident` (`ResidentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CollectorResident_Payment`
--

LOCK TABLES `CollectorResident_Payment` WRITE;
/*!40000 ALTER TABLE `CollectorResident_Payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `CollectorResident_Payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Depot_WorkingDate`
--

DROP TABLE IF EXISTS `Depot_WorkingDate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Depot_WorkingDate` (
  `DepotWorkingDateId` int NOT NULL AUTO_INCREMENT,
  `WorkingDateId` int NOT NULL,
  `RecyclingDepotId` int NOT NULL,
  `Working_Hour` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`DepotWorkingDateId`),
  KEY `WorkingDateId` (`WorkingDateId`),
  KEY `RecyclingDepotId` (`RecyclingDepotId`),
  CONSTRAINT `Depot_WorkingDate_ibfk_1` FOREIGN KEY (`WorkingDateId`) REFERENCES `WorkingDate` (`WorkingDateId`),
  CONSTRAINT `Depot_WorkingDate_ibfk_2` FOREIGN KEY (`RecyclingDepotId`) REFERENCES `RecyclingDepot` (`RecyclingDepotId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Depot_WorkingDate`
--

LOCK TABLES `Depot_WorkingDate` WRITE;
/*!40000 ALTER TABLE `Depot_WorkingDate` DISABLE KEYS */;
/*!40000 ALTER TABLE `Depot_WorkingDate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Message`
--

DROP TABLE IF EXISTS `Message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Message` (
  `MessageId` int NOT NULL AUTO_INCREMENT,
  `SenderId` int NOT NULL,
  `ReceiverId` int NOT NULL,
  `Content` text,
  `ImageData` longblob,
  `ImageType` varchar(50) DEFAULT NULL,
  `MessageType` enum('TEXT','IMAGE') NOT NULL,
  `CreatedAt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Status` enum('SENT','DELIVERED','READ') NOT NULL DEFAULT 'SENT',
  PRIMARY KEY (`MessageId`),
  KEY `SenderId` (`SenderId`),
  KEY `ReceiverId` (`ReceiverId`),
  CONSTRAINT `Message_ibfk_1` FOREIGN KEY (`SenderId`) REFERENCES `User` (`UserId`),
  CONSTRAINT `Message_ibfk_2` FOREIGN KEY (`ReceiverId`) REFERENCES `User` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Message`
--

LOCK TABLES `Message` WRITE;
/*!40000 ALTER TABLE `Message` DISABLE KEYS */;
/*!40000 ALTER TABLE `Message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Notification`
--

DROP TABLE IF EXISTS `Notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Notification` (
  `NotificationId` int NOT NULL AUTO_INCREMENT,
  `Message` text,
  `CreatedAt` date DEFAULT NULL,
  `AdminId` int NOT NULL,
  PRIMARY KEY (`NotificationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Notification`
--

LOCK TABLES `Notification` WRITE;
/*!40000 ALTER TABLE `Notification` DISABLE KEYS */;
/*!40000 ALTER TABLE `Notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Notification_Recipient`
--

DROP TABLE IF EXISTS `Notification_Recipient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Notification_Recipient` (
  `NotificationRecipientId` int NOT NULL AUTO_INCREMENT,
  `UserId` int NOT NULL,
  `NotificationId` int NOT NULL,
  `SendType` int DEFAULT NULL,
  PRIMARY KEY (`NotificationRecipientId`),
  KEY `UserId` (`UserId`),
  KEY `NotificationId` (`NotificationId`),
  CONSTRAINT `Notification_Recipient_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `User` (`UserId`),
  CONSTRAINT `Notification_Recipient_ibfk_2` FOREIGN KEY (`NotificationId`) REFERENCES `Notification` (`NotificationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Notification_Recipient`
--

LOCK TABLES `Notification_Recipient` WRITE;
/*!40000 ALTER TABLE `Notification_Recipient` DISABLE KEYS */;
/*!40000 ALTER TABLE `Notification_Recipient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Ranking`
--

DROP TABLE IF EXISTS `Ranking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Ranking` (
  `RankingId` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) DEFAULT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `RankingType` int DEFAULT NULL,
  `CreatedAt` int DEFAULT NULL,
  `UpdatedAt` int DEFAULT NULL,
  PRIMARY KEY (`RankingId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Ranking`
--

LOCK TABLES `Ranking` WRITE;
/*!40000 ALTER TABLE `Ranking` DISABLE KEYS */;
/*!40000 ALTER TABLE `Ranking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RecyclingDepot`
--

DROP TABLE IF EXISTS `RecyclingDepot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RecyclingDepot` (
  `RecyclingDepotId` int NOT NULL AUTO_INCREMENT,
  `DepotName` varchar(255) DEFAULT NULL,
  `Location` varchar(255) DEFAULT NULL,
  `IsWorking` bit(1) DEFAULT NULL,
  `UserId` int NOT NULL,
  `CreatedAt` datetime(6) DEFAULT NULL,
  `UpdatedAt` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`RecyclingDepotId`),
  KEY `UserId` (`UserId`),
  CONSTRAINT `RecyclingDepot_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `User` (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RecyclingDepot`
--

LOCK TABLES `RecyclingDepot` WRITE;
/*!40000 ALTER TABLE `RecyclingDepot` DISABLE KEYS */;
/*!40000 ALTER TABLE `RecyclingDepot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Resident`
--

DROP TABLE IF EXISTS `Resident`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Resident` (
  `ResidentId` int NOT NULL AUTO_INCREMENT,
  `UserId` int NOT NULL,
  `RewardPoints` int DEFAULT NULL,
  `ApartmentId` int DEFAULT NULL,
  `CreatedAt` datetime(6) DEFAULT NULL,
  `UpdatedAt` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`ResidentId`),
  KEY `UserId` (`UserId`),
  KEY `ApartmentId` (`ApartmentId`),
  CONSTRAINT `Resident_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `User` (`UserId`),
  CONSTRAINT `Resident_ibfk_2` FOREIGN KEY (`ApartmentId`) REFERENCES `Apartment` (`ApartmentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Resident`
--

LOCK TABLES `Resident` WRITE;
/*!40000 ALTER TABLE `Resident` DISABLE KEYS */;
/*!40000 ALTER TABLE `Resident` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Resident_Reward`
--

DROP TABLE IF EXISTS `Resident_Reward`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Resident_Reward` (
  `ResidentRewardId` int NOT NULL AUTO_INCREMENT,
  `ResidentId` int NOT NULL,
  `RewardId` int NOT NULL,
  `CreatedAt` date DEFAULT NULL,
  `UpdatedAt` date DEFAULT NULL,
  `Status` int DEFAULT NULL,
  PRIMARY KEY (`ResidentRewardId`),
  KEY `ResidentId` (`ResidentId`),
  KEY `RewardId` (`RewardId`),
  CONSTRAINT `Resident_Reward_ibfk_1` FOREIGN KEY (`ResidentId`) REFERENCES `Resident` (`ResidentId`),
  CONSTRAINT `Resident_Reward_ibfk_2` FOREIGN KEY (`RewardId`) REFERENCES `Reward` (`RewardId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Resident_Reward`
--

LOCK TABLES `Resident_Reward` WRITE;
/*!40000 ALTER TABLE `Resident_Reward` DISABLE KEYS */;
/*!40000 ALTER TABLE `Resident_Reward` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Reward`
--

DROP TABLE IF EXISTS `Reward`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Reward` (
  `RewardId` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(255) DEFAULT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `Point` int DEFAULT NULL,
  PRIMARY KEY (`RewardId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Reward`
--

LOCK TABLES `Reward` WRITE;
/*!40000 ALTER TABLE `Reward` DISABLE KEYS */;
/*!40000 ALTER TABLE `Reward` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User` (
  `UserId` int NOT NULL AUTO_INCREMENT,
  `Username` varchar(255) DEFAULT NULL,
  `Password` varchar(255) DEFAULT NULL,
  `Email` varchar(255) DEFAULT NULL,
  `EmailConfirmed` bit(1) DEFAULT NULL,
  `PhoneNumber` varchar(15) DEFAULT NULL,
  `FirstName` varchar(50) DEFAULT NULL,
  `LastName` varchar(50) DEFAULT NULL,
  `Address` varchar(255) DEFAULT NULL,
  `CreatedAt` datetime DEFAULT NULL,
  `UpdatedAt` datetime DEFAULT NULL,
  `IsActive` bit(1) DEFAULT NULL,
  `Role` enum('ROLE_RESIDENT','ROLE_COLLECTOR','ROLE_RECYCLING_DEPOT','ROLE_ADMIN') NOT NULL DEFAULT 'ROLE_RESIDENT',
  PRIMARY KEY (`UserId`),
  UNIQUE KEY `UK7xyi34i31xehayqd3heubave8` (`Email`),
  UNIQUE KEY `UKseh7nteifndaopocsq9f1w8ia` (`Username`),
  UNIQUE KEY `UKmwf067fe01yh2iw5lyjqgl9cl` (`PhoneNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,'admin','$2a$10$9WURl0XBBUNGi.BkBdo20eirQoVWhqT4ridutnz4EgAJjd6Pe5KFm',NULL,NULL,NULL,NULL,NULL,NULL,'2024-10-31 23:55:58','2024-10-31 23:55:58',_binary '','ROLE_ADMIN'),(2,'huy','$2a$10$ER7TnUxBDB5p9I9PAFn9meUI8CHtn1.WrtzVazv9ql9nfCM0rL.Za','abc@gmail.com',_binary '','0378760486','Nguyen','huy',NULL,'2024-11-02 16:08:37','2024-11-02 16:08:37',_binary '','ROLE_RESIDENT');
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User_Ranking`
--

DROP TABLE IF EXISTS `User_Ranking`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `User_Ranking` (
  `UserRankingId` int NOT NULL AUTO_INCREMENT,
  `UserId` int NOT NULL,
  `RankingId` int NOT NULL,
  `CreatedAt` date DEFAULT NULL,
  `UpdatedAt` date DEFAULT NULL,
  PRIMARY KEY (`UserRankingId`),
  KEY `UserId` (`UserId`),
  KEY `RankingId` (`RankingId`),
  CONSTRAINT `User_Ranking_ibfk_1` FOREIGN KEY (`UserId`) REFERENCES `User` (`UserId`),
  CONSTRAINT `User_Ranking_ibfk_2` FOREIGN KEY (`RankingId`) REFERENCES `Ranking` (`RankingId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User_Ranking`
--

LOCK TABLES `User_Ranking` WRITE;
/*!40000 ALTER TABLE `User_Ranking` DISABLE KEYS */;
/*!40000 ALTER TABLE `User_Ranking` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `WorkingDate`
--

DROP TABLE IF EXISTS `WorkingDate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `WorkingDate` (
  `WorkingDateId` int NOT NULL AUTO_INCREMENT,
  `Name` varchar(20) DEFAULT NULL,
  `Time` int DEFAULT NULL,
  PRIMARY KEY (`WorkingDateId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `WorkingDate`
--

LOCK TABLES `WorkingDate` WRITE;
/*!40000 ALTER TABLE `WorkingDate` DISABLE KEYS */;
/*!40000 ALTER TABLE `WorkingDate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tfu_user`
--

DROP TABLE IF EXISTS `tfu_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tfu_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tfu_user`
--

LOCK TABLES `tfu_user` WRITE;
/*!40000 ALTER TABLE `tfu_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `tfu_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-11-05 14:30:50
