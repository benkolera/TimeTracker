# --- !Ups
 
CREATE TABLE users ( 
  username      VARCHAR(25) NOT NULL
, first_name    VARCHAR(20) NOT NULL
, last_name     VARCHAR(20) NOT NULL
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

INSERT INTO users (username,first_name,last_name) VALUES
  ('bkolera'    ,'Ben'   ,'Kolera'     )
, ('chenry'     ,'Chenry','Henry'      )
, ('cmckay'     ,'Chris' ,'McKay'      )
, ('dmeiklejohn','MJ'    ,'Meiklejohn' )
, ('gmc'        ,'Glen'  ,'Cotterill'  )
, ('ncoughlin'  ,'Nick'  ,'Coughlin'   )
;

INSERT INTO time_log ( username , date, category , hours, minutes ) VALUES
  ('chenry'     ,'2013-02-05','Support',5,30)
, ('chenry'     ,'2013-02-05','WG 9.8' ,2,30)
, ('chenry'     ,'2013-02-06','WG 10'  ,2,0 )
, ('chenry'     ,'2013-02-06','Support',5,30)
, ('chenry'     ,'2013-02-06','WG 9.8' ,1,0 )
, ('dmeiklejohn','2013-02-07','WG 10'  ,4,30)
, ('dmeiklejohn','2013-02-07','Support',0,15)
, ('chenry'     ,'2013-02-07','Support',3,0 )
, ('chenry'     ,'2013-02-07','WG 9.8' ,1,30)
, ('dmeiklejohn','2013-02-08','Support',0,40)
, ('dmeiklejohn','2013-02-08','WG 10'  ,4,50)
, ('chenry'     ,'2013-02-08','Support',4,50)
, ('chenry'     ,'2013-02-08','WG 9.8' ,0,30)

# --- !Downs

DROP TABLE time_log;
DROP TABLE users;

