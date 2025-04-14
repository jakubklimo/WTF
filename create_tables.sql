USE WTF;

CREATE TABLE country(
    id int NOT NULL AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE city(
    id int NOT NULL AUTO_INCREMENT,
    name VARCHAR(200) NOT NULL,
    latitude DECIMAL(9,6) NOT NULL,
    longitude DECIMAL(9,6) NOT NULL,
    country_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (country_id) REFERENCES country(id)
);

CREATE TABLE measurement(
    id int NOT NULL AUTO_INCREMENT,
    datetime DATETIME NOT NULL,
    temperature DECIMAL(7,2) NOT NULL,
    pressure DECIMAL(7,2) NOT NULL,
    humidity  DECIMAL(7,2) NOT NULL,
    temperature_min DECIMAL(7,2) NOT NULL,
    temperature_max DECIMAL(7,2) NOT NULL,
    weather_main VARCHAR(50) NOT NULL,
    weather_desc VARCHAR(200) NOT NULL,
    wind_speed DECIMAL(4,2) NOT NULL,
    city_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (city_id) REFERENCES city(id)
);