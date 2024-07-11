--liquibase formatted sql
--changeset joe:2

create table goods (
    _id uuid not null,
    name varchar(128) not null,
    cr_user uuid not null,
    cr_datetime timestamp,
    up_user uuid not null,
    up_datetime timestamp,
    primary key (_id),
    constraint fk_cr_user foreign key (cr_user) references system_user(_id),
    constraint fk_up_user foreign key (up_user) references system_user(_id)
);

--rollback drop table goods;
