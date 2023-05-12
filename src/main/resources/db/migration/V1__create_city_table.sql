CREATE TABLE IF NOT EXISTS `city` (
    `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(100),
    `photo` varchar(2050) -- 2048 is the max length of a URL
)
