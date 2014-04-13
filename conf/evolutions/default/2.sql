# --- !Ups
 
INSERT INTO users (username,first_name,last_name,email) VALUES
  ('bkolera'    ,'Ben'   ,'Kolera'     , 'bkolera@iseek.com.au'           )
, ('chenry'     ,'Chenry','Henry'      , 'chenry@iseek.com.au'            )
, ('cmckay'     ,'Chris' ,'McKay'      , 'cmckay@iseek.com.au'            )
, ('dmeiklejohn','MJ'    ,'Meiklejohn' , 'dmeiklejohn@iseek.com.au'       )
, ('gmc'        ,'Glen'  ,'Cotterill'  , 'glen@iseek.com.au'              )
, ('ncoughlin'  ,'Nick'  ,'Coughlin'   , 'Nicholas.Coughlin@iseek.com.au' )
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

DELETE FROM time_log;
DELETE FROM users;

