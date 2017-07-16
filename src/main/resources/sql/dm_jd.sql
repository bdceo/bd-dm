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


-- 导出 dm_jd 的数据库结构
DROP DATABASE IF EXISTS `dm_jd`;
CREATE DATABASE IF NOT EXISTS `dm_jd` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `dm_jd`;

-- 导出  表 dm_jd.jd_product 结构
DROP TABLE IF EXISTS `jd_product`;
CREATE TABLE IF NOT EXISTS `jd_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pid` varchar(20) NOT NULL,
  `pname` varchar(200) NOT NULL,
  `price` decimal(10,2) NOT NULL DEFAULT '0.00',
  `ptype` tinyint(4) NOT NULL DEFAULT '1' COMMENT '商品类型： 1-京东自营，2-卖家店铺',
  `vid` varchar(20) DEFAULT NULL COMMENT '卖家编号',
  `rvecount` int(11) NOT NULL DEFAULT '0' COMMENT '商品评论总数',
  `pstat` tinyint(4) NOT NULL COMMENT '商品状态：1-有货，2-无货，3-下架',
  `cat1` varchar(50) DEFAULT NULL,
  `cat1Code` varchar(10) DEFAULT NULL,
  `cat2` varchar(50) DEFAULT NULL,
  `cat2Code` varchar(10) DEFAULT NULL,
  `cat3` varchar(50) DEFAULT NULL,
  `cat3Code` varchar(10) DEFAULT NULL,
  `purl` varchar(100) NOT NULL,
  `ctime` datetime NOT NULL COMMENT '商品首次入库时间',
  `utime` datetime NOT NULL COMMENT '商品更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `pid` (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='京东，商品基本信息表';

-- 数据导出被取消选择。
-- 导出  表 dm_jd.jd_queue 结构
DROP TABLE IF EXISTS `jd_queue`;
CREATE TABLE IF NOT EXISTS `jd_queue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `qtype` tinyint(4) DEFAULT NULL COMMENT '1-商品地址，2-评论地址',
  `url` varchar(100) DEFAULT NULL,
  `qstatus` tinyint(4) DEFAULT '0' COMMENT '0-未抓取，1-抓取成功，2-抓取失败，3-下架商品',
  `ctime` datetime NOT NULL COMMENT '初始时间',
  `ftime` datetime DEFAULT NULL COMMENT '抓取时间',
  `rtime` datetime DEFAULT NULL COMMENT '重新抓取时间',
  `ferr` varchar(500) DEFAULT NULL COMMENT '抓取错误描述',
  PRIMARY KEY (`id`),
  UNIQUE KEY `url` (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='京东，抓取队列';

-- 数据导出被取消选择。
-- 导出  表 dm_jd.jd_queue_cats 结构
DROP TABLE IF EXISTS `jd_queue_cats`;
CREATE TABLE IF NOT EXISTS `jd_queue_cats` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(100) NOT NULL COMMENT '分类地址页，带分页',
  `stat` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态：0-未抓取，1-抓取，2-抓取失败',
  `ctime` datetime NOT NULL COMMENT '分类地址首次入库时间',
  `ftime` datetime DEFAULT NULL COMMENT '抓取时间',
  `lftime` datetime DEFAULT NULL COMMENT '最后一次抓取时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `url` (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='京东，商品分类地址';

-- 数据导出被取消选择。
-- 导出  表 dm_jd.jd_reviews 结构
DROP TABLE IF EXISTS `jd_reviews`;
CREATE TABLE IF NOT EXISTS `jd_reviews` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `rid` bigint(20) NOT NULL COMMENT '评论ID',
  `pid` varchar(20) NOT NULL COMMENT '商品ID',
  `uid` varchar(40) NOT NULL COMMENT '用户ID',
  `rdetail` text NOT NULL COMMENT '心得,评论内容',
  `contime` datetime DEFAULT NULL COMMENT '评论时间',
  `buytime` datetime DEFAULT NULL COMMENT '购买时间',
  `ctime` datetime NOT NULL COMMENT '抓取时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `comid` (`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='京东，商品评论信息';

-- 数据导出被取消选择。
-- 导出  表 dm_jd.jd_user 结构
DROP TABLE IF EXISTS `jd_user`;
CREATE TABLE IF NOT EXISTS `jd_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` varchar(40) NOT NULL COMMENT '用户ID', 
  `nickname` varchar(50) NOT NULL COMMENT '用户昵称',
  `ip` varchar(20) DEFAULT NULL COMMENT '用户IP',
  `prvc` varchar(50) DEFAULT NULL COMMENT '用户所在省',
  `lvid` tinyint(4) DEFAULT NULL COMMENT '用户等级',
  `lvname` varchar(20) DEFAULT NULL COMMENT '用户等级描述',
  `rtime` datetime NOT NULL,
  `ctime` datetime NOT NULL COMMENT '用户首次入库时间',
  `utime` datetime NOT NULL COMMENT '用户信息更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='京东，用户基本信息';

-- 数据导出被取消选择。
-- 导出  表 dm_jd.jd_vender 结构
DROP TABLE IF EXISTS `jd_vender`;
CREATE TABLE IF NOT EXISTS `jd_vender` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `vid` varchar(50) NOT NULL COMMENT '卖家编号',
  `name` varchar(100) DEFAULT NULL COMMENT '卖家名称',
  `url` varchar(150) DEFAULT NULL COMMENT '店铺地址',
  `cmpy` varchar(100) DEFAULT NULL COMMENT '公司名称',
  `prvc` varchar(50) DEFAULT NULL COMMENT '省市地区',
  `city` varchar(30) DEFAULT NULL COMMENT '城市',
  `ctime` datetime NOT NULL COMMENT '首次收录卖家信息时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `vid` (`vid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='京东，卖家店铺信息';

-- 数据导出被取消选择。
-- 导出  过程 dm_jd.pc_jd_max 结构
DROP PROCEDURE IF EXISTS `pc_jd_max`;
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `pc_jd_max`()
BEGIN
select 'product',max(id) from jd_product
union
select 'review',max(id) from jd_reviews
union
select 'user',max(id) from jd_user;
END//
DELIMITER ;

-- 导出  过程 dm_jd.pc_jd_rst 结构
DROP PROCEDURE IF EXISTS `pc_jd_rst`;
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `pc_jd_rst`(IN `bd_ufc` INT)
    COMMENT '重启恢复未抓取评论的商品'
BEGIN

-- 已抓取
declare bd_fc int default 0;
-- 准备删除
declare bd_del int default 0;

select count(*) into bd_fc from jd_queue where qstatus>0;
set @bd_del=bd_fc-bd_ufc;
-- select bd_fc,@bd_del;

-- 准备删除
prepare bd_st1 from 
	'delete from jd_queue where id in(
		select id from (
			select id from jd_queue where qstatus>0 order by ftime limit ?) t )';
-- 执行删除
execute bd_st1 using @bd_del;
commit;
-- 删除sql
deallocate prepare bd_st1;

-- 重置状态，重启后抓取
update jd_queue set qstatus=0 where qstatus>0;

END//
DELIMITER ;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
