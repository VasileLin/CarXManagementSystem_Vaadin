CREATE TABLE if not exists model_goods
(
    model_id INT,
    good_id INT,
    PRIMARY KEY (model_id, good_id),
    FOREIGN KEY (model_id) REFERENCES car_model (id),
    FOREIGN KEY (good_id) REFERENCES cost_of_good (id)
);