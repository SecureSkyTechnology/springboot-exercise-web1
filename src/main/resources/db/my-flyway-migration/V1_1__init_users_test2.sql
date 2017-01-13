CREATE TABLE USERS_TEST2(
ID INT AUTO_INCREMENT PRIMARY KEY, 
EMAIL VARCHAR(255) NOT NULL,
PASSWORD VARCHAR(255) NOT NULL,
NICKNAME VARCHAR(255) NOT NULL,
AGE INT DEFAULT 0,
BIRTHDAY DATE NOT NULL,
CREATED_AT TIMESTAMP DEFAULT NOW(),
UPDATED_AT TIMESTAMP DEFAULT NOW()
);

INSERT INTO USERS_TEST2(EMAIL, PASSWORD, NICKNAME, AGE, BIRTHDAY) VALUES 
  ('bar1@example.com', 'password', '日本語5', 21, '2002-02-01')
, ('bar2@example.com', 'password', '日本語6', 22, '2002-02-02')
, ('bar3@example.com', 'password', '日本語7', 23, '2002-02-03')
, ('bar4@example.com', 'password', '日本語8', 24, '2002-02-04')
, ('bar5@example.com', 'password', '日本語9', 25, '2002-02-05')
;
