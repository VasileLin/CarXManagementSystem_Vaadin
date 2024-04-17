ALTER TABLE model_goods
    DROP FOREIGN KEY model_goods_ibfk_1;
ALTER TABLE model_goods
    ADD CONSTRAINT model_goods_ibfk_1
        FOREIGN KEY (model_id)
            REFERENCES car_model (id)
            ON UPDATE NO ACTION;