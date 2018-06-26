DROP TABLE IF EXISTS `page_archive`;
CREATE TABLE `page_archive` (
  `id` varchar(36) NOT NULL,
  `count` int(11) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `page_category`;
CREATE TABLE `page_category` (
  `id` varchar(36) NOT NULL,
  `created_by` varchar(63) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `description` varchar(1023) DEFAULT NULL,
  `display_name` varchar(127) DEFAULT NULL,
  `display_order` int(11) DEFAULT NULL,
  `group_id` varchar(255) DEFAULT NULL,
  `group_roles` varchar(255) DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `keywords` varchar(511) DEFAULT NULL,
  `others_roles` varchar(255) DEFAULT NULL,
  `owner_id` varchar(255) DEFAULT NULL,
  `owner_roles` varchar(255) DEFAULT NULL,
  `parent_id` varchar(36) DEFAULT NULL,
  `parent_ids` varchar(512) DEFAULT NULL,
  `path` varchar(511) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `tags` varchar(511) DEFAULT NULL,
  `template_path` varchar(255) DEFAULT NULL,
  `updated_by` varchar(63) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `page_content`;
CREATE TABLE `page_content` (
  `id` varchar(36) NOT NULL,
  `content` text,
  `created_by` varchar(64) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `page_id` varchar(36) DEFAULT NULL,
  `updated_by` varchar(64) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `page_draft`;
CREATE TABLE `page_draft` (
  `id` varchar(36) NOT NULL,
  `category_id` varchar(36) DEFAULT NULL,
  `content` text,
  `created_by` varchar(64) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `fields` varchar(2048) DEFAULT NULL,
  `image_url` varchar(512) DEFAULT NULL,
  `image_urls` varchar(2048) DEFAULT NULL,
  `keywords` varchar(1024) DEFAULT NULL,
  `page_id` varchar(36) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `tags` varchar(512) DEFAULT NULL,
  `template_path` varchar(255) DEFAULT NULL,
  `title` varchar(256) DEFAULT NULL,
  `updated_by` varchar(64) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `page_draft_changes`;
CREATE TABLE `page_draft_changes` (
  `id` varchar(36) NOT NULL,
  `created_by` varchar(64) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `difference` text,
  `draft_id` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `page_keyword`;
CREATE TABLE `page_keyword` (
  `id` varchar(36) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `page_page`;
CREATE TABLE `page_page` (
  `id` varchar(36) NOT NULL,
  `category_id` varchar(36) DEFAULT NULL,
  `created_by` varchar(64) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `description` varchar(512) DEFAULT NULL,
  `fields` varchar(2048) DEFAULT NULL,
  `image_url` varchar(512) DEFAULT NULL,
  `image_urls` varchar(2048) DEFAULT NULL,
  `keywords` varchar(512) DEFAULT NULL,
  `path` varchar(128) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `tags` varchar(512) DEFAULT NULL,
  `template_path` varchar(255) DEFAULT NULL,
  `title` varchar(256) DEFAULT NULL,
  `total_commented` int(11) DEFAULT NULL,
  `total_disliked` int(11) DEFAULT NULL,
  `total_liked` int(11) DEFAULT NULL,
  `total_visited` int(11) DEFAULT NULL,
  `updated_by` varchar(64) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `user_id` varchar(36) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `page_saved_component`;
CREATE TABLE `page_saved_component` (
  `id` varchar(36) NOT NULL,
  `attributes` varchar(2048) DEFAULT NULL,
  `component_name` varchar(255) DEFAULT NULL,
  `created_by` varchar(64) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `updated_by` varchar(64) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `page_tag`;
CREATE TABLE `page_tag` (
  `id` varchar(36) NOT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `path` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `total_tagged` int(11) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `page_template`;
CREATE TABLE `page_template` (
  `id` varchar(36) NOT NULL,
  `created_by` varchar(64) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `display_name` varchar(256) DEFAULT NULL,
  `path` varchar(128) DEFAULT NULL,
  `read_only` bit(1) DEFAULT NULL,
  `sections` text,
  `status` int(11) DEFAULT NULL,
  `updated_by` varchar(64) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `page_variable`;
CREATE TABLE `page_variable` (
  `id` varchar(36) NOT NULL,
  `created_by` varchar(64) DEFAULT NULL,
  `created_time` datetime DEFAULT NULL,
  `fields` text,
  `name` varchar(128) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `updated_by` varchar(64) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
);