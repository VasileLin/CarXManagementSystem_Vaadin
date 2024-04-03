CREATE table if not exists acquisition
(
    id          int     not null AUTO_INCREMENT,
    total_price decimal not null,
    quantity    int     not null,
    date        date    not null,
    CONSTRAINT acquisition_pk PRIMARY KEY (id)
);