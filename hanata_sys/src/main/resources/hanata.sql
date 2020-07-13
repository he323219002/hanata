CREATE DATABASE `hanatablog` DEFAULT CHARACTER SET utf8mb4 COLLATE set utf8mb4_general_ci;;

USE `hanatablog`;

CREATE TABLE `user` (
	`uid` varchar(32) PRIMARY KEY,
	`createTime` datetime,
	`updateTime` datetime,
	`createUserId` varchar(32),
	`updateUserId` varchar(32),
	`deleted` char(1) default '0',

	`username` varchar(32) DEFAULT '',
	`password` varchar(64) DEFAULT '',
	`nickname` varchar(32) DEFAULT '',
	`avatar` varchar(512) DEFAULT '',
	`phone` char(15) DEFAULT '',
	`mail` varchar(64) DEFAULT '',
	`gender` char(1) DEFAULT '',
	`lastLoginDate` datetime DEFAULT NULL,
	`role` char(1) DEFAULT '1',
	`isActive` char(1) DEFAULT '1',
	UNIQUE KEY unique_username (username),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;



CREATE TABLE `article` (
	`uid` varchar(32) PRIMARY KEY,
	`createTime` datetime,
	`updateTime` datetime,
	`createUserId` varchar(32),
	`updateUserId` varchar(32),
	`deleted` char(1) DEFAULT '0',

	`title` varchar(32) DEFAULT '',
	`content` text,
	-- 0草稿 1发布
	`state` char(1) DEFAULT '1', 
	`categoryId` varchar(32) DEFAULT '',
	`categoryName` varchar(32) DEFAULT '',
	`likeCount` integer DEFAULT 0,
	`commentCount` integer DEFAULT 0,
	`preId` varchar(32) DEFAULT '',
	`preName` varchar(32) DEFAULT '',
	`nextId` varchar(32) DEFAULT '',
	`nextName` varchar(32) DEFAULT '',
	`open` char(1) DEFAULT '1',
	`commentOpen` char(1) DEFAULT '1',
	`viewCount` integer DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `category`(
	`uid` varchar(32) PRIMARY KEY,
	`createTime` datetime,
	`updateTime` datetime,
	`createUserId` varchar(32),
	`updateUserId` varchar(32),
	`deleted` char(1) DEFAULT '0',

	`name` varchar(32) DEFAULT '',
	`count` integer DEFAULT 0
)


CREATE TABLE `article_category` (
	`uid` varchar(32) PRIMARY KEY,
	`createTime` datetime,
	`updateTime` datetime,
	`createUserId` varchar(32),
	`updateUserId` varchar(32),
	`deleted` char(1) DEFAULT '0',

	`name` varchar(32) DEFAULT '',
	`count` integer DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;


CREATE TABLE `label` (
	`uid` varchar(32) PRIMARY KEY,
	`createTime` datetime,
	`updateTime` datetime,
	`createUserId` varchar(32),
	`updateUserId` varchar(32),
	`deleted` char(1) DEFAULT '0',

	`name` varchar(32) DEFAULT '',
	`count` integer DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;

CREATE TABLE `article_label` (
	`uid` varchar(32) PRIMARY KEY,
	`createTime` datetime,
	`updateTime` datetime,
	`createUserId` varchar(32),
	`updateUserId` varchar(32),
	`deleted` char(1) DEFAULT '0',

	`labelId` varchar(32) DEFAULT '',
	`labelName` varchar(32) DEFAULT '',
	`title` varchar(32) DEFAULT '',
	`articleId` varchar(32) DEFAULT '',
	index (articleId),
	index (labelId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;


CREATE TABLE `favour` (
	`uid` varchar(32) PRIMARY KEY,
	`createTime` datetime,
	`updateTime` datetime,
	`createUserId` varchar(32),
	`updateUserId` varchar(32),
	`deleted` char(1) DEFAULT '0',

	`userName` varchar(32),
	`userAvatar` varchar(512),
	`articleId` varchar(32),
	`title` varchar(32),
	index (articleId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;


CREATE TABLE `comment` (
	`uid` varchar(32) PRIMARY KEY,
	`createTime` datetime,
	`updateTime` datetime,
	`createUserId` varchar(32),
	`updateUserId` varchar(32),
	`deleted` char(1) DEFAULT '0',

	`articleId` varchar(32),
	`title` varchar(128),
	`content` varchar(512),
	`fartherId` varchar(32) DEFAULT '',
	`fartherContent` varchar(256) DEFAULT '',
	`userName` varchar(32) DEFAULT '',
	`targetId` varchar(32),
	`target` varchar(1),
	`appending` varchar (32),
	`userAvatar` varchar (512),
	`receiverId` varchar (32),
	index (articleId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;



CREATE TABLE `message` (
	`uid` varchar(32) PRIMARY KEY,
	`createTime` datetime,
	`updateTime` datetime,
	`createUserId` varchar(32),
	`updateUserId` varchar(32),
	`deleted` char(1) DEFAULT '0',

	`title` varchar(32),
	`content` varchar(256),
	`isRead` varchar(1) DEFAULT '0',
	`userId` varchar(32),
	`nickname` varchar(32),
	`avatar` varchar(512),
	`username` varchar (32),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;



CREATE TABLE `tag` (
	`uid` varchar(32) PRIMARY KEY,
	`createTime` datetime,
	`updateTime` datetime,
	`createUserId` varchar(32),
	`updateUserId` varchar(32),
	`deleted` char(1) DEFAULT '0',

	`name` varchar(32) DEFAULT '',
	`count` integer DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;



CREATE TABLE `dairy` (
	`uid` varchar(32) PRIMARY KEY,
	`createTime` datetime,
	`updateTime` datetime,
	`createUserId` varchar(32),
	`updateUserId` varchar(32),
	`deleted` char(1) DEFAULT '0',

	`title` varchar(32) DEFAULT '',
	`content` text,
	`tagId` varchar(32) DEFAULT '',
	`state` char(1) DEFAULT '', 

	`tagName` varchar(32) DEFAULT '',
	`likeCount` integer DEFAULT 0,
	`commentCount` integer DEFAULT 0,
	`preId` varchar(32) DEFAULT '',
	`preName` varchar(32) DEFAULT '',
	`nextId` varchar(32) DEFAULT '',
	`nextName` varchar(32) DEFAULT '',
	`open` char(1) DEFAULT '1',
	`commentOpen` char(1) DEFAULT '1',
	`coverPic` varchar(512) DEFAULT '',
	`picId` varchar(32) DEFAULT '',
	`viewCount` integer DEFAULT 0,
	index (tagId)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;



CREATE TABLE `file` (
	`uid` varchar(32) PRIMARY KEY,
	`createTime` datetime,
	`updateTime` datetime,
	`createUserId` varchar(32),
	`updateUserId` varchar(32),
	`deleted` char(1) DEFAULT '0',

	`filePath` varchar(512),
	`fileName` varchar(64) DEFAULT '',
	`contentType` varchar(32) DEFAULT ''

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;


CREATE TABLE `updateRecord` (
	`uid` varchar(32) PRIMARY KEY,
	`createTime` datetime,
	`updateTime` datetime,
	`createUserId` varchar(32),
	`updateUserId` varchar(32),
	`deleted` char(1) DEFAULT '0',

	`version` varchar(24),
	`content` varchar(512) DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_general_ci;




