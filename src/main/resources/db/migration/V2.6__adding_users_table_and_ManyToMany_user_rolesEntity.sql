CREATE TABLE IF NOT EXISTS users
(
    id   int          not null AUTO_INCREMENT,
    username varchar(250) not null,
    password varchar(250) not null,
    CONSTRAINT users_pk PRIMARY KEY (id)
);

CREATE TABLE if not exists user_to_role
(
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

insert into users(username, password) values ('vaslin','$2a$10$dKNJv.lffzZAJy54RedVPuMxbbKoOinER2lKXjnt7KfbF1ROq4qSm');
insert into user_to_role values(1,3);