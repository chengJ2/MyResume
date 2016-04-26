/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.0.51b-community-nt-log : Database - myresume
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

/*Table structure for table `apkupdate` */

DROP TABLE IF EXISTS `apkupdate`;

CREATE TABLE `apkupdate` (
  `APKName` varchar(50) default NULL,
  `Version` varchar(10) default NULL,
  `DownloadPath` varchar(150) default NULL,
  `UpContent` varchar(1000) default NULL,
  `Updatime` varchar(30) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `apkupdate` */

insert  into `apkupdate`(`APKName`,`Version`,`DownloadPath`,`UpContent`,`Updatime`) values ('MyResume','1','/ApkFile/resume.apk','&lt;br&gt; -  调整了部分界面,购物车查询页，交通管制页;&lt;br&gt;&lt;br&gt; -  增加了部分功能，相关切图已更新;&lt;br&gt; &lt;br&gt;-  优化了部分功能;&lt;br&gt; &lt;br&gt;-  调整个人中心界面部分功能，相关切图已更新;&lt;br&gt;&lt;br&gt;','2016-5-10');

/*Table structure for table `city` */

DROP TABLE IF EXISTS `city`;

CREATE TABLE `city` (
  `cityID` int(11) NOT NULL,
  `cityName` varchar(50) NOT NULL,
  `proID` int(11) default NULL,
  PRIMARY KEY  (`cityName`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `city` */

insert  into `city`(`cityID`,`cityName`,`proID`) values (1,'北京',1),(1,'天津',2),(1,'上海',3),(1,'重庆',4),(1,'石家庄',5),(2,'唐山',5),(3,'秦皇岛',5),(4,'邯郸',5),(5,'邢台',5),(6,'保定',5),(7,'张家口',5),(8,'承德',5),(9,'沧州',5),(10,'廊坊',5),(11,'衡水',5),(1,'太原',6),(2,'大同',6),(3,'阳泉',6),(4,'长治',6),(5,'晋城',6),(6,'朔州',6),(7,'晋中',6),(8,'运城',6),(9,'忻州',6),(10,'临汾',6),(11,'吕梁',6),(1,'台北',7),(2,'高雄',7),(3,'基隆',7),(4,'台中',7),(5,'台南',7),(6,'新竹',7),(7,'嘉义',7),(9,'宜兰',7),(10,'桃园',7),(12,'苗栗',7),(14,'彰化',7),(15,'南投',7),(16,'云林',7),(20,'屏东',7),(21,'澎湖',7),(22,'台东',7),(23,'花莲',7),(1,'沈阳',8),(2,'大连',8),(3,'鞍山',8),(4,'抚顺',8),(5,'本溪',8),(6,'丹东',8),(7,'锦州',8),(8,'营口',8),(9,'阜新',8),(10,'辽阳',8),(11,'盘锦',8),(12,'铁岭',8),(13,'朝阳',8),(14,'葫芦岛',8),(1,'长春',9),(2,'吉林',9),(3,'四平',9),(4,'辽源',9),(5,'通化',9),(6,'白山',9),(7,'松原',9),(8,'白城',9),(9,'延边',9),(1,'哈尔滨',10),(2,'齐齐哈尔',10),(3,'鹤岗',10),(4,'双鸭山',10),(5,'鸡西',10),(6,'大庆',10),(7,'伊春',10),(8,'牡丹江',10),(9,'佳木斯',10),(10,'七台河',10),(11,'黑河',10),(12,'绥化',10),(13,'大兴安岭',10),(1,'南京',11),(2,'无锡',11),(3,'徐州',11),(4,'常州',11),(5,'苏州',11),(6,'南通',11),(7,'连云港',11),(8,'淮安',11),(9,'盐城',11),(10,'扬州',11),(11,'镇江',11),(12,'泰州',11),(13,'宿迁',11),(1,'杭州',12),(2,'宁波',12),(3,'温州',12),(4,'嘉兴',12),(5,'湖州',12),(6,'绍兴',12),(7,'金华',12),(8,'衢州',12),(9,'舟山',12),(10,'台州',12),(11,'丽水',12),(1,'合肥',13),(2,'芜湖',13),(3,'蚌埠',13),(4,'淮南',13),(5,'马鞍山',13),(6,'淮北',13),(7,'铜陵',13),(8,'安庆',13),(9,'黄山',13),(10,'滁州',13),(11,'阜阳',13),(12,'宿州',13),(13,'巢湖',13),(14,'六安',13),(15,'亳州',13),(16,'池州',13),(17,'宣城',13),(1,'福州',14),(2,'厦门',14),(3,'莆田',14),(4,'三明',14),(5,'泉州',14),(6,'漳州',14),(7,'南平',14),(8,'龙岩',14),(9,'宁德',14),(1,'南昌',15),(2,'景德镇',15),(3,'萍乡',15),(4,'九江',15),(5,'新余',15),(6,'鹰潭',15),(7,'赣州',15),(8,'吉安',15),(9,'宜春',15),(10,'抚州',15),(11,'上饶',15),(1,'济南',16),(2,'青岛',16),(3,'淄博',16),(4,'枣庄',16),(5,'东营',16),(6,'烟台',16),(7,'潍坊',16),(8,'济宁',16),(9,'泰安',16),(10,'威海',16),(11,'日照',16),(12,'莱芜',16),(13,'临沂',16),(14,'德州',16),(15,'聊城',16),(16,'滨州',16),(17,'菏泽',16),(1,'郑州',17),(2,'开封',17),(3,'洛阳',17),(4,'平顶山',17),(5,'安阳',17),(6,'鹤壁',17),(7,'新乡',17),(8,'焦作',17),(9,'濮阳',17),(10,'许昌',17),(11,'漯河',17),(12,'三门峡',17),(13,'南阳',17),(14,'商丘',17),(15,'信阳',17),(16,'周口',17),(17,'驻马店',17),(18,'济源',17),(1,'武汉',18),(2,'黄石',18),(3,'十堰',18),(4,'荆州',18),(5,'宜昌',18),(6,'襄樊',18),(7,'鄂州',18),(8,'荆门',18),(9,'孝感',18),(10,'黄冈',18),(11,'咸宁',18),(12,'随州',18),(13,'仙桃',18),(14,'天门',18),(15,'潜江',18),(16,'神农架',18),(17,'恩施',18),(1,'长沙',19),(2,'株洲',19),(3,'湘潭',19),(4,'衡阳',19),(5,'邵阳',19),(6,'岳阳',19),(7,'常德',19),(8,'张家界',19),(9,'益阳',19),(10,'郴州',19),(11,'永州',19),(12,'怀化',19),(13,'娄底',19),(14,'湘西',19),(1,'广州',20),(2,'深圳',20),(3,'珠海',20),(4,'汕头',20),(5,'韶关',20),(6,'佛山',20),(7,'江门',20),(8,'湛江',20),(9,'茂名',20),(10,'肇庆',20),(11,'惠州',20),(12,'梅州',20),(13,'汕尾',20),(14,'河源',20),(15,'阳江',20),(16,'清远',20),(17,'东莞',20),(18,'中山',20),(19,'潮州',20),(20,'揭阳',20),(21,'云浮',20),(1,'兰州',21),(2,'金昌',21),(3,'白银',21),(4,'天水',21),(5,'嘉峪关',21),(6,'武威',21),(7,'张掖',21),(8,'平凉',21),(9,'酒泉',21),(10,'庆阳',21),(11,'定西',21),(12,'陇南',21),(13,'临夏',21),(14,'甘南',21),(1,'成都',22),(2,'自贡',22),(3,'攀枝花',22),(4,'泸州',22),(5,'德阳',22),(6,'绵阳',22),(7,'广元',22),(8,'遂宁',22),(9,'内江',22),(10,'乐山',22),(11,'南充',22),(12,'眉山',22),(13,'宜宾',22),(14,'广安',22),(15,'达州',22),(16,'雅安',22),(17,'巴中',22),(18,'资阳',22),(19,'阿坝',22),(20,'甘孜',22),(21,'凉山',22),(1,'贵阳',24),(2,'六盘水',24),(3,'遵义',24),(4,'安顺',24),(5,'铜仁',24),(6,'毕节',24),(7,'黔西南',24),(8,'黔东南',24),(9,'黔南',24),(1,'海口',25),(2,'三亚',25),(3,'五指山',25),(4,'琼海',25),(5,'儋州',25),(6,'文昌',25),(7,'万宁',25),(8,'东方',25),(9,'澄迈',25),(10,'定安',25),(11,'屯昌',25),(12,'临高',25),(13,'白沙',25),(14,'昌江',25),(15,'乐东',25),(16,'陵水',25),(17,'保亭',25),(18,'琼中',25),(1,'昆明',26),(2,'曲靖',26),(3,'玉溪',26),(4,'保山',26),(5,'昭通',26),(6,'丽江',26),(7,'思茅',26),(8,'临沧',26),(9,'文山',26),(10,'红河',26),(11,'西双版纳',26),(12,'楚雄',26),(13,'大理',26),(14,'德宏',26),(15,'怒江',26),(16,'迪庆',26),(1,'西宁',27),(2,'海东',27),(3,'海北',27),(4,'黄南',27),(5,'海南',27),(6,'果洛',27),(7,'玉树',27),(8,'海西',27),(1,'西安',28),(2,'铜川',28),(3,'宝鸡',28),(4,'咸阳',28),(5,'渭南',28),(6,'延安',28),(7,'汉中',28),(8,'榆林',28),(9,'安康',28),(10,'商洛',28),(1,'南宁',29),(2,'柳州',29),(3,'桂林',29),(5,'北海',29),(6,'防城港',29),(7,'钦州',29),(8,'贵港',29),(9,'玉林',29),(10,'百色',29),(11,'贺州',29),(12,'河池',29),(13,'来宾',29),(14,'崇左',29),(1,'拉萨',30),(2,'那曲',30),(3,'昌都',30),(4,'山南',30),(5,'日喀则',30),(6,'阿里',30),(7,'林芝',30),(1,'银川',31),(2,'石嘴山',31),(3,'吴忠',31),(4,'固原',31),(5,'中卫',31),(1,'乌鲁木齐',32),(2,'克拉玛依',32),(3,'石河子　',32),(4,'阿拉尔',32),(5,'图木舒克',32),(6,'五家渠',32),(7,'吐鲁番',32),(8,'阿克苏',32),(9,'喀什',32),(10,'哈密',32),(11,'和田',32),(12,'阿图什',32),(13,'库尔勒',32),(14,'昌吉　',32),(15,'阜康',32),(16,'米泉',32),(17,'博乐',32),(18,'伊宁',32),(19,'奎屯',32),(20,'塔城',32),(21,'乌苏',32),(22,'阿勒泰',32),(1,'呼和浩特',33),(2,'包头',33),(3,'乌海',33),(4,'赤峰',33),(5,'通辽',33),(6,'鄂尔多斯',33),(7,'呼伦贝尔',33),(8,'巴彦淖尔',33),(9,'乌兰察布',33),(10,'锡林郭勒盟',33),(11,'兴安盟',33),(12,'阿拉善盟',33),(1,'澳门',34),(1,'香港',35);

/*Table structure for table `district` */

DROP TABLE IF EXISTS `district`;

CREATE TABLE `district` (
  `id` int(11) default NULL,
  `disName` varchar(80) default NULL,
  `cityID` int(11) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `district` */

/*Table structure for table `education` */

DROP TABLE IF EXISTS `education`;

CREATE TABLE `education` (
  `id` int(11) NOT NULL auto_increment,
  `userId` int(11) NOT NULL,
  `educationtimestart` varchar(30) default NULL,
  `educationtimeend` varchar(30) default NULL,
  `school` varchar(218) default NULL,
  `examination` varchar(200) default NULL,
  `majorname` varchar(100) default NULL,
  `degree` varchar(2) default NULL,
  `background` varchar(20) default NULL,
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
  `background` varchar(20) default NULL,
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
  `background` varchar(20) default NULL,
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
  `background` varchar(20) default NULL,
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

/*Table structure for table `promary` */

DROP TABLE IF EXISTS `promary`;

CREATE TABLE `promary` (
  `proID` int(11) NOT NULL,
  `proName` varchar(50) NOT NULL,
  PRIMARY KEY  (`proID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `promary` */

insert  into `promary`(`proID`,`proName`) values (1,'北京'),(2,'天津'),(3,'上海'),(4,'重庆'),(5,'河北'),(6,'山西'),(7,'台湾'),(8,'辽宁'),(9,'吉林'),(10,'黑龙江'),(11,'江苏'),(12,'浙江'),(13,'安徽'),(14,'福建'),(15,'江西'),(16,'山东'),(17,'河南'),(18,'湖北'),(19,'湖南'),(20,'广东'),(21,'甘肃'),(22,'四川'),(24,'贵州'),(25,'海南'),(26,'云南'),(27,'青海'),(28,'陕西'),(29,'广西'),(30,'西藏'),(31,'宁夏'),(32,'新疆'),(33,'内蒙古'),(34,'澳门'),(35,'香港');

/*Table structure for table `training` */

DROP TABLE IF EXISTS `training`;

CREATE TABLE `training` (
  `id` int(11) NOT NULL auto_increment,
  `userId` int(11) NOT NULL,
  `trainingtimeend` varchar(30) default NULL,
  `trainingtimestart` varchar(30) default NULL,
  `trainingorganization` varchar(200) default NULL,
  `trainingclass` varchar(200) default NULL,
  `certificate` varchar(200) default NULL,
  `description` varchar(500) default NULL,
  `background` varchar(20) default NULL,
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
  `background` varchar(20) default NULL,
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
  `deviceId` varchar(50) default NULL,
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
  `companyname` varchar(100) collate utf8_hungarian_ci NOT NULL,
  `industryclassification` varchar(128) collate utf8_hungarian_ci NOT NULL,
  `jobtitle` varchar(100) collate utf8_hungarian_ci NOT NULL,
  `worktimeStart` varchar(30) collate utf8_hungarian_ci NOT NULL,
  `worktimeEnd` varchar(30) collate utf8_hungarian_ci NOT NULL,
  `expectedsalary` varchar(100) collate utf8_hungarian_ci NOT NULL,
  `workdesc` varchar(500) collate utf8_hungarian_ci default NULL,
  `background` varchar(20) collate utf8_hungarian_ci default NULL,
  `createtime` datetime default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_hungarian_ci;

/*Data for the table `work_experience` */

insert  into `work_experience`(`id`,`userId`,`companyname`,`industryclassification`,`jobtitle`,`worktimeStart`,`worktimeEnd`,`expectedsalary`,`workdesc`,`background`,`createtime`) values (1,1,'adwr','qwr','asfasdfe23423sadvsdv','互联网/电子商务','2014-01-12','2015-11-12','4001-6000元/月',NULL,'2016-04-09 17:59:51');

/* Procedure structure for procedure `pro_education` */

/*!50003 DROP PROCEDURE IF EXISTS  `pro_education` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `pro_education`(IN `install` VARCHAR(2),IN p_edId INT,IN p_userId INT,IN p_worktimestart VARCHAR(30),in p_worktimeend varchar(30),
					in p_school varchar(218),in p_examination varchar(200),in p_majorname varchar(100),in p_degree varchar(2))
BEGIN
	 IF `install` = 1
	    THEN  
		SELECT * FROM `education` WHERE `userId` = p_edId;
	 ELSEIF `install` = 2 
	    Then
		insert into `education`(`userId`,`worktimestart`,`worktimeend`,`school`,`examination`,`majorname`,`degree`,`createtime`,`updatime`)
		values(p_userId,p_worktimestart,p_worktimeend,p_school,p_examination,p_majorname,p_degree,now(),now()); 
	 elseif `install` = 3
	   then
		update `education` 
		set `worktimestart` = p_worktimestart,`worktimeend` = p_worktimeend,
		   `school` = p_school,`examination` = p_examination,`majorname` = p_majorname,`degree` = p_degree,`updatime` = now()
		  WHERE `userId` = p_edId AND `id` = p_edId;
		SELECT '200' AS msg;
	  ELSEIF `install` = 4
	  then
		delete from `education` where `userId` = p_edId AND `id` = p_edId;
		SELECT '200' AS msg;
	  END IF;
    END */$$
DELIMITER ;

/* Procedure structure for procedure `pro_getapp_info` */

/*!50003 DROP PROCEDURE IF EXISTS  `pro_getapp_info` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `pro_getapp_info`(IN `install` VARCHAR(2))
BEGIN
      IF `install` = 1
	THEN
		SELECT * FROM `apkupdate` ORDER BY updatime DESC LIMIT 0,1;
	END IF;
		
    END */$$
DELIMITER ;

/* Procedure structure for procedure `pro_login_out` */

/*!50003 DROP PROCEDURE IF EXISTS  `pro_login_out` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `pro_login_out`(IN `install` VARCHAR(2),IN p_userId int)
BEGIN
	
	UPDATE `user_login` SET `lastlogintime` = NOW() WHERE `id`= p_userId;	
	
    END */$$
DELIMITER ;

/* Procedure structure for procedure `pro_user_login` */

/*!50003 DROP PROCEDURE IF EXISTS  `pro_user_login` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `pro_user_login`(IN `install` VARCHAR(2),IN p_username VARCHAR(50),IN p_userpwd VARCHAR(30))
BEGIN	
	DECLARE  cun INT;
	SET cun = (SELECT COUNT(*) FROM  `user_login` a INNER JOIN `user_baseinfo` b ON a.`id` = b.`userId`
			WHERE (a.`username` = puser_name OR b.`phone`= puser_name) AND a.`password`=  p_userpwd);	
			
	 IF cun > 0  
	    THEN  
		SELECT a.*,b.* FROM  usertb a 
			INNER JOIN user_detailed b ON a.user_id=b.user_id 
		WHERE (a.`username` = puser_name OR b.`phone`= puser_name) AND a.`password`=  p_userpwd;
	    ELSEIF cun <= 0  
	    THEN  
		SELECT "404"  AS msg;-- 登陆失败，用户名或密码错误！	    
	    END IF;  			
    END */$$
DELIMITER ;

/* Procedure structure for procedure `pro_user_register` */

/*!50003 DROP PROCEDURE IF EXISTS  `pro_user_register` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `pro_user_register`(IN `install` VARCHAR(2),IN p_username VARCHAR(50),IN p_userpwd VARCHAR(30),
								in p_deviceId varchar(50),in p_patform varchar(30))
BEGIN
		DECLARE tuser_id VARCHAR(14);
		DECLARE tusername INT;
		SET tusername = (SELECT COUNT(user_name) FROM `user_login` WHERE user_name = p_username);		
		IF tusername > 0
		   THEN
		   SELECT "405" AS msg;
		ELSEIF tusername <=0
		THEN
			INSERT INTO usertb (`username`,`password`,`deviceId`,`patform`,`createtime`,`lastlogintime`) 
			VALUES(p_username,p_userpwd,p_deviceId,p_patform,NOW(),NOW());
			SET tuser_id = (SELECT user_id FROM `user_login` WHERE `username`=p_username AND `password`=p_userpwd);
			INSERT INTO `user_baseinfo` (`userId`) VALUES(tuser_id);
			SELECT a.*,b.* FROM  `user_login` a INNER JOIN `user_baseinfo` b ON a.user_id=b.user_id WHERE a.user_id=tuser_id;
		END IF;
    END */$$
DELIMITER ;

/* Procedure structure for procedure `pro_workexpericnce` */

/*!50003 DROP PROCEDURE IF EXISTS  `pro_workexpericnce` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `pro_workexpericnce`(IN `install` VARCHAR(2),IN p_userId INT,IN p_companyname VARCHAR(100),
							   IN p_industryclassification VARCHAR(128),IN p_jobtitle VARCHAR(100),IN p_worktimeStart VARCHAR(30),
							   IN p_worktimeEnd VARCHAR(30),IN p_expectedsalary VARCHAR(100),IN p_workdesc VARCHAR(500),In p_background varchar(20))
BEGIN
    IF `install` = 1
    THEN  
	SELECT * from `work_experience` where `userId` = p_userId;
	SELECT '201' AS msg;
    ELSEIF `install` = 2  
    THEN  
	INSERT INTO `work_experience` (`userId`,`companyname`,`industryclassification`,`jobtitle`,`worktimeStart`,`worktimeEnd`,`expectedsalary`,`workdesc`,`background`,`createtime`) 
	VALUES(p_userId,p_companyname,p_industryclassification,p_jobtitle,p_worktimeStart,p_worktimeEnd,p_expectedsalary,p_workdesc,p_background,NOW());
	SELECT '200' AS msg;
    ELSEIF `install` = 3
     THEN  
	update `work_experience` set `companyname` = p_companyname,`industryclassification` = p_industryclassification,
		`jobtitle` = p_jobtitle,`worktimeStart` = p_worktimeStart,`worktimeEnd` = p_worktimeEnd,
		`expectedsalary` = p_expectedsalary,`workdesc` = p_workdesc,`background`= p_background,updatime = now()
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
