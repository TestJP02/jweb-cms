DROP TABLE IF EXISTS `email_template`;
CREATE TABLE `email_template` (
  `id` varchar(36) NOT NULL,
  `content` text,
  `created_by` varchar(64) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `subject` varchar(512) DEFAULT NULL,
  `updated_by` varchar(64) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `email_tracking`;
CREATE TABLE `email_tracking` (
  `id` varchar(36) NOT NULL,
  `content` text,
  `created_by` varchar(64) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `error_message` varchar(512) DEFAULT NULL,
  `from_email` varchar(128) DEFAULT NULL,
  `reply_to` varchar(64) DEFAULT NULL,
  `result` varchar(128) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `subject` varchar(128) DEFAULT NULL,
  `to_email` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
