CREATE TABLE if not exists roles (
    id int NOT NULL AUTO_INCREMENT,
    name varchar(255) NOT NULL,
    CONSTRAINT roles_pkey PRIMARY KEY (id)
);

insert ignore into roles (id, name)
values
(1, 'CASHIER'),
(2, 'MANAGER'),
(3, 'ADMIN');

CREATE table if not exists employers (
    id int not null AUTO_INCREMENT,
    role_id int NOT NULL,
    full_name varchar(255) not null,
    date_of_birth date not null,
    phone varchar(255) not null,
    email varchar(255) not null,
    address varchar(255) not null,
    UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
    CONSTRAINT employer_pk PRIMARY KEY (id),
    CONSTRAINT role_fk FOREIGN KEY (role_id) REFERENCES roles(id)
);