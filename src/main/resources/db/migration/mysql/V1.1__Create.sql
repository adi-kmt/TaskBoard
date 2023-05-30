CREATE TABLE IF NOT EXISTS `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(100) NOT NULL,
  `user_email` varchar(70) NOT NULL,
  `user_password` varchar(150) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_name` (`user_name`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `boards` (
  `id` int NOT NULL AUTO_INCREMENT,
  `board_title` varchar(70) NOT NULL,
  `is_board_starred` bit DEFAULT 0 NOT NULL,
  PRIMARY KEY (`id`),
  INDEX (`board_title`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `buckets` (
  `id` int NOT NULL AUTO_INCREMENT,
  `bucket_title` varchar(70) NOT NULL,
  `board_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `bucket_title` (`bucket_title`),
  CONSTRAINT `boards_fk` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `labels` (
  `id` int NOT NULL AUTO_INCREMENT,
  `label_name` varchar(100) NOT NULL,
  `label_colour` varchar(70) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `label_name` (`label_name`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `cards` (
  `id` int NOT NULL AUTO_INCREMENT,
  `card_title` varchar(70) NOT NULL,
  `card_desc` text,
  `card_start_date` datetime NOT NULL,
  `card_end_date` datetime NOT NULL,
  `is_card_archived` bit DEFAULT 0 NOT NULL,
  `bucket_id` int NOT NULL,
  `label_id` int,
  `user_assigned_id` int NOT NULL,

  CONSTRAINT `cards_bucket_added_fk` FOREIGN KEY (`bucket_id`) REFERENCES `buckets` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `cards_user_added_fk` FOREIGN KEY (`user_assigned_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `cards_labels_added_fk` FOREIGN KEY (`label_id`) REFERENCES `labels` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `boards_user_added` (
  `user_id` int NOT NULL,
  `board_id` int NOT NULL,
  `user_role` ENUM ('user', 'admin') NOT NULL,

  PRIMARY KEY (`board_id`, `user_id`),
  CONSTRAINT `boards_user_added_fk_1` FOREIGN KEY (`board_id`) REFERENCES `boards` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `boards_user_added_fk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE=InnoDB;