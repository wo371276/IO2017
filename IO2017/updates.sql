
CREATE DATABASE io2017;
use io2017;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;
CREATE  TABLE users (
  userid int(11) NOT NULL AUTO_INCREMENT,
  username VARCHAR(45) NOT NULL UNIQUE,
  name VARCHAR(255) NOT NULL,
  surname VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(60) NOT NULL ,
  enabled BOOLEAN NOT NULL,
  PRIMARY KEY (userid));
  
CREATE TABLE user_roles (
  user_role_id int(11) NOT NULL AUTO_INCREMENT,
  userid int(11) NOT NULL,
  role varchar(45) NOT NULL,
  PRIMARY KEY (user_role_id),
  UNIQUE KEY uni_userid_role (role,userid),
  KEY fk_user_idx (userid),
  CONSTRAINT fk_userid FOREIGN KEY (userid) REFERENCES users (userid));

 -- to jest zakodowane hasło "123"
INSERT INTO users VALUES(1, 'admin', 'Admin', 'Adminek', 'admin@test.com', 
					'$2a$04$p3MKqXK.2VKPh96wdm/sDOZxnpe2qaCzC/JS4ilHDBX9M/SjnAMV6',
                    TRUE);
                    
INSERT INTO user_roles (userid, role)
VALUES (001, 'ROLE_ADMIN');
