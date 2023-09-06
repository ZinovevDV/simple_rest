create table task(
    id uuid primary key ,
    details text,
    completed boolean not null default false
);