CREATE TABLE IF NOT EXISTS song (
    id       BIGINT PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    artist   VARCHAR(100) NOT NULL,
    album    VARCHAR(100) NOT NULL,
    duration VARCHAR(10)  NOT NULL,
    year     VARCHAR(4)
);
