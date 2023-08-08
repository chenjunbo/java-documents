/*
SQLyog Job Agent Version 8.2 Copyright(c) Webyog Softworks Pvt. Ltd. All Rights Reserved.


MySQL - 5.5.27 : Database - db_shopping
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`db_shopping` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `db_shopping`;

/*Table structure for table `tb_address` */

DROP TABLE IF EXISTS `tb_address`;

CREATE TABLE `tb_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `detail` text,
  `name` varchar(20) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `uid` int(11) NOT NULL,
  `level` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_address_id` (`uid`),
  CONSTRAINT `fk_address_id` FOREIGN KEY (`uid`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `tb_address` */

insert  into `tb_address` values (2,'北京市:北京市市辖区:东城区:宝盛北里西区天丰利商城四层 千锋教育','李爽','1567890890822',4,1),(3,'北京市海淀区3','李爽1','136110803022',6,0),(4,'22223','张','112',6,0),(6,'辽宁省朝阳市凌源','张','15678908908',4,0);

/*Table structure for table `tb_cart` */

DROP TABLE IF EXISTS `tb_cart`;

CREATE TABLE `tb_cart` (
  `id` int(11) NOT NULL,
  `pid` int(11) NOT NULL DEFAULT '0',
  `Num` int(11) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`,`pid`),
  KEY `fk_cart_pid` (`pid`),
  CONSTRAINT `fk_cart_id` FOREIGN KEY (`id`) REFERENCES `tb_user` (`id`),
  CONSTRAINT `fk_cart_pid` FOREIGN KEY (`pid`) REFERENCES `tb_goods` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_cart` */

/*Table structure for table `tb_goods` */

DROP TABLE IF EXISTS `tb_goods`;

CREATE TABLE `tb_goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `pubdate` date DEFAULT NULL,
  `picture` varchar(255) NOT NULL,
  `price` int(11) NOT NULL,
  `star` tinyint(4) NOT NULL DEFAULT '0',
  `intro` text,
  `typeid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_typeid` (`typeid`),
  CONSTRAINT `fk_typeid` FOREIGN KEY (`typeid`) REFERENCES `tb_goods_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Data for the table `tb_goods` */

insert  into `tb_goods` values (1,'java模式','2017-09-12','8\\7\\8bba858c-79da-4cdc-a974-eb4526d7408b_696673-1_w.jpg',1,5,'第一',1),(2,'java编程思想','2017-09-14','8\\7\\8bba858c-79da-4cdc-a974-eb4526d7408b_696673-1_w.jpg',23,5,'第二本',1),(3,'html5','2017-09-07','8\\7\\8b607da8e9-8421-47c2-bc61-424c22c50697_696673-1_w.jpg',78,4,'第一本html\r\n实例二\r\n\r\n     这次要介绍的就没那么简单了，这个悬停弹出框的效果比第一个实例要复杂很多。弹出框弹出的效果是一样的，不一样的是弹出框的内容，这次的弹出框中不仅要有提示，还要有相应的信息，链接等。小编所做的效果是在弹出框中添加了图片，然后有相应的链接，在点击之后可以跳转到不同的页面。',1),(4,'安卓','2017-09-02','b\\8\\46e944eb-3589-4dc3-aafa-24227ff0323c_',89,3,'安卓第一本书是从这里开始的',1),(5,'小米6','2018-01-24','d\\a\\3b673ed6-9dd9-4f34-8920-c4da39af94fd_liebiao_xiaomi6.jpg',2499,3,'变焦双摄，4 轴防抖 / 骁龙835 旗舰处理器，6GB 大内存，最大可选128GB 闪存 / 5.15\" 护眼屏 / 四曲面玻璃/陶瓷机身',2),(6,'小米7mini','2018-01-10','8\\5\\86c72bfa-67c2-4ab1-97ec-a82d6b2168d0_liebiao_xiaomi6.jpg',1,2,'',2),(7,'admin','2018-01-20','7\\1\\daf26636-d382-402c-9ac0-5e4f39aa5299_liebiao_xiaomi6.jpg',11,4,'',5),(8,'小米MIX2','2018-01-24','f\\0\\c5da94db-0d2e-42df-a4ab-2610e9b02df0_liebiao_xiaomimix.jpg',1588,3,'5.99\" 超大屏幕\r\n机身却比 5.5\" 传统手机还小\r\n\r\n5.99\"，你很难找到比它屏幕大，但是尺寸却比它小的手机。\r\n这源于不断进化的全面屏科技，带来更窄的底边、更小的相机、隐藏的距离传感器……\r\n最终将 5.99\" 大屏装进了比 5.5\" 传统手机还小的机身内，握感绝佳。',2);

/*Table structure for table `tb_goods_type` */

DROP TABLE IF EXISTS `tb_goods_type`;

CREATE TABLE `tb_goods_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `level` int(11) DEFAULT NULL,
  `Parent` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

/*Data for the table `tb_goods_type` */

insert  into `tb_goods_type` values (1,'计算机',1,0),(2,'小米手机',1,0),(3,'笔记本',1,0),(4,'电视盒子',1,0),(5,'智能家电',1,0),(6,'其他',2,1),(7,'平衡车',0,6),(8,'辅导室',3,6),(9,'辅导室222',4,8);

/*Table structure for table `tb_order` */

DROP TABLE IF EXISTS `tb_order`;

CREATE TABLE `tb_order` (
  `id` varchar(100) NOT NULL,
  `uid` int(11) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `aid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_uid` (`uid`),
  KEY `fk_order_aid` (`aid`),
  CONSTRAINT `fk_order_aid` FOREIGN KEY (`aid`) REFERENCES `tb_address` (`id`),
  CONSTRAINT `fk_order_uid` FOREIGN KEY (`uid`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_order` */

/*Table structure for table `tb_orderdetail` */

DROP TABLE IF EXISTS `tb_orderdetail`;

CREATE TABLE `tb_orderdetail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Oid` varchar(100) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `Money` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_pid` (`pid`),
  KEY `fk_order_id` (`Oid`),
  CONSTRAINT `fk_order_id` FOREIGN KEY (`Oid`) REFERENCES `tb_order` (`id`),
  CONSTRAINT `fk_order_pid` FOREIGN KEY (`pid`) REFERENCES `tb_goods` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_orderdetail` */

/*Table structure for table `tb_user` */

DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(32) NOT NULL,
  `email` varchar(50) NOT NULL,
  `gender` varchar(50) NOT NULL,
  `flag` int(11) DEFAULT NULL,
  `role` int(11) DEFAULT NULL,
  `code` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `tb_user` */

insert  into `tb_user` values (3,'admin','e10adc3949ba59abbe56e057f20f883e','2407359193@qq.com','女',1,0,'20170912202221249354'),(4,'lee','e10adc3949ba59abbe56e057f20f883e','2407359193@qq.com','男',1,1,'2017091221312740330d'),(5,'andy','81dc9bdb52d04dc20036dbd8313ed055','2407359193@qq.com','女',2,1,'201709122331221743af'),(6,'amin','e10adc3949ba59abbe56e057f20f883e','2407359193@qq.com','女',1,1,'2018012218370268135d');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
