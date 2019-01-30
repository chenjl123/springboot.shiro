/*
SQLyog Enterprise - MySQL GUI v7.14 
MySQL - 5.5.20 : Database - auth_shiro
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`auth_shiro` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `auth_shiro`;

/*Table structure for table `permission` */

DROP TABLE IF EXISTS `permission`;

CREATE TABLE `permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL COMMENT '菜单名称',
  `pid` int(11) DEFAULT NULL COMMENT '父菜单id',
  `zindex` int(2) DEFAULT NULL COMMENT '菜单排序',
  `istype` int(1) DEFAULT NULL COMMENT '权限分类（0 菜单；1 功能）',
  `descpt` varchar(50) DEFAULT NULL COMMENT '描述',
  `code` varchar(20) DEFAULT NULL COMMENT '菜单编号',
  `icon` varchar(30) DEFAULT NULL COMMENT '菜单图标名称',
  `page` varchar(50) DEFAULT NULL COMMENT '菜单url',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Data for the table `permission` */

insert  into `permission`(`id`,`name`,`pid`,`zindex`,`istype`,`descpt`,`code`,`icon`,`page`,`create_time`) values (1,'系统管理',0,100,0,'系统管理','system','','/','2017-12-20 16:22:43'),(2,'用户管理',1,1100,0,'用户管理','usermanage','','/user/userList','2017-12-20 16:27:03'),(3,'角色管理',1,1200,0,'角色管理','rolemanage','','/auth/roleList','2017-12-20 16:27:03'),(4,'权限管理',1,1300,0,'权限管理','permmanage',NULL,'/auth/permList','2017-12-30 19:17:32'),(5,'商品管理',0,300,0,'商品管理','shops',NULL,'/','2017-12-30 19:17:50'),(6,'供应商管理',0,200,0,'供应商管理','channel',NULL,'/','2018-01-01 11:07:17'),(8,'订单管理',0,400,0,'订单管理','orders',NULL,'/','2018-01-09 09:26:53'),(10,'供应商信息列表',6,2200,0,'供应商信息列表','channelPage',NULL,'/','2018-01-09 19:07:05'),(11,'供应商会员列表',6,2300,0,'供应商会员列表','channelUsers',NULL,'/channel/channelUserListPage','2018-01-09 19:07:52'),(13,'商品列表',5,3100,0,'商品列表','shopPage',NULL,'/shop/shopPage','2018-01-09 19:33:53'),(14,'商品订单列表',8,4100,0,'商品订单列表','orderPage',NULL,'/order/orderPage','2018-01-09 19:34:33');

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(30) DEFAULT NULL COMMENT '角色名称',
  `descpt` varchar(50) DEFAULT NULL COMMENT '角色描述',
  `code` varchar(20) DEFAULT NULL COMMENT '角色编号',
  `create_time` datetime DEFAULT NULL COMMENT '添加数据时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `role` */

insert  into `role`(`id`,`role_name`,`descpt`,`code`,`create_time`) values (1,'超级管理','超级管理员','superman','2018-01-09 19:28:53'),(2,'高级管理员','高级管理员12','highmanage','2018-01-17 13:53:23'),(3,'经理','经理','bdmanage','2018-01-18 13:41:47'),(4,'质检员','质检员','checkmanage','2018-01-18 14:03:00'),(5,'客维员','客维员','guestmanage','2018-01-18 14:06:48');

/*Table structure for table `role_permission` */

DROP TABLE IF EXISTS `role_permission`;

CREATE TABLE `role_permission` (
  `permit_id` int(5) NOT NULL AUTO_INCREMENT,
  `role_id` int(5) NOT NULL,
  PRIMARY KEY (`permit_id`,`role_id`),
  KEY `perimitid` (`permit_id`) USING BTREE,
  KEY `roleid` (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

/*Data for the table `role_permission` */

insert  into `role_permission`(`permit_id`,`role_id`) values (1,1),(1,2),(1,4),(1,5),(2,1),(2,2),(3,1),(3,2),(3,4),(3,5),(4,1),(5,1),(5,2),(5,3),(6,1),(6,2),(6,3),(7,1),(8,1),(8,2),(8,3),(10,1),(10,2),(10,3),(11,1),(11,2),(11,3),(12,1),(12,2),(12,3),(13,1),(13,2),(13,3),(14,1),(14,2),(14,3);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `mobile` varchar(15) DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  `is_del` char(1) DEFAULT '0' COMMENT '是否删除（0：正常；1：已删）',
  `user_code` varchar(50) DEFAULT NULL,
  `is_lock` char(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `name` (`user_name`) USING BTREE,
  KEY `id` (`id`) USING BTREE,
  KEY `mobile` (`mobile`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='用户表';

/*Data for the table `user` */

insert  into `user`(`id`,`user_name`,`mobile`,`email`,`password`,`create_time`,`is_del`,`user_code`,`is_lock`) values (1,'wyait','12316596566','aaa','c4ca4238a0b923820dcc509a6f75849b','2017-12-29 17:27:23','0','admin','0'),(3,'wy1','11155556667','a11','c33367701511b4f6020ec61ded352059','2018-01-01 15:17:19','0','cjl','0'),(5,'wy23','11155552233','a','c33367701511b4f6020ec61ded352059','2018-01-02 13:41:29','0','cjl','0'),(6,'wyyyy','12356456542','afdfd123','c33367701511b4f6020ec61ded352059','2018-01-02 13:44:04','0','cjl','0'),(7,'wwwww','11155623232','123456','c33367701511b4f6020ec61ded352059','2018-01-02 13:44:23','1','cjl','0'),(8,'manage','12345678911','359818226@.com','e10adc3949ba59abbe56e057f20f883e','2018-01-04 16:51:21','0','cjl','0'),(10,'b','12345678977','a','c33367701511b4f6020ec61ded352059','2018-01-09 10:30:56','0','cjl','0'),(11,'e','12345678911','e','c33367701511b4f6020ec61ded352059','2018-01-09 10:31:08','0','cjl','0'),(12,'ee','12345678919','a','c33367701511b4f6020ec61ded352059','2018-01-09 10:31:33','0','cjl','0'),(13,'456','12345678888','e','c33367701511b4f6020ec61ded352059','2018-01-09 10:31:46','0','cjl','0'),(14,'89','12345612222','a','c33367701511b4f6020ec61ded352059','2018-01-09 10:31:58','0','cjl','0'),(15,'aa','12345678915','ee1','c33367701511b4f6020ec61ded352059','2018-01-09 10:32:12','0','cjl','0'),(16,'tty','12345678521','aa','c33367701511b4f6020ec61ded352059','2018-01-09 13:32:17','0','cjl','0'),(17,'oo','12345666666','qq','c33367701511b4f6020ec61ded352059','2018-01-09 13:51:01','0','cjl','0'),(18,'iik','12345678920','aaaa120','c33367701511b4f6020ec61ded352059','2018-01-09 16:31:03','0','cjl','0'),(19,'123456','12321727724','24319@qq.com','c33367701511b4f6020ec61ded352059','2018-01-17 09:24:27','0','cjl','0'),(20,NULL,'111111222','','c33367701511b4f6020ec61ded352059','2018-01-17 13:54:08','0','cjl','0'),(21,'aaaacc2','10123235656','','c33367701511b4f6020ec61ded352059','2018-04-22 21:14:48','1','cjl','0'),(22,'11232323232','23233223322','','c33367701511b4f6020ec61ded352059','2018-04-26 13:30:44','1','cjl','0'),(23,'bbb1','10222224564','','c33367701511b4f6020ec61ded352059','2018-04-26 14:36:30','1','cjl','0'),(24,'eee','12536369898','','c33367701511b4f6020ec61ded352059','2018-04-26 18:37:34','1','cjl','0'),(25,'fast','12312312312','','c33367701511b4f6020ec61ded352059','2018-04-28 09:37:32','1','cjl','0'),(26,'xxx','12923235959','','c33367701511b4f6020ec61ded352059','2018-05-02 16:55:35','1','cjl','0'),(27,'ppp12','12826265353','','c33367701511b4f6020ec61ded352059','2018-05-02 16:56:41','1','cjl','0'),(28,NULL,'15965215454','','e10adc3949ba59abbe56e057f20f883e','2019-01-17 15:00:50','1','test1','0'),(29,NULL,'11111111','','e10adc3949ba59abbe56e057f20f883e','2019-01-17 15:37:06','0','tes11','0'),(30,NULL,'111111111','','e10adc3949ba59abbe56e057f20f883e','2019-01-17 15:37:59','0','testas','0'),(31,NULL,'333','','e10adc3949ba59abbe56e057f20f883e','2019-01-17 15:39:58','0','abcd','0'),(32,NULL,'21212','','e10adc3949ba59abbe56e057f20f883e','2019-01-17 15:42:42','0','dede','0'),(33,NULL,'1545451451','','c4ca4238a0b923820dcc509a6f75849b','2019-01-18 14:22:47','0','chenjl','0');

/*Table structure for table `user_role` */

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(5) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `userid` (`user_id`) USING BTREE,
  KEY `roleid` (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_role` */

insert  into `user_role`(`user_id`,`role_id`) values (1,1),(1,3),(3,5),(12,5),(19,3),(20,2),(21,4),(22,5),(23,3),(24,5),(25,2),(26,5),(27,5),(32,4),(32,5),(33,4);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
