CREATE TABLE IF NOT EXISTS car_model
(
    id    int          not null AUTO_INCREMENT,
    model varchar(250) not null,
    year int not null,
    brand_id int not null,
    CONSTRAINT model_pk PRIMARY KEY (id),
    CONSTRAINT car_model_fk FOREIGN KEY (brand_id)
        REFERENCES car_brand (id)
);