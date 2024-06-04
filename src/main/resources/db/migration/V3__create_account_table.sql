CREATE TABLE IF NOT EXISTS account (
    username varchar(100) NOT NULL PRIMARY KEY,
    password varchar(1000) NOT NULL,
    roles varchar(1000)
);
