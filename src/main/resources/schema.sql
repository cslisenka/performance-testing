CREATE TABLE IF NOT EXISTS `rooms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`,`name`)
);

CREATE UNIQUE INDEX name_UNIQUE ON rooms(name);

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(45) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`,`login`)
);

CREATE UNIQUE INDEX login_UNIQUE ON users(login);

CREATE TABLE IF NOT EXISTS `messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `text` varchar(255) CHARACTER SET utf8 NOT NULL,
  `time` datetime NOT NULL,
  `room_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `room_id_fk` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE INDEX room_id_index ON messages(room_id);
CREATE INDEX user_id_index ON messages(user_id);
CREATE INDEX time_index ON messages(time DESC);

DELETE FROM messages WHERE 1=1;
DELETE FROM rooms WHERE 1=1;
DELETE FROM users WHERE 1=1;