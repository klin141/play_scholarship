# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table comment (
  comment                   varchar(255))
;

create table likes (
  id                        bigint not null,
  likes                     integer,
  dislikes                  integer,
  constraint pk_likes primary key (id))
;

create table scholarship (
  id                        bigint not null,
  school                    varchar(255),
  description               varchar(255),
  amount                    integer,
  likes                     integer,
  dislikes                  integer,
  constraint pk_scholarship primary key (id))
;

create table user (
  email                     varchar(255) not null,
  name                      varchar(255),
  password                  varchar(255),
  constraint pk_user primary key (email))
;


create table scholarship_user (
  scholarship_id                 bigint not null,
  user_email                     varchar(255) not null,
  constraint pk_scholarship_user primary key (scholarship_id, user_email))
;
create sequence likes_seq;

create sequence scholarship_seq;

create sequence user_seq;




alter table scholarship_user add constraint fk_scholarship_user_scholarsh_01 foreign key (scholarship_id) references scholarship (id) on delete restrict on update restrict;

alter table scholarship_user add constraint fk_scholarship_user_user_02 foreign key (user_email) references user (email) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists comment;

drop table if exists likes;

drop table if exists scholarship;

drop table if exists scholarship_user;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists likes_seq;

drop sequence if exists scholarship_seq;

drop sequence if exists user_seq;

