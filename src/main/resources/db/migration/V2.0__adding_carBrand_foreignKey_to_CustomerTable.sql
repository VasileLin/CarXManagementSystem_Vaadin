ALTER TABLE customers
    ADD COLUMN brand_id int,
    ADD CONSTRAINT car_brand_fk FOREIGN KEY (brand_id)
        REFERENCES car_brand (id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION;