CREATE table if not exists services
(
    id         int          not null AUTO_INCREMENT,
    name       varchar(255) not null,
    price      decimal not null,
    CONSTRAINT service_pk PRIMARY KEY (id)
);

CREATE table if not exists cash
(
    id             int          not null AUTO_INCREMENT,
    transaction_no varchar(255) not null,
    price          decimal      not null,
    date           date         not null,
    status         varchar(20)  not null,
    details        varchar(1024) not null,
    customer_id int not null,
    CONSTRAINT cash_pk PRIMARY KEY (id),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE if not exists cash_service (
                              cash_id INT,
                              service_id INT,
                              PRIMARY KEY (cash_id, service_id),
                              FOREIGN KEY (cash_id) REFERENCES cash(id),
                              FOREIGN KEY (service_id) REFERENCES services(id)
);