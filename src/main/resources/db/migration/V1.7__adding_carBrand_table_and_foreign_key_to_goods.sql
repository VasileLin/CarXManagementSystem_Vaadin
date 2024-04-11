CREATE TABLE IF NOT EXISTS car_brand
(
    id    int          not null AUTO_INCREMENT,
    brand varchar(250) not null,
    CONSTRAINT brand_pk PRIMARY KEY (id)
);

ALTER TABLE cost_of_good
    ADD COLUMN brand_id int,
    ADD CONSTRAINT brand_fk FOREIGN KEY (brand_id)
        REFERENCES car_brand (id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION;