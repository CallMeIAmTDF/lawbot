CREATE TABLE IF NOT EXISTS `users`
(
    `id`         varchar(255) NOT NULL,
    `created_at` datetime(6) DEFAULT NULL,
    `created_by` varchar(255) DEFAULT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `updated_by` varchar(255) DEFAULT NULL,
    `active`     bit(1)       DEFAULT NULL,
    `avatar`     varchar(255) DEFAULT NULL,
    `email`      varchar(255) DEFAULT NULL,
    `name`       varchar(255) DEFAULT NULL,
    `role`       varchar(255) DEFAULT 'USER',
    `password`   varchar(255) DEFAULT NULL,
    `is_locked`  bit(1)       DEFAULT 0,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO users (id, created_at, created_by, updated_at, updated_by, active, avatar, email, name, role, password, is_locked)
SELECT 'e692cd89-e09e-4651-afb8-8956d349ff6c',
       NOW(),
       'system',
       NOW(),
       'system',
       1,
       NULL,
       'user@gmail.com',
       'Default User',
       'USER',
       '$2a$10$EJkL.sXN6Tg.NHzrmTk7DeWJf2lO/QYAJk7x7S41T4iHlgfimeUQu',
       0 WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'user@gmail.com'
);
INSERT INTO users (id, created_at, created_by, updated_at, updated_by, active, avatar, email, name, role, password, is_locked)
SELECT 'f7a9b8cd-3f4b-4c6a-8c13-09ec03c00abc',
       NOW(),
       'system',
       NOW(),
       'system',
       1,
       NULL,
       'admin@gmail.com',
       'Default Admin',
       'ADMIN',
       '$2a$10$EJkL.sXN6Tg.NHzrmTk7DeWJf2lO/QYAJk7x7S41T4iHlgfimeUQu',
       0
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'admin@gmail.com'
);


CREATE TABLE IF NOT EXISTS `verification_code`
(
    `id`    bigint(20) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `code`  varchar(255) DEFAULT NULL,
    `email` varchar(255) DEFAULT NULL,
    `exp`   datetime(6) DEFAULT NULL,
    `type`  tinyint(4) DEFAULT NULL
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

COMMIT;