DROP TABLE IF EXISTS `pincode_tracking`;
CREATE TABLE `pincode_tracking` (
  `id` varchar(36) NOT NULL,
  `text` varchar(16) DEFAULT NULL,
  `created_by` varchar(64) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `ip` varchar(16) DEFAULT NULL,
  `phone` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
);