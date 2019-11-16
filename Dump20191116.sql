CREATE DATABASE  IF NOT EXISTS `Flights` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `Flights`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: cs336db.cevjd6qfgzlu.us-east-2.rds.amazonaws.com    Database: Flights
-- ------------------------------------------------------
-- Server version	5.7.22-log

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
-- Dumping data for table `reset_password`
--

LOCK TABLES `reset_password` WRITE;
/*!40000 ALTER TABLE `reset_password` DISABLE KEYS */;
INSERT INTO `reset_password` VALUES ('bhanutej.onteru@gmail.com','1e6b7bda-c4bc-4616-b835-dfdaaebf9d93','2019-11-16 12:09:05'),('devnandol@gmail.com','af12f22c-47f6-490d-a3a7-123e71e71b37','2019-11-15 23:26:40');
/*!40000 ALTER TABLE `reset_password` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'YOGESH KUMAR GUPTA','ugeshgupta000@gmail.com',NULL,'user','654bb7807d93ca4992c2e9bee3aeb706a2a9e602fc9c2913c1cb1b325529772e1fa4e0fd1ad26548da88f65e974fd8d59a8939d1d6833d344c31cd4ea8361b40'),(2,'PAVAN','pppatel@gmail.com',NULL,'user','9e0a04bb1a1e56ee9311e0d3bd105efb25d714c69c73250c3fef027b2ce72351d452a8a7b23b3132ff0531ae1c55d584a8bcc2bfb6da4dce19cab90bb7aae19c'),(3,'DEV','devnandol@gmail.com',NULL,'user','05dc7fa4c7cff096f6ced0dc99e2916bb404d0309bb4c26ef4755e54aeff02c7137e2a771d0615377011836c1059eeea7ca507d473830d2dc24c414f223773ac'),(4,'BHANU','bhanutej.onteru@gmail.com',NULL,'user','bb892e44fb6a7ba25dd27d0e582cf7fea6a085d5c82a8a50a0a6c444a6b245d1004928c30abb3e72a71e6bc0ef1008cabd7976ff1f15e96a40708ef9c148cba9'),(5,'PAVAN','ppatel2gmail.com',NULL,'user','f20d8f7c7ece267a6cf3e4ec014130e638679551dbd870f751bf1c1e89b72aa84d43ffd7e663dc2aaa018a5b41e775542d12db12faa84da8293939daa6d63f7b'),(6,'PAVAN','ppatel@gmail.com',NULL,'user','f20d8f7c7ece267a6cf3e4ec014130e638679551dbd870f751bf1c1e89b72aa84d43ffd7e663dc2aaa018a5b41e775542d12db12faa84da8293939daa6d63f7b');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'Flights'
--

--
-- Dumping routines for database 'Flights'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-11-16 22:56:09
