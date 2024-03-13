CREATE table if not exists customers (
    id int not null AUTO_INCREMENT,
    name varchar(255) not null,
    phone varchar(255) not null,
    car_number varchar(255) not null,
    car_model varchar(255) not null,
    email varchar(255) not null,
    UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
    CONSTRAINT customer_pk PRIMARY KEY (id)
);