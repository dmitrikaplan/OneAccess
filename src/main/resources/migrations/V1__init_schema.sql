create table users
(
    id         bigserial primary key,
    username   text not null,
    email      text not null unique,
    enabled    bool not null,
    first_name text not null,
    last_name  text not null,
    password   text not null
);

create table role
(
    id   bigserial primary key,
    name text not null unique
);

create table role_user
(
    role_id bigint not null,
    user_id bigint not null,
    primary key (role_id, user_id)
);

create table permission
(
    id   bigserial not null,
    name text      not null
);

create table role_permission
(
    role_id       bigint not null,
    permission_id bigint not null,
    primary key (role_id, permission_id)
);

CREATE TABLE oauth2_registered_client
(
    id                            varchar(100)  NOT NULL,
    client_id                     varchar(100)  NOT NULL,
    client_id_issued_at           timestamp,
    client_secret                 varchar(200)  DEFAULT NULL,
    client_secret_expires_at      timestamp,
    client_name                   varchar(200)  NOT NULL,
    client_authentication_methods varchar(1000) NOT NULL,
    authorization_grant_types     varchar(1000) NOT NULL,
    redirect_uris                 varchar(1000) DEFAULT NULL,
    post_logout_redirect_uris     varchar(1000) DEFAULT NULL,
    scopes                        varchar(1000) NOT NULL,
    client_settings               varchar(2000) NOT NULL,
    token_settings                varchar(2000) NOT NULL,
    PRIMARY KEY (id)
);


CREATE UNIQUE INDEX idx_oauth2_registered_client_client_id
    ON oauth2_registered_client (client_id);


insert into permission(name)
values ('READ_USERS'),
       ('WRITE_USERS'),
       ('READ_CLIENTS'),
       ('WRITE_CLIENTS'),
       ('READ_SETTINGS'),
       ('WRITE_SETTINGS'),
       ('WRITE_ROLES'),
       ('READ_ROLES');