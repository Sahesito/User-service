INSERT INTO users (first_name, last_name, email, role, active, phone, address, city, country)
SELECT 'Admin', 'SmartCommerce', 'admin@smartcommerce.com', 'ADMIN', true, '+53 987 654 321', 'La América, Carrera 79', 'Antioquia', 'Colombia'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@smartcommerce.com');

INSERT INTO users (first_name, last_name, email, role, active, phone, address, city, country)
SELECT 'Seller', 'SmartCommerce', 'seller@smartcommerce.com', 'SELLER', true, '+52 987 654 321', 'Av. Brasil 789', 'Lima', 'Peru'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'seller@smartcommerce.com');

INSERT INTO users (first_name, last_name, email, role, active, phone, address, city, country)
SELECT 'Client', 'SmartCommerce', 'client@smartcommerce.com', 'CLIENT', true, '+51 987 654 321', 'Puerto Madero', 'Buenos Aires', 'Argentina'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'client@smartcommerce.com');