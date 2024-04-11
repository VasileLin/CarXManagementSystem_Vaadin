CREATE TABLE IF NOT EXISTS good_category
(
    id   int          not null AUTO_INCREMENT,
    name varchar(250) not null,
    CONSTRAINT category_pk PRIMARY KEY (id)
);

ALTER TABLE cost_of_good
    ADD COLUMN category_id int,
    ADD CONSTRAINT category_fk FOREIGN KEY (category_id)
        REFERENCES good_category (id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION;
