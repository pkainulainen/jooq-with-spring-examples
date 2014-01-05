create table if not exists todos (
  id bigint auto_increment primary key,
  creation_time timestamp not null default current_timestamp,
  description varchar(500),
  modification_time timestamp not null default current_timestamp,
  title varchar(100)
);