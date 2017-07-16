-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        5.5.33 - MySQL Community Server (GPL)
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  9.3.0.5059
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 dm_douban 的数据库结构
CREATE DATABASE IF NOT EXISTS `dm_douban` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `dm_douban`;

-- 导出  表 dm_douban.dou_book 结构
CREATE TABLE IF NOT EXISTS `dou_book` (
  `id` bigint(20) NOT NULL,
  `bookIsbn` varchar(50) NOT NULL COMMENT 'isbn',
  `bookName` varchar(100) NOT NULL COMMENT '书名',
  `authorInfo` text NOT NULL COMMENT '作者',
  `bookAuthor` varchar(100) NOT NULL COMMENT '作者简介',
  `bookCatalog` text NOT NULL COMMENT '目录',
  `bookInfo` text NOT NULL COMMENT '内容简介',
  `bookNameEn` varchar(100) NOT NULL COMMENT '原作名',
  `bookPic` varchar(100) NOT NULL COMMENT '封面',
  `bookRank` varchar(50) NOT NULL COMMENT '评分',
  `bookSerial` varchar(50) NOT NULL COMMENT '丛书/系列',
  `bookTag` varchar(200) NOT NULL COMMENT '标签',
  `bookTranslator` varchar(100) NOT NULL COMMENT '翻译',
  `douUrl` varchar(100) NOT NULL COMMENT '地址',
  `packed` varchar(50) NOT NULL COMMENT '装订',
  `pages` int(11) NOT NULL COMMENT '页数',
  `price` varchar(50) NOT NULL COMMENT '定价',
  `pubYear` varchar(50) NOT NULL COMMENT '出版年',
  `publisher` varchar(50) NOT NULL COMMENT '出版社',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_ISBN` (`bookIsbn`),
  KEY `KEY_ISBN` (`bookIsbn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍详情';

-- 数据导出被取消选择。
-- 导出  表 dm_douban.dou_book_tag 结构
CREATE TABLE IF NOT EXISTS `dou_book_tag` (
  `id` bigint(20) NOT NULL,
  `tagName` varchar(50) NOT NULL,
  `tagUrl` varchar(200) NOT NULL,
  `tagBooks` int(11) NOT NULL,
  `fetchStat` tinyint(4) NOT NULL,
  `fetchPage` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `tagName` (`tagName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书标签';

-- 数据导出被取消选择。
-- 导出  表 dm_douban.dou_buylink 结构
CREATE TABLE IF NOT EXISTS `dou_buylink` (
  `id` bigint(20) NOT NULL,
  `bookIsbn` varchar(50) NOT NULL COMMENT '书籍编号',
  `bookStore` varchar(10) NOT NULL COMMENT '商家',
  `savePrice` varchar(50) NOT NULL COMMENT '原价',
  `storePrice` varchar(50) NOT NULL COMMENT '售价',
  `storeUrl` varchar(500) NOT NULL COMMENT '购买链接',
  PRIMARY KEY (`id`),
  KEY `bookIsbn` (`bookIsbn`),
  KEY `storeUrl` (`storeUrl`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='导购链接';

-- 数据导出被取消选择。
-- 导出  表 dm_douban.dou_fetch_queue 结构
CREATE TABLE IF NOT EXISTS `dou_fetch_queue` (
  `id` bigint(20) NOT NULL,
  `bookName` varchar(100) NOT NULL,
  `bookIsbn` varchar(50) DEFAULT '',
  `fetchUrl` varchar(100) NOT NULL COMMENT '抓取地址',
  `fetchFlag` tinyint(4) NOT NULL COMMENT '11-书的总评；12-书的单评；21-书的导购；31-推荐的书',
  `fetchStat` tinyint(4) NOT NULL COMMENT '1-未抓取；2-抓取完毕；3-抓取失败',
  `fetchDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_FETCHURL` (`fetchUrl`),
  KEY `bookIsbn` (`bookIsbn`),
  KEY `fetchUrl` (`fetchUrl`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抓取队列';

-- 数据导出被取消选择。
-- 导出  表 dm_douban.dou_fetch_tag 结构
CREATE TABLE IF NOT EXISTS `dou_fetch_tag` (
  `id` bigint(20) NOT NULL,
  `tagName` varchar(50) NOT NULL,
  `tagUrl` varchar(200) NOT NULL,
  `tagBooks` int(11) NOT NULL,
  `fetchStat` tinyint(4) NOT NULL COMMENT '1-未抓取；2-抓取中；3-抓取完',
  `fetchPage` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='抓取标签';

-- 数据导出被取消选择。
-- 导出  表 dm_douban.dou_review 结构
CREATE TABLE IF NOT EXISTS `dou_review` (
  `id` bigint(20) NOT NULL,
  `userCode` varchar(50) NOT NULL COMMENT '用户',
  `bookIsbn` varchar(50) NOT NULL COMMENT '书',
  `rewTittle` varchar(200) NOT NULL COMMENT '标题',
  `rewInfo` text NOT NULL COMMENT '内容',
  `rewDate` datetime NOT NULL COMMENT '评论时间',
  PRIMARY KEY (`id`),
  KEY `userCode` (`userCode`),
  KEY `bookIsbn` (`bookIsbn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论信息';

-- 数据导出被取消选择。
-- 导出  表 dm_douban.dou_user 结构
CREATE TABLE IF NOT EXISTS `dou_user` (
  `id` bigint(20) NOT NULL,
  `addDate` datetime NOT NULL,
  `douHome` varchar(200) NOT NULL,
  `userStat` tinyint(4) NOT NULL COMMENT '1-ok；2-error',
  `userCode` varchar(50) NOT NULL,
  `userName` varchar(100) NOT NULL,
  `userPwd` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='豆瓣用户信息';

-- 数据导出被取消选择。
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
