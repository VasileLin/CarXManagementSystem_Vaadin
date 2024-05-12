CREATE table if not exists company
(
    id         int          not null AUTO_INCREMENT,
    name       varchar(255) not null,
    address      varchar(255) not null,
    iban varchar(100) not null,
    CONSTRAINT customer_pk PRIMARY KEY (id)
);

insert into company(name, address, iban) values ('CarX Company','Address sample','I3B2A3N5S9A3M1P3L0E');

CREATE table if not exists cost_of_good
(
    id         int          not null AUTO_INCREMENT,
    cost_name       varchar(255) not null,
    cost      decimal not null,
    date date not null,
    stock int not null,
    car_model varchar(255),
    CONSTRAINT customer_pk PRIMARY KEY (id)
);

