--liquibase formatted sql
--changeset joe:1

create table system_user (
    _id uuid not null,
    account varchar(128) not null,
    password varchar(128), 
    name varchar(128),
    primary key (_id),
    unique (account)
);

--rollback drop table system_user; 
