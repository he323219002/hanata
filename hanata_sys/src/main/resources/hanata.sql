-- CREATE DATABASE `hanatablog` DEFAULT CHARACTER SET utf8mb4 COLLATE set utf8mb4_general_ci;;
--
-- USE `hanatablog`;
--
-- CREATE TABLE `user` (
-- 	`uid` varchar(32) PRIMARY KEY,
-- 	`createTime` datetime,
-- 	`updateTime` datetime,
-- 	`createUserId` varchar(32),
-- 	`updateUserId` varchar(32),
--
-- 	`username` varchar(32) DEFAULT '',
-- 	`password` varchar(256) DEFAULT '',
-- 	`nickname` varchar(32) DEFAULT '',
-- 	`avatar` varchar(512) DEFAULT '',
-- 	`phone` char(15) DEFAULT '',
-- 	`mail` varchar(64) DEFAULT '',
-- 	`gender` char(1) DEFAULT '',
-- 	`lastLoginDate` datetime DEFAULT NULL,
-- 	`role` char(1) DEFAULT '',
-- 	`isActive` char(1) DEFAULT '1'
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `article` (
	`uid` varchar(32) PRIMARY KEY,
	`createTime` datetime,
	`updateTime` datetime,
	`createUserId` varchar(32),
	`updateUserId` varchar(32),
	`deleted` char(1) DEFAULT '0',

	`title` varchar(32) DEFAULT '',
	`content` text,
	`categoryId` varchar(32) DEFAULT '',
	`categoryName` varchar(32) DEFAULT '',
	`like` integer DEFAULT 0,
	`commentCount` integer DEFAULT 0,
	`preId` varchar(32) DEFAULT '',
	`preName` varchar(32) DEFAULT '',
	`nextId` varchar(32) DEFAULT '',
	`nextName` varchar(32) DEFAULT '',
	`open` char(1) DEFAULT '1',
	`commentOpen` char(1) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;