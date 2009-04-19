#
# database creation
#

CREATE DATABASE dtsdb;

#
# user creation to access db
#


#
# works for mysql 4
#

#USE mysql;
#INSERT INTO db VALUES ('localhost','dtsdb','nl','Y','Y','Y','Y','Y','Y','N','Y','Y','Y','Y','Y');
#INSERT INTO user VALUES ('localhost','dts_user',password('dts_pass'),'Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','N','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','','','','',0,0,0);

# 
# works for mysql 5
#

# This should work better
grant all privileges on dtsdb.* to 'dts_user'@'localhost'  IDENTIFIED BY 'dts_pass' WITH GRANT OPTION;

#USE mysql;
#INSERT INTO db VALUES ('localhost','dtsdb','nl','Y','Y','Y','Y','Y','Y','N','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y');
#INSERT INTO user VALUES ('localhost','dts_user',password('dts_pass'),'Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','N','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','Y','','','','',0,0,0,0);

#
# adding table
#

USE dtsdb;
CREATE TABLE simple_user (
   suUserId                     bigint auto_increment          not null,
   suUsername                   char(64) UNIQUE                not null,
   suPassword                   char(64)                       not null,
   suEmail                      text                           not null,
   suCreated                    datetime                       not null,
   primary key (suUserId)
) type = InnoDB;

#
# loading database with trash dummy data 
#

USE dtsdb;
INSERT INTO simple_user VALUES('', 'user1', 'password9', 'oprokhorenko@gmail.com', NOW());
INSERT INTO simple_user VALUES('', 'user2', 'password8', 'oprokhorenko@gmail.com', NOW());
INSERT INTO simple_user VALUES('', 'user3', 'password7', 'oprokhorenko@gmail.com', NOW());
INSERT INTO simple_user VALUES('', 'user4', 'password6', 'oprokhorenko@gmail.com', NOW());
INSERT INTO simple_user VALUES('', 'user5', 'password5', 'oprokhorenko@gmail.com', NOW());
INSERT INTO simple_user VALUES('', 'user6', 'password4', 'oprokhorenko@gmail.com', NOW());
INSERT INTO simple_user VALUES('', 'user7', 'password3', 'oprokhorenko@gmail.com', NOW());
INSERT INTO simple_user VALUES('', 'user8', 'password2', 'oprokhorenko@gmail.com', NOW());
INSERT INTO simple_user VALUES('', 'user9', 'password1', 'oprokhorenko@gmail.com', NOW());
