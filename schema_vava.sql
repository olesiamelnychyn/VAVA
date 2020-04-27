-- MySQL dump 10.13  Distrib 8.0.19, for macos10.15 (x86_64)
--
-- Host: localhost    Database: restaurant_net
-- ------------------------------------------------------
-- Server version	8.0.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cheque`
--

DROP TABLE IF EXISTS `cheque`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cheque` (
  `id` int NOT NULL AUTO_INCREMENT,
  `rest_id` int DEFAULT NULL,
  `meal_id` int DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rest_id` (`rest_id`),
  KEY `meal_id` (`meal_id`),
  CONSTRAINT `cheque_ibfk_1` FOREIGN KEY (`rest_id`) REFERENCES `restaurant` (`id`),
  CONSTRAINT `cheque_ibfk_2` FOREIGN KEY (`meal_id`) REFERENCES `meal` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000001 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `emp_reserv`
--

DROP TABLE IF EXISTS `emp_reserv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `emp_reserv` (
  `id` int NOT NULL AUTO_INCREMENT,
  `reserv_id` int DEFAULT NULL,
  `emp_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `reserv_id` (`reserv_id`),
  KEY `emp_id` (`emp_id`),
  CONSTRAINT `emp_reserv_ibfk_1` FOREIGN KEY (`reserv_id`) REFERENCES `reservation` (`id`),
  CONSTRAINT `emp_reserv_ibfk_2` FOREIGN KEY (`emp_id`) REFERENCES `employee` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=80080 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `rest_id` int DEFAULT NULL,
  `first_name` varchar(20) NOT NULL,
  `last_name` varchar(20) NOT NULL,
  `gender` char(1) NOT NULL,
  `birthdate` date NOT NULL,
  `phone` char(13) NOT NULL,
  `e_mail` varchar(320) NOT NULL,
  `position` varchar(30) NOT NULL,
  `wage` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rest_id` (`rest_id`),
  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`rest_id`) REFERENCES `restaurant` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=651 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `meal`
--

DROP TABLE IF EXISTS `meal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meal` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(40) DEFAULT NULL,
  `price` decimal(6,2) DEFAULT NULL,
  `prep_time` time DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `meal_price` (`price`),
  KEY `meal_time` (`prep_time`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `meal_product`
--

DROP TABLE IF EXISTS `meal_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meal_product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `meal_id` int DEFAULT NULL,
  `prod_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `prod_id` (`prod_id`),
  KEY `meal_id` (`meal_id`),
  CONSTRAINT `meal_product_ibfk_1` FOREIGN KEY (`prod_id`) REFERENCES `product` (`id`),
  CONSTRAINT `meal_product_ibfk_2` FOREIGN KEY (`meal_id`) REFERENCES `meal` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=80013 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `meal_reserv`
--

DROP TABLE IF EXISTS `meal_reserv`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meal_reserv` (
  `id` int NOT NULL AUTO_INCREMENT,
  `reserv_id` int DEFAULT NULL,
  `meal_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `reserv_id` (`reserv_id`),
  KEY `meal_id` (`meal_id`),
  CONSTRAINT `meal_reserv_ibfk_1` FOREIGN KEY (`reserv_id`) REFERENCES `reservation` (`id`),
  CONSTRAINT `meal_reserv_ibfk_2` FOREIGN KEY (`meal_id`) REFERENCES `meal` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=80023 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `meal_rest`
--

DROP TABLE IF EXISTS `meal_rest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `meal_rest` (
  `id` int NOT NULL AUTO_INCREMENT,
  `rest_id` int DEFAULT NULL,
  `meal_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rest_id` (`rest_id`),
  KEY `meal_id` (`meal_id`),
  CONSTRAINT `meal_rest_ibfk_1` FOREIGN KEY (`rest_id`) REFERENCES `restaurant` (`id`),
  CONSTRAINT `meal_rest_ibfk_2` FOREIGN KEY (`meal_id`) REFERENCES `meal` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=82528 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(40) DEFAULT NULL,
  `price` decimal(6,2) DEFAULT NULL,
  `supp_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `supp_id` (`supp_id`),
  CONSTRAINT `product_ibfk_1` FOREIGN KEY (`supp_id`) REFERENCES `supplier` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `rest_id` int DEFAULT NULL,
  `date_start` datetime DEFAULT NULL,
  `date_end` datetime DEFAULT NULL,
  `visitors` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rest_id` (`rest_id`),
  CONSTRAINT `reservation_ibfk_1` FOREIGN KEY (`rest_id`) REFERENCES `restaurant` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1010 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `restaurant`
--

DROP TABLE IF EXISTS `restaurant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurant` (
  `id` int NOT NULL AUTO_INCREMENT,
  `zip` varchar(10) DEFAULT NULL,
  `capacity` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `zip` (`zip`),
  KEY `rest_capacity` (`capacity`),
  CONSTRAINT `restaurant_ibfk_1` FOREIGN KEY (`zip`) REFERENCES `zip` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=87001 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(30) DEFAULT NULL,
  `phone` char(13) DEFAULT NULL,
  `e_mail` varchar(320) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=151 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `zip`
--

DROP TABLE IF EXISTS `zip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zip` (
  `code` varchar(10) NOT NULL,
  `state` char(2) NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-04-27 12:39:55
