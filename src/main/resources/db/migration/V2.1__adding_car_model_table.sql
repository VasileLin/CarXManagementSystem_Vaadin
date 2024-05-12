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

INSERT INTO car_model (model, year, brand_id) VALUES
-- Toyota
('Camry', 2021, 1), ('Corolla', 2022, 1), ('Prius', 2020, 1),
-- Ford
('F-150', 2022, 2), ('Mustang', 2021, 2), ('Explorer', 2023, 2),
-- Volkswagen
('Golf', 2020, 3), ('Passat', 2021, 3), ('Tiguan', 2022, 3),
-- Nissan
('Altima', 2023, 4), ('Leaf', 2022, 4), ('Maxima', 2021, 4),
-- Honda
('Civic', 2022, 5), ('Accord', 2021, 5), ('CR-V', 2023, 5),
-- BMW
('3 Series', 2021, 6), ('X5', 2022, 6), ('Z4', 2020, 6),
-- Mercedes-Benz
('C-Class', 2021, 7), ('E-Class', 2022, 7), ('S-Class', 2023, 7),
-- Audi
('A4', 2021, 8), ('A6', 2022, 8), ('Q5', 2023, 8),
-- Chevrolet
('Silverado', 2022, 9), ('Malibu', 2021, 9), ('Equinox', 2023, 9),
-- Tesla
('Model S', 2022, 24), ('Model X', 2023, 24), ('Model 3', 2021, 24),
-- Volvo
('XC90', 2022, 22), ('S60', 2023, 22), ('V60', 2021, 22);

