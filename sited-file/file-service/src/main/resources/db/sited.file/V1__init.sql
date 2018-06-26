DROP TABLE IF EXISTS `file_directory`;
CREATE TABLE `file_directory` (
  `id` varchar(36) NOT NULL,
  `created_by` varchar(64) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `group_id` varchar(36) DEFAULT NULL,
  `group_roles` varchar(512) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `others_roles` varchar(512) DEFAULT NULL,
  `owner_id` varchar(36) DEFAULT NULL,
  `owner_roles` varchar(512) DEFAULT NULL,
  `parent_id` varchar(36) DEFAULT NULL,
  `parent_ids` varchar(512) DEFAULT NULL,
  `path` varchar(128) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `updated_by` varchar(64) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `file_file`;
CREATE TABLE `file_file` (
  `id` varchar(36) NOT NULL,
  `created_by` varchar(64) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `directory_id` varchar(36) DEFAULT NULL,
  `extension` varchar(16) DEFAULT NULL,
  `file_name` varchar(64) DEFAULT NULL,
  `length` bigint(20) DEFAULT NULL,
  `path` varchar(128) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `tags` varchar(512) DEFAULT NULL,
  `title` varchar(256) DEFAULT NULL,
  `updated_by` varchar(66) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);
