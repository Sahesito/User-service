INSERT INTO users (first_name, last_name, email, role, active, phone, address, city, country)
VALUES ('Admin', 'SmartCommerce', 'admin@smartcommerce.com', 'ADMIN', true,
        '+51 999 000 001', 'Av. Javier Prado 123', 'San Isidro', 'Peru')
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (first_name, last_name, email, role, active, phone, address, city, country)
VALUES ('Seller', 'SmartCommerce', 'seller@smartcommerce.com', 'SELLER', true,
        '+51 999 000 002', 'Av. Arequipa 456', 'Miraflores', 'Peru')
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (first_name, last_name, email, role, active, phone, address, city, country)
VALUES ('Client', 'SmartCommerce', 'client@smartcommerce.com', 'CLIENT', true,
        '+51 999 000 003', 'Calle Las Flores 789', 'Lima', 'Peru')
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (first_name, last_name, email, role, active, phone, address, city, country)
VALUES ('Carlos', 'Administrador', 'carlos.admin@smartcommerce.com', 'ADMIN', true,
        '+51 912 345 678', 'Av. La Marina 987', 'San Miguel', 'Peru')
ON CONFLICT (email) DO NOTHING;

INSERT INTO users (first_name, last_name, email, role, active, phone, address, city, country)
VALUES ('María', 'Vendedora', 'maria.seller@smartcommerce.com', 'SELLER', true,
        '+51 923 456 789', 'Av. Universitaria 654', 'Los Olivos', 'Peru')
ON CONFLICT (email) DO NOTHING;