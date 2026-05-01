CREATE DATABASE IF NOT EXISTS airlinedb;
USE airlinedb;

CREATE TABLE IF NOT EXISTS flights (
    flight_id INT PRIMARY KEY AUTO_INCREMENT,
    flight_name VARCHAR(100) NOT NULL,
    available_seats INT NOT NULL,
    price_per_seat DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    passenger_name VARCHAR(100) NOT NULL,
    flight_id INT NOT NULL,
    seats_booked INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (flight_id) REFERENCES flights(flight_id)
);

INSERT INTO flights (flight_name, available_seats, price_per_seat) VALUES
('Indigo 101', 100, 5000.00),
('Air India 202', 120, 6000.00),
('SpiceJet 303', 80, 4500.00);
