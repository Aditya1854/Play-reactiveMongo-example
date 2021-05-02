# --- !Ups
CREATE TABLE "university"("id" int primary key,"name" varchar(200),"location" varchar(200) );
insert into "university" values(1,'hcu','hyderabad');

CREATE TABLE "student"("id" int PRIMARY KEY AUTO_INCREMENT,"name" varchar(200) , "email" varchar(200) ,"university_id" int ,"DOB" Date,foreign key ("university_id") references "university"("id"));
insert into "student" values(1,'Aditya','aditya@gmail.com',1,'1995-05-03');
insert into "student" values(2,'Aman','aman@gmail.com',1,'1994-08-03');


# --- !Downs
DROP TABLE "student";
DROP TABLE "university";


