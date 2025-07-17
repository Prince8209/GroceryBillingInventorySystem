-- grocery_db.sql

-- Create users table
CREATE TABLE users (
    userid SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(10) NOT NULL CHECK (role IN ('admin', 'customer'))
);

-- Create products table
CREATE TABLE products (
    product_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    stock INT NOT NULL
);

-- Insert sample admin and customer
INSERT INTO users (username, password, role) VALUES
('admin1', 'adminpass', 'admin'),
('customer1', 'custpass', 'customer');

-- Insert sample products
INSERT INTO products (name, price, stock) VALUES
('Apple', 2.50, 100),
('Bread', 1.20, 50),
('Milk', 1.00, 30); 