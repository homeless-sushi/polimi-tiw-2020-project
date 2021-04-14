-- MySQL dump 10.13  Distrib 8.0.23, for Linux (x86_64)
--
-- Host: localhost    Database: tiw-project
-- ------------------------------------------------------
-- Server version	8.0.23-0ubuntu0.20.04.1

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
-- Current Database: `tiw-project`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `tiw-project` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `tiw-project`;

--
-- Table structure for table `attend`
--

DROP TABLE IF EXISTS `attend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attend` (
  `student_id` int unsigned NOT NULL,
  `course_id` int unsigned NOT NULL,
  `year` year NOT NULL,
  PRIMARY KEY (`student_id`,`course_id`,`year`),
  KEY `fk_attend_2_idx` (`course_id`,`year`),
  CONSTRAINT `fk_course` FOREIGN KEY (`course_id`, `year`) REFERENCES `course_details` (`course_id`, `year`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_student_id` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `course`
--

DROP TABLE IF EXISTS `course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `cfu` int unsigned NOT NULL DEFAULT '0',
  `semester` enum('1','2') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `course_details`
--

DROP TABLE IF EXISTS `course_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course_details` (
  `course_id` int unsigned NOT NULL,
  `year` year NOT NULL,
  `professor_id` int unsigned DEFAULT NULL,
  PRIMARY KEY (`course_id`,`year`),
  KEY `fk_course_detail_1_idx` (`professor_id`),
  CONSTRAINT `fk_course_id` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_professor_id` FOREIGN KEY (`professor_id`) REFERENCES `professor` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `exam`
--

DROP TABLE IF EXISTS `exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exam` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `course_id` int unsigned NOT NULL,
  `year` year NOT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `fk_course_idx` (`course_id`,`year`,`date`),
  CONSTRAINT `fk_course_exam` FOREIGN KEY (`course_id`, `year`) REFERENCES `course_details` (`course_id`, `year`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `exam_record`
--

DROP TABLE IF EXISTS `exam_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exam_record` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `exam_id` int unsigned NOT NULL,
  `time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_exam_record_1_idx` (`exam_id`),
  CONSTRAINT `fk_exam_record_1` FOREIGN KEY (`exam_id`) REFERENCES `exam` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `exam_registration`
--

DROP TABLE IF EXISTS `exam_registration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `exam_registration` (
  `exam_id` int unsigned NOT NULL,
  `student_id` int unsigned NOT NULL,
  `status` enum('NINS','INS','PUB','RIF','VERB') NOT NULL DEFAULT 'NINS',
  `result` enum('VUOTO','ASS','RM','RP','PASS') NOT NULL DEFAULT 'VUOTO',
  `grade` tinyint unsigned NOT NULL DEFAULT '0',
  `laude` tinyint unsigned NOT NULL DEFAULT '0',
  `repr` char(5) GENERATED ALWAYS AS ((case when (`result` = _utf8mb4'PASS') then convert((case when (`laude` <> 0) then concat(`grade`,_utf8mb4'L') else `grade` end) using utf8mb4) else `result` end)) VIRTUAL,
  `record_id` int unsigned DEFAULT NULL,
  PRIMARY KEY (`exam_id`,`student_id`),
  KEY `fk_exam_registration_2_idx` (`student_id`),
  KEY `fk_exam_registration_3_idx` (`record_id`),
  CONSTRAINT `fk_exam_registration_1` FOREIGN KEY (`exam_id`) REFERENCES `exam` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_exam_registration_2` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_exam_registration_3` FOREIGN KEY (`record_id`) REFERENCES `exam_record` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `chk_grade_ass_eq_0` CHECK (((`result` <> _utf8mb4'ASS') or (`grade` = 0))),
  CONSTRAINT `chk_grade_laude_eq_30` CHECK (((`laude` = 0) or (`grade` = 30))),
  CONSTRAINT `chk_grade_max_30` CHECK ((`grade` <= 30)),
  CONSTRAINT `chk_grade_pass_gte_18` CHECK (((`result` <> _utf8mb4'PASS') or (`grade` >= 18))),
  CONSTRAINT `chk_grade_vuoto_eq_0` CHECK (((`result` <> _utf8mb4'VUOTO') or (`grade` = 0))),
  CONSTRAINT `chk_rim_if_rif` CHECK (((`status` <> _utf8mb4'RIF') or (`result` = _utf8mb4'RM'))),
  CONSTRAINT `chk_verb_has_record` CHECK ((((`record_id` is not null) and (`status` = _utf8mb4'VERB')) or ((`record_id` is null) and (`status` <> _utf8mb4'VERB')))),
  CONSTRAINT `chk_vuoto_eq_nins` CHECK ((((`result` = _utf8mb4'VUOTO') and (`status` = _utf8mb4'NINS')) or ((`result` <> _utf8mb4'VUOTO') and (`status` <> _utf8mb4'NINS'))))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `exam_unrecorded`
--

DROP TABLE IF EXISTS `exam_unrecorded`;
/*!50001 DROP VIEW IF EXISTS `exam_unrecorded`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `exam_unrecorded` AS SELECT 
 1 AS `exam_id`,
 1 AS `student_id`,
 1 AS `status`,
 1 AS `result`,
 1 AS `grade`,
 1 AS `laude`,
 1 AS `repr`,
 1 AS `record_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `major`
--

DROP TABLE IF EXISTS `major`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `major` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `professor`
--

DROP TABLE IF EXISTS `professor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `professor` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `person_code` int(8) unsigned zerofill NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_professor_1_idx` (`person_code`),
  CONSTRAINT `fk_professor_is_user` FOREIGN KEY (`person_code`) REFERENCES `user` (`person_code`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `student`
--

DROP TABLE IF EXISTS `student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `person_code` int(8) unsigned zerofill NOT NULL,
  `major_id` int unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_student_1_idx` (`person_code`),
  KEY `major` (`major_id`),
  CONSTRAINT `fk_student_is_user` FOREIGN KEY (`person_code`) REFERENCES `user` (`person_code`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_student_registered_to_major` FOREIGN KEY (`major_id`) REFERENCES `major` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `person_code` int(8) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `email` varchar(254) NOT NULL,
  `password` char(60) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
  `name` varchar(64) NOT NULL,
  `surname` varchar(64) NOT NULL,
  PRIMARY KEY (`person_code`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `user_career`
--

DROP TABLE IF EXISTS `user_career`;
/*!50001 DROP VIEW IF EXISTS `user_career`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `user_career` AS SELECT 
 1 AS `person_code`,
 1 AS `id`,
 1 AS `role`,
 1 AS `major`*/;
SET character_set_client = @saved_cs_client;

--
-- Current Database: `tiw-project`
--

USE `tiw-project`;

--
-- Final view structure for view `exam_unrecorded`
--

/*!50001 DROP VIEW IF EXISTS `exam_unrecorded`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `exam_unrecorded` AS select `exam_registration`.`exam_id` AS `exam_id`,`exam_registration`.`student_id` AS `student_id`,`exam_registration`.`status` AS `status`,`exam_registration`.`result` AS `result`,`exam_registration`.`grade` AS `grade`,`exam_registration`.`laude` AS `laude`,`exam_registration`.`repr` AS `repr`,`exam_registration`.`record_id` AS `record_id` from `exam_registration` where (`exam_registration`.`status` <> 'VERB') */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `user_career`
--

/*!50001 DROP VIEW IF EXISTS `user_career`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `user_career` AS select `professor`.`person_code` AS `person_code`,`professor`.`id` AS `id`,'professor' AS `role`,NULL AS `major` from `professor` union select `student`.`person_code` AS `person_code`,`student`.`id` AS `id`,'student' AS `role`,`major`.`name` AS `major` from (`student` join `major` on((`student`.`major_id` = `major`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-04-15 11:53:12
