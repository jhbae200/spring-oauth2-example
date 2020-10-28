DROP TABLE IF EXISTS `OAuthClient`;
CREATE TABLE `OAuthClient` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `clientId` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `clientSecret` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
  `serviceName` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `innerService` tinyint(4) NOT NULL DEFAULT '0',
  `createdDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deletedDate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`seq`),
  UNIQUE KEY `client_id_UNIQUE` (`clientId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `OAuthClientHttp`;
CREATE TABLE IF NOT EXISTS `OAuthClientHttp` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `clientSeq` int(11) NOT NULL,
  `url` varchar(300) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` int(11) NOT NULL COMMENT '1: 탈퇴, 2: 닉네임 변경',
  `createdDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `OAuthClientUri`;
CREATE TABLE IF NOT EXISTS `OAuthClientUri` (
  `seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `clientSeq` int(11) NOT NULL,
  `redirectUri` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `OAuthClientUser`;
CREATE TABLE IF NOT EXISTS `OAuthClientUser` (
  `seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `userSeq` bigint(20) NOT NULL,
  `clientUserId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `clientSeq` int(11) NOT NULL,
  `createdDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`seq`),
  KEY `clientUserId_index` (`clientUserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `OAuthClientUserLog`;
CREATE TABLE IF NOT EXISTS `OAuthClientUserLog` (
  `seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `userSeq` bigint(20) NOT NULL,
  `clientUserId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `clientSeq` int(11) NOT NULL,
  `os` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0 : default 1 : android 2 : ios',
  `type` int(11) NOT NULL COMMENT '0: 가입 1: 탈퇴, 2: 닉네임 변경',
  `data` varchar(400) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createdDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `taskDate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`seq`),
  KEY `ix_userSeq` (`userSeq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `OAuthJwtRsaKey`;
CREATE TABLE IF NOT EXISTS `OAuthJwtRsaKey` (
  `seq` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `privateKey` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `publicKey` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `createdDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`seq`),
  UNIQUE KEY `JwtRsaKey_name_uindex` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='account jwt rsa key';

DROP TABLE IF EXISTS `OAuthRefreshToken`;
CREATE TABLE IF NOT EXISTS `OAuthRefreshToken` (
  `seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `jti` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `userSeq` bigint(20) NOT NULL,
  `userId` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `clientSeq` int(11) NOT NULL,
  `createdDate` timestamp NULL DEFAULT NULL,
  `expireDate` timestamp NULL DEFAULT NULL,
  `accessDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `scope` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nonce` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `type` int(11) NOT NULL,
  PRIMARY KEY (`seq`),
  UNIQUE KEY `jti_UNIQUE` (`jti`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `User`;
CREATE TABLE IF NOT EXISTS `User` (
  `seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(30) NOT NULL,
  `createdDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deletedDate` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `UserIds`;
CREATE TABLE IF NOT EXISTS `UserIds` (
  `seq` bigint(20) NOT NULL AUTO_INCREMENT,
  `userSeq` bigint(20) NOT NULL,
  `id` varchar(300) NOT NULL,
  `password` varchar(300) NOT NULL,
  `type` int(11) NOT NULL COMMENT '1: 기본회원 2 : 구글 소셜 회원 3 : 페이스북 소셜 회원 4: 카카오 소셜 회원',
  `createdDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`seq`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
