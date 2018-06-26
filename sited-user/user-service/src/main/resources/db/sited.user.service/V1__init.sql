DROP TABLE IF EXISTS `user_auto_login_token`;
CREATE TABLE `user_auto_login_token` (
  `id` varchar(36) NOT NULL,
  `created_by` varchar(64) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `expired_time` datetime DEFAULT NULL,
  `token` varchar(64) DEFAULT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `user_confirm`;
CREATE TABLE `user_confirm` (
  `id` varchar(36) NOT NULL,
  `confirm_code` varchar(36) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `expire_time` datetime DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `id` varchar(36) NOT NULL,
  `created_by` varchar(64) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `roles` varchar(512) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `updated_by` varchar(64) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `user_oauth_user`;
CREATE TABLE `user_oauth_user` (
  `id` varchar(36) NOT NULL,
  `created_by` varchar(64) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `phone` varchar(16) DEFAULT NULL,
  `provider` int(11) DEFAULT NULL,
  `updated_by` varchar(64) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  `username` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `user_reset_password_token`;
CREATE TABLE `user_reset_password_token` (
  `id` varchar(36) NOT NULL,
  `created_by` varchar(64) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `removed` bit(1) DEFAULT NULL,
  `token` varchar(64) DEFAULT NULL,
  `updated_by` varchar(64) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `user_user`;
CREATE TABLE `user_user` (
  `id` varchar(36) NOT NULL,
  `campaign` varchar(32) DEFAULT NULL,
  `channel` varchar(32) DEFAULT NULL,
  `city` varchar(64) DEFAULT NULL,
  `country` varchar(16) DEFAULT NULL,
  `created_by` varchar(64) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `fields` varchar(2048) DEFAULT NULL,
  `gender` int(11) DEFAULT NULL,
  `hashed_password` varchar(32) DEFAULT NULL,
  `image_url` varchar(512) DEFAULT NULL,
  `iteration` int(11) DEFAULT NULL,
  `language` varchar(16) DEFAULT NULL,
  `nickname` varchar(64) DEFAULT NULL,
  `phone` varchar(16) DEFAULT NULL,
  `salt` varchar(64) DEFAULT NULL,
  `state` varchar(64) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `tags` varchar(512) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `updated_by` varchar(64) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `user_group_ids` varchar(512) DEFAULT NULL,
  `username` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`)
);