create table users(
                      id serial,
                      login varchar(50) primary key not null,
                      "password" varchar(100) not null
);


create table files(
                      file_name varchar(255) primary key,
                      "size" bigint,
                      "type" varchar(255),
                      file_content bytea not null
);

create table tokens(
    "token" varchar(255) primary key
);