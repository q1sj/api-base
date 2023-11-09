CREATE TABLE `sys_log` (
                           `id` bigint(20) NOT NULL,
                           `method` varchar(255) NOT NULL,
                           `ip` varchar(15) DEFAULT NULL,
                           `username` varchar(255) DEFAULT NULL,
                           `url` varchar(255) DEFAULT NULL,
                           `args` text,
                           `resp` text,
                           `throwable` text DEFAULT NULL,
                           `cost` bigint(20) NOT NULL,
                           `record_time` datetime NOT NULL,
                           `create_time` datetime NOT NULL,
                           PRIMARY KEY (`id`),
                           KEY `idx_record_time` (`record_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;