INSERT INTO users VALUES (1,'giga@gmail.com','password','user','username');
INSERT INTO countries VALUES (1,'GE','Georgia');
INSERT INTO teams (TEAM_NAME,COUNTRY_ID,TEAM_VALUE,USER_ID,BUDGET,USERNAME)
    VALUES ('TEAM',1,1000.00,1, 1000.00,'');
INSERT INTO teams (TEAM_NAME,COUNTRY_ID,TEAM_VALUE,USER_ID,BUDGET,USERNAME)
    VALUES ('TEAM2',1,1000.00,1, 1000.00,'username2');
INSERT INTO players (PLAYER_ID,FIRST_NAME,LAST_NAME,AGE,PRICE,COUNTRY_ID,POSITION,TEAM_ID)
    VALUES (1,'Giga','Chalauri',19,1000.00,1,'Goalkeeper',1);