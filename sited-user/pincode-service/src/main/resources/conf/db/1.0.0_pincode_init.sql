DROP TABLE IF EXISTS `pin_code_tracking`;
CREATE TABLE `pin_code_tracking` (
  `id` varchar(64) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ;
