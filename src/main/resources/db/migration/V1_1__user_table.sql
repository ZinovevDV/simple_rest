create table application_user(
    id uuid primary key ,
    username varchar(255) not null ,
    password varchar(255) not null
);

create unique index uix_application_user_username on application_user(username);

alter table task
add column id_application_user uuid not null references application_user(id);

create index ix_task_application_user on task(id_application_user);