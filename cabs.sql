create database cab_booking;
use cab_booking;

-- Create the places table
CREATE TABLE places (
    place_name VARCHAR(100) PRIMARY KEY,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL
);

-- Create the users table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(100) NOT NULL,
    ride_status ENUM('inactive', 'waiting', 'riding') NOT NULL DEFAULT 'inactive'
);

-- Create the cabs table
CREATE TABLE cabs (
    licence_number VARCHAR(20) PRIMARY KEY,
    current_location VARCHAR(100),
    current_status ENUM('available', 'busy') NOT NULL DEFAULT 'available',
    FOREIGN KEY (current_location) REFERENCES places(place_name)
);

-- Create the bookings table
CREATE TABLE bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    start_location VARCHAR(100),
    drop_location VARCHAR(100),
    licence_number VARCHAR(20),
    user_id INT,
    FOREIGN KEY (start_location) REFERENCES places(place_name),
    FOREIGN KEY (drop_location) REFERENCES places(place_name),
    FOREIGN KEY (licence_number) REFERENCES cabs(licence_number),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

INSERT INTO places (place_name, latitude, longitude) VALUES
('Charminar', 17.3616, 78.4747),
('Gachibowli', 17.4401, 78.3489),
('Hitech City', 17.4483, 78.3915),
('Kondapur', 17.4592, 78.3525),
('Banjara Hills', 17.4148, 78.4294),
('Jubilee Hills', 17.4324, 78.4077),
('Begumpet', 17.4437, 78.4746),
('Madhapur', 17.4486, 78.3908),
('Secunderabad', 17.5043, 78.5426),
('Mehdipatnam', 17.3946, 78.4442);

INSERT INTO users (user_name, ride_status) VALUES
('Ankit Sharma', 'inactive'),
('Pooja Reddy', 'inactive'),
('Rajesh Kumar', 'inactive'),
('Sneha Verma', 'inactive'),
('Rahul Deshmukh', 'inactive'),
('Deepika Iyer', 'inactive'),
('Arjun Das', 'inactive'),
('Ravi Patel', 'inactive'),
('Nisha Gupta', 'inactive'),
('Vikram Choudhary', 'inactive'),
('Priya Singh', 'inactive'),
('Amitabh Jain', 'inactive'),
('Swati Bhardwaj', 'inactive'),
('Suresh Nair', 'inactive'),
('Sunita Bhosale', 'inactive'),
('Neha Kapoor', 'inactive'),
('Manoj Yadav', 'inactive'),
('Aarti Joshi', 'inactive'),
('Karan Mehra', 'inactive'),
('Shweta Malhotra', 'inactive');

INSERT INTO cabs (licence_number, current_location, current_status) VALUES
('AP09AB1234', 'Charminar', 'available'),
('TS07XY5678', 'Gachibowli', 'available'),
('AP09CD9101', 'Hitech City', 'available'),
('TS08EF2345', 'Kondapur', 'available'),
('TS09GH6789', 'Banjara Hills', 'available'),
('AP10IJ3456', 'Jubilee Hills', 'available'),
('TS11KL7890', 'Begumpet', 'available'),
('AP12MN4567', 'Madhapur', 'available'),
('TS13OP8901', 'Secunderabad', 'available'),
('AP14QR6789', 'Mehdipatnam', 'available');

select * from cabs;
select * from bookings;
select * from places;
select * from users;
truncate table bookings;

