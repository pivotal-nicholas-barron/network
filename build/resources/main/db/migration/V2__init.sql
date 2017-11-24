create table user (username varchar(255) not null, enabled integer not null, password varchar(255), role varchar(255), primary key (username));

create table note (note_id integer not null, content varchar(255), timestamp bigint not null, username varchar(255), primary key (note_id));

create table friend (email varchar(255) not null, friend_email varchar(255), pending bit, primary key (email));

create table comment (id integer not null, comment_body varchar(255), note_id integer, parent_comment_id integer, primary key (id));