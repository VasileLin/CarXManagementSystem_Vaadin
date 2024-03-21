CREATE table if not exists company
(
    id         int          not null AUTO_INCREMENT,
    name       varchar(255) not null,
    address      varchar(255) not null,
    iban varchar(100) not null,
    CONSTRAINT customer_pk PRIMARY KEY (id)
);

CREATE table if not exists cost_of_good
(
    id         int          not null AUTO_INCREMENT,
    cost_name       varchar(255) not null,
    cost      decimal not null,
    date date not null,
    stock int not null,
    CONSTRAINT customer_pk PRIMARY KEY (id)
);
