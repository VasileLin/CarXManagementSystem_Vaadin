ALTER TABLE customers
    ADD COLUMN model_id int,
    ADD CONSTRAINT car_model_key FOREIGN KEY (model_id)
        REFERENCES car_model (id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION;