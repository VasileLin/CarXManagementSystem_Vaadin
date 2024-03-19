CREATE TABLE if not exists cash_goods
(
    cash_id INT,
    good_id INT,
    PRIMARY KEY (cash_id, good_id),
    FOREIGN KEY (cash_id) REFERENCES cash (id),
    FOREIGN KEY (good_id) REFERENCES cost_of_good (id)
);