# --- !Ups
 
CREATE TABLE users ( 
  username      VARCHAR(25)  NOT NULL
, first_name    VARCHAR(20)  NOT NULL
, last_name     VARCHAR(20)  NOT NULL
, email         VARCHAR(100) NOT NULL
);

CREATE TABLE time_log (
  id         SERIAL       NOT NULL PRIMARY KEY
, username   VARCHAR(25)  NOT NULL REFERENCES users(username)
, date       DATE         NOT NULL 
, category   VARCHAR(50)  NOT NULL
, hours      INTEGER      NOT NULL
, minutes    INTEGER      NOT NULL
, created    TIMESTAMP    NOT NULL DEFAULT NOW()
, updated    TIMESTAMP    
);

# --- !Downs

DROP TABLE time_log;
DROP TABLE users;

