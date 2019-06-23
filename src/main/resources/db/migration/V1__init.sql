CREATE TABLE `user` (
                      `id` INT NOT NULL AUTO_INCREMENT COMMENT 'Primary key.',
                      `domain` varchar(255) NOT NULL,
                      `user_id` varchar(255) NOT NULL,
                      `metadata` json DEFAULT NULL,
                      `status` varchar(255) DEFAULT NULL,
                      PRIMARY KEY (`id`),
                      UNIQUE KEY uk_domain_user_id (`domain`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
