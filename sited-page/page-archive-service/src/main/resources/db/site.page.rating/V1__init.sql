DROP TABLE IF EXISTS `page_rating_rating`;
CREATE TABLE `page_rating_rating` (
  `id` varchar(255) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `page_id` varchar(255) DEFAULT NULL,
  `total_score` int(11) DEFAULT NULL,
  `total_scored` int(11) DEFAULT NULL,
  `total_scored1` int(11) DEFAULT NULL,
  `total_scored2` int(11) DEFAULT NULL,
  `total_scored3` int(11) DEFAULT NULL,
  `total_scored4` int(11) DEFAULT NULL,
  `total_scored5` int(11) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `page_rating_tracking`;
CREATE TABLE `page_rating_tracking` (
  `id` varchar(255) NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `page_id` varchar(255) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);