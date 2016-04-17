/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.0.96-community-nt : Database - myresume
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`myresume` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `myresume`;

/*Table structure for table `education` */

DROP TABLE IF EXISTS `education`;

CREATE TABLE `education` (
  `id` int(11) NOT NULL auto_increment,
  `userId` int(11) NOT NULL,
  `time` varchar(100) default NULL,
  `school` varchar(218) default NULL,
  `examination` varchar(200) default NULL,
  `majorname` varchar(100) default NULL,
  `degree` varchar(2) default NULL,
  `createtime` datetime default NULL,
  `updatime` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `education` */

/*Table structure for table `evaluation` */

DROP TABLE IF EXISTS `evaluation`;

CREATE TABLE `evaluation` (
  `id` int(11) NOT NULL auto_increment,
  `userId` int(11) NOT NULL,
  `selfevaluation` varchar(500) default NULL,
  `careergoal` varchar(500) default NULL,
  `createtime` datetime default NULL,
  `updatime` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `evaluation` */

/*Table structure for table `job_intension` */

DROP TABLE IF EXISTS `job_intension`;

CREATE TABLE `job_intension` (
  `id` int(11) NOT NULL auto_increment,
  `userId` int(11) NOT NULL,
  `expworkingproperty` varchar(2) default NULL,
  `expworkplace` varchar(150) default NULL,
  `expworkindustry` varchar(100) default NULL,
  `expworkcareer` varchar(50) default NULL,
  `expmonthlysalary` varchar(50) default NULL,
  `workingstate` varchar(2) default NULL,
  `createtime` datetime default NULL,
  `updatetime` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `job_intension` */

/*Table structure for table `otherinfo` */

DROP TABLE IF EXISTS `otherinfo`;

CREATE TABLE `otherinfo` (
  `id` int(11) NOT NULL auto_increment,
  `userId` int(10) unsigned default NULL,
  `language` varchar(30) default NULL,
  `literacyskills` varchar(10) default NULL,
  `listeningspeaking` varchar(10) default NULL,
  `createtime` datetime default NULL,
  `updatime` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `otherinfo` */

/*Table structure for table `otherinfo1` */

DROP TABLE IF EXISTS `otherinfo1`;

CREATE TABLE `otherinfo1` (
  `id` int(11) NOT NULL auto_increment,
  `userId` int(11) NOT NULL,
  `certificate` varchar(80) default NULL,
  `certificatetime` varchar(30) default NULL,
  `createtime` datetime default NULL,
  `updatime` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `otherinfo1` */

/*Table structure for table `otherinfo2` */

DROP TABLE IF EXISTS `otherinfo2`;

CREATE TABLE `otherinfo2` (
  `id` int(11) NOT NULL auto_increment,
  `userId` int(11) NOT NULL,
  `title` varchar(100) default NULL,
  `description` varchar(500) default NULL,
  `createtime` datetime default NULL,
  `updatime` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `otherinfo2` */

/*Table structure for table `training` */

DROP TABLE IF EXISTS `training`;

CREATE TABLE `training` (
  `id` int(11) NOT NULL auto_increment,
  `userId` int(11) NOT NULL,
  `time` varchar(100) default NULL,
  `trainingorganization` varchar(200) default NULL,
  `trainingclass` varchar(200) default NULL,
  `certificate` varchar(200) default NULL,
  `description` varchar(500) default NULL,
  `createtime` datetime default NULL,
  `updatime` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `training` */

/*Table structure for table `user_baseinfo` */

DROP TABLE IF EXISTS `user_baseinfo`;

CREATE TABLE `user_baseinfo` (
  `id` int(11) NOT NULL auto_increment,
  `userId` int(11) NOT NULL,
  `realname` varchar(50) NOT NULL,
  `gender` varchar(2) default NULL,
  `brithday` varchar(30) default NULL,
  `joinworktime` varchar(30) default NULL,
  `phone` varchar(11) NOT NULL,
  `hometown` varchar(200) default NULL,
  `city` varchar(200) default NULL,
  `email` varchar(100) NOT NULL,
  `ismarry` varchar(2) default NULL,
  `nationality` varchar(200) default NULL,
  `license` varchar(30) default NULL,
  `workingabroad` varchar(2) default NULL,
  `politicalstatus` varchar(2) default NULL,
  `avator` varchar(150) default NULL,
  `updatime` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_baseinfo` */

/*Table structure for table `user_login` */

DROP TABLE IF EXISTS `user_login`;

CREATE TABLE `user_login` (
  `id` int(11) NOT NULL auto_increment,
  `username` varchar(50) NOT NULL,
  `password` varchar(30) NOT NULL,
  `deviceId` varchar(30) default NULL,
  `patform` varchar(30) default NULL,
  `createtime` datetime default NULL,
  `lastlogintime` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_login` */

/*Table structure for table `work_experience` */

DROP TABLE IF EXISTS `work_experience`;

CREATE TABLE `work_experience` (
  `id` int(11) NOT NULL auto_increment,
  `userId` int(11) NOT NULL,
  `companyname` varchar(100) character set utf8 collate utf8_hungarian_ci NOT NULL,
  `industryclassification` varchar(128) character set utf8 collate utf8_hungarian_ci NOT NULL,
  `jobtitle` varchar(100) character set utf8 collate utf8_hungarian_ci NOT NULL,
  `worktimeStart` varchar(30) character set utf8 collate utf8_hungarian_ci NOT NULL,
  `worktimeEnd` varchar(30) character set utf8 collate utf8_hungarian_ci NOT NULL,
  `expectedsalary` varchar(100) character set utf8 collate utf8_hungarian_ci NOT NULL,
  `workdesc` varchar(500) character set utf8 collate utf8_hungarian_ci default NULL,
  `createtime` datetime default NULL,
  `updatime` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `work_experience` */

/* Procedure structure for procedure `pro_workexpericnce` */

/*!50003 DROP PROCEDURE IF EXISTS  `pro_workexpericnce` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `pro_workexpericnce`(IN `install` VARCHAR(2),IN p_weId INT,IN p_userId INT,IN p_companyname VARCHAR(100),
							   IN p_industryclassification VARCHAR(128),IN p_jobtitle VARCHAR(100),IN p_worktimeStart VARCHAR(30),
							   IN p_worktimeEnd VARCHAR(30),IN p_expectedsalary VARCHAR(100),IN p_workdesc VARCHAR(500))
BEGIN
    IF `install` = 1
    THEN  
	SELECT * from `work_experience` where `userId` = p_userId;
    ELSEIF `install` = 2  
    THEN  
	INSERT INTO `work_experience` (`userId`,`companyname`,`industryclassification`,`jobtitle`,`worktimeStart`,`worktimeEnd`,`expectedsalary`,`workdesc`,`createtime`,`updatime`) 
	VALUES(p_userId,p_companyname,p_industryclassification,p_jobtitle,p_worktimeStart,p_worktimeEnd,p_expectedsalary,p_workdesc,NOW(),NOW());
	SELECT '200' AS msg;
     ELSEIF `install` = 3
     THEN  
	update `work_experience` set `companyname` = p_companyname,`industryclassification` = p_industryclassification,
		`jobtitle` = p_jobtitle,`worktimeStart` = p_worktimeStart,`worktimeEnd` = p_worktimeEnd,
		`expectedsalary` = p_expectedsalary,`workdesc` = p_workdesc,`updatime` = now()
	where `userId` = p_userId and `id` = p_weId;
	SELECT '200' AS msg;
     ELSEIF `install` = 4
     then
	delete from `work_experience` where `userId` = p_userId AND `id` = p_weId;
	SELECT '200' AS msg;
    END IF;  
END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
