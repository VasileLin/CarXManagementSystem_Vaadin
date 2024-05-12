CREATE table if not exists services
(
    id         int          not null AUTO_INCREMENT,
    name       varchar(255) not null,
    price      decimal not null,
    CONSTRAINT service_pk PRIMARY KEY (id)
);

INSERT INTO services (name, price) VALUES
                                       ('Schimb ulei motor', 250.00),
                                       ('Schimb ulei cutie viteze', 300.00),
                                       ('Inlocuire filtru aer', 100.00),
                                       ('Inlocuire filtru combustibil', 150.00),
                                       ('Verificare și înlocuire plăcuțe frână', 200.00),
                                       ('Schimb lichid frână', 100.00),
                                       ('Aliniere roți', 120.00),
                                       ('Balansare roți', 80.00),
                                       ('Reparatie sistem climatizare', 350.00),
                                       ('Verificare și încărcare freon', 150.00),
                                       ('Reparatie sistem de iluminat', 90.00),
                                       ('Diagnoză computerizată', 150.00),
                                       ('Inspectie tehnică periodică', 200.00),
                                       ('Schimb lichid de răcire', 130.00),
                                       ('Schimb distribuție', 500.00),
                                       ('Verificare și reparație suspensii', 300.00),
                                       ('Înlocuire anvelope', 40.00),
                                       ('Reparatie motor', 1500.00),
                                       ('Reparatie cutie de viteze', 2000.00);

CREATE table if not exists cash
(
    id             int          not null AUTO_INCREMENT,
    transaction_no varchar(255) not null,
    price          decimal      not null,
    date           date         not null,
    status         varchar(20)  not null,
    details        varchar(1024) not null,
    receipt_path   varchar(1000) not null,
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