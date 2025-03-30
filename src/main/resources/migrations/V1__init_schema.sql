create table users(
    id bigserial primary key,
    username text not null unique,
    email text not null unique,
    enabled bool not null,
    first_name text not null,
    last_name text not null,
    password text not null
);

create table role(
    id bigserial primary key,
    name text not null unique
);

create table role_user(
    role_id bigint not null,
    user_id bigint not null,
    primary key (role_id, user_id)
);

create table permission(
    id bigserial not null,
    name text not null
);

create table role_permission(
    role_id bigint not null,
    permission_id bigint not null,
    primary key (role_id, permission_id)
);

create table client(
    id bigserial primary key,
    client_id text not null,
    client_secret text not null,
    enabled bool not null
);

create table client_client_scope(
    client_id bigint not null,
    client_scope text not null,
    primary key (client_id, client_scope)
);