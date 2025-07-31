BEGIN TRANSACTION;

DROP TABLE IF EXISTS dance, users;

CREATE TABLE users (
	user_id SERIAL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);

CREATE TABLE dance (
	dance_id integer NOT NULL,
	user_id integer NOT NULL,
	is_learned boolean NOT NULL DEFAULT false,
	dance_name varchar(100) NOT NULL,
	song_name varchar(100) NOT NULL,
	artist_name varchar(100) NOT NULL,
	count integer NOT NULL,
	walls integer NOT NULL,
	level varchar(50) NOT NULL,
	copperknob_link varchar(200),
	demo_url varchar(200),
	tutorial_url varchar(200),
	CONSTRAINT PK_dance PRIMARY KEY (dance_id)
);

alter table dance add constraint FK_dance_user foreign key (user_id) references users(user_id);


COMMIT TRANSACTION;
