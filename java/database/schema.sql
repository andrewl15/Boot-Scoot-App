BEGIN TRANSACTION;

-- Drop tables if they exist
DROP TABLE IF EXISTS user_dance;
DROP TABLE IF EXISTS dance;
DROP TABLE IF EXISTS users;

-- Users table
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(200) NOT NULL,
    role VARCHAR(50) NOT NULL
);

-- Shared dances table
CREATE TABLE dance (
    dance_id SERIAL PRIMARY KEY,
    dance_name VARCHAR(100) NOT NULL,
    song_name VARCHAR(100) NOT NULL,
    artist_name VARCHAR(100) NOT NULL,
    count INTEGER NOT NULL,
    walls INTEGER NOT NULL,
    level VARCHAR(50) NOT NULL,
    copperknob_link VARCHAR(200),
    demo_url VARCHAR(200),
    tutorial_url VARCHAR(200)
);

-- User-dance mapping table (tracks learned status)
CREATE TABLE user_dance (
    user_id INTEGER NOT NULL REFERENCES users(user_id),
    dance_id INTEGER NOT NULL REFERENCES dance(dance_id),
    is_learned BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY(user_id, dance_id)
);

COMMIT TRANSACTION;
