ALTER TABLE customers
    ADD COLUMN is_deleted BIT(1) NOT NULL AFTER email;
