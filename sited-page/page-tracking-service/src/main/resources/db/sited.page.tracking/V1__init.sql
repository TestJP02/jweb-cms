DROP TABLE IF EXISTS `page_tracking_daily_tracking`;
CREATE TABLE `page_tracking_daily_tracking` (
  `id` varchar(64) NOT NULL,
  `category_id` varchar(64) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `date` varchar(16) DEFAULT NULL,
  `page_id` varchar(64) DEFAULT NULL,
  `total_visited` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `page_tracking_monthly_tracking`;
CREATE TABLE `page_tracking_monthly_tracking` (
  `id` varchar(64) NOT NULL,
  `category_id` varchar(64) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `month` varchar(8) DEFAULT NULL,
  `page_id` varchar(64) DEFAULT NULL,
  `total_visited` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `page_tracking_tracking`;
CREATE TABLE `page_tracking_tracking` (
  `id` varchar(64) NOT NULL,
  `category_id` varchar(64) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `page_id` varchar(64) DEFAULT NULL,
  `total_visited` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `page_tracking_weekly_tracking`;
CREATE TABLE `page_tracking_weekly_tracking` (
  `id` varchar(64) NOT NULL,
  `category_id` varchar(64) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `page_id` varchar(64) DEFAULT NULL,
  `total_visited` int(11) DEFAULT NULL,
  `week` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
);