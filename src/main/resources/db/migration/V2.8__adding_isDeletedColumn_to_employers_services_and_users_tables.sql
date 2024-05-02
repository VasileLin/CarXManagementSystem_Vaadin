ALTER TABLE services
    ADD COLUMN is_deleted BIT(1) NOT NULL;

ALTER TABLE employers
    ADD COLUMN is_deleted BIT(1) NOT NULL;

ALTER TABLE users
    ADD COLUMN is_deleted BIT(1) NOT NULL;


