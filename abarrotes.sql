DROP DATABASE IF EXISTS abarrotes;
CREATE DATABASE IF NOT EXISTS abarrotes;
USE abarrotes;

-- Sucursales
DROP TABLE IF EXISTS sucursales;
CREATE TABLE sucursales (
	id_sucursal INT PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(100),
	ubicacion VARCHAR(255)
);

INSERT INTO sucursales (nombre, ubicacion) VALUES
('Sucursal Central', 'Av. Insurgentes Sur 1234, CDMX'),
('Sucursal Norte', 'Calzada Vallejo 456, CDMX'),
('Sucursal Sur', 'Periférico Sur 789, CDMX');

-- Empleados
DROP TABLE IF EXISTS empleados;
CREATE TABLE empleados (
	id_empleado INT PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(50) NOT NULL,
	usuario VARCHAR(25) NOT NULL UNIQUE,
	password_hash VARCHAR(100) NOT NULL,
    id_sucursal INT NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (id_sucursal) REFERENCES sucursales(id_sucursal)
);

-- Contraseña de ambos perfiles 1234
INSERT INTO empleados VALUES (1, 'Diego Arroyo', 'darroyo', '$2a$10$aCKzZl3s3tWZzUf3gYxQeeMKwZA.2IkdiPWxV0yGnZK92pNRp5oFe', 1, true), (2, 'Salma Navarro', 'snavarro', '$2a$10$/xTJg2Yjws5JOEOfivKAOuHg1IoY4MaU01S6gAVq8GZG/XsEkgKRS', 1, true);

-- Roles
DROP TABLE IF EXISTS roles;
CREATE TABLE roles (
	id_rol INT PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(25) NOT NULL UNIQUE
);

INSERT INTO roles VALUES (1, 'Gerente'), (2, 'Cajero'), (3, 'Almacenista');

-- Rol del empleado
DROP TABLE IF EXISTS empleado_roles;
CREATE TABLE empleado_roles (
	id_empleado INT NOT NULL,
	id_rol INT NOT NULL,
	FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado),
	FOREIGN KEY (id_rol) REFERENCES roles(id_rol),
	PRIMARY KEY (id_empleado, id_rol)
);

INSERT INTO empleado_roles VALUES (1, 1), (2, 2);

-- Cajas
DROP TABLE IF EXISTS cajas;
CREATE TABLE cajas (
  id_caja INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL
);

INSERT INTO cajas VALUES (1 , "Caja 1"), (2 , "Caja 2"), (3 , "Caja 3"), (4 , "Caja 4"), (5 , "Caja 5"), (6 , "Caja 6");

-- Clientes
DROP TABLE IF EXISTS clientes;
CREATE TABLE clientes (
	correo VARCHAR(50) PRIMARY KEY,
	nombre VARCHAR(50) NOT NULL,
	telefono VARCHAR(15)
);

-- Proveedores
DROP TABLE IF EXISTS proveedores;
CREATE TABLE proveedores (
	id_proveedor INT PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(50) NOT NULL,
	telefono VARCHAR(15),
	correo VARCHAR(50)
);

-- Productos
DROP TABLE IF EXISTS productos;
CREATE TABLE productos (
	id_producto INT PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(100) NOT NULL,
	imagen VARCHAR(255),
	precio DOUBLE NOT NULL
);

INSERT INTO productos (nombre, imagen, precio) VALUES
('Arroz 1kg', '/img/productos/arroz.png', 22.50),
('Frijol negro 1kg', '/img/productos/frijol_negro.png', 28.00),
('Aceite vegetal 1L', '/img/productos/aceite_vegetal.png', 45.90),
('Harina de trigo 1kg', '/img/productos/harina_trigo.png', 19.75),
('Azúcar 1kg', '/img/productos/azucar.png', 23.20),
('Sal yodada 500g', '/img/productos/sal_yodada.png', 8.50),
('Leche entera 1L', '/img/productos/leche_entera.png', 18.90),
('Huevos 12 piezas', '/img/productos/huevos.png', 36.00),
('Pan de caja integral', '/img/productos/pan_integral.png', 34.75),
('Mantequilla 250g', '/img/productos/mantequilla.png', 31.00),
('Queso manchego 200g', '/img/productos/queso_manchego.png', 42.50),
('Jamón de pierna 250g', '/img/productos/jamon_pierna.png', 39.99),
('Café soluble 100g', '/img/productos/cafe.png', 55.00),
('Té de manzanilla 25 sobres', '/img/productos/te_manzanilla.png', 24.00),
('Agua embotellada 1.5L', '/img/productos/agua.png', 13.25),
('Refresco cola 2L', '/img/productos/refresco.png', 29.90),
('Papel higiénico 4 rollos', '/img/productos/papel_higienico.png', 33.50),
('Shampoo 750ml', '/img/productos/shampoo.png', 59.00),
('Detergente en polvo 1kg', '/img/productos/detergente.png', 37.25),
('Jabón de baño 3 piezas', '/img/productos/jabon_bano.png', 21.75);

-- Inventario por sucursal
DROP TABLE IF EXISTS inventarios;
CREATE TABLE inventarios (
	id_producto INT NOT NULL,
	id_sucursal INT NOT NULL,
	stock INT NOT NULL DEFAULT 0,
	PRIMARY KEY (id_producto, id_sucursal),
	FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
	FOREIGN KEY (id_sucursal) REFERENCES sucursales(id_sucursal)
);

-- Para Sucursal Central (id_sucursal = 1)
INSERT INTO inventarios (id_producto, id_sucursal, stock) VALUES
(1, 1, 150),
(2, 1, 120),
(3, 1, 100),
(4, 1, 180),
(5, 1, 140),
(6, 1, 200),
(7, 1, 130),
(8, 1, 160),
(9, 1, 90),
(10, 1, 70),
(11, 1, 85),
(12, 1, 95),
(13, 1, 110),
(14, 1, 105),
(15, 1, 180),
(16, 1, 175),
(17, 1, 60),
(18, 1, 75),
(19, 1, 50),
(20, 1, 190);

-- Para Sucursal Norte (id_sucursal = 2)
INSERT INTO inventarios (id_producto, id_sucursal, stock) VALUES
(1, 2, 80),
(2, 2, 95),
(3, 2, 70),
(4, 2, 110),
(5, 2, 65),
(6, 2, 90),
(7, 2, 100),
(8, 2, 115),
(9, 2, 60),
(10, 2, 55),
(11, 2, 65),
(12, 2, 75),
(13, 2, 85),
(14, 2, 80),
(15, 2, 120),
(16, 2, 130),
(17, 2, 45),
(18, 2, 55),
(19, 2, 40),
(20, 2, 100);

-- Para Sucursal Sur (id_sucursal = 3)
INSERT INTO inventarios (id_producto, id_sucursal, stock) VALUES
(1, 3, 60),
(2, 3, 70),
(3, 3, 55),
(4, 3, 90),
(5, 3, 80),
(6, 3, 110),
(7, 3, 85),
(8, 3, 100),
(9, 3, 45),
(10, 3, 40),
(11, 3, 50),
(12, 3, 65),
(13, 3, 70),
(14, 3, 75),
(15, 3, 100),
(16, 3, 110),
(17, 3, 35),
(18, 3, 50),
(19, 3, 30),
(20, 3, 95);

-- Historial de accesos
DROP TABLE IF EXISTS historial_accesos;
CREATE TABLE historial_accesos (
  id_acceso INT PRIMARY KEY AUTO_INCREMENT,
  id_empleado INT NOT NULL,
  id_caja INT NULL,
  id_sucursal INT NULL,
  fecha_entrada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_salida TIMESTAMP NULL,
  FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado),
  FOREIGN KEY (id_caja) REFERENCES cajas(id_caja),
  FOREIGN KEY (id_sucursal) REFERENCES sucursales(id_sucursal)
);

-- Ventas
DROP TABLE IF EXISTS ventas;
CREATE TABLE ventas (
	id_venta INT PRIMARY KEY AUTO_INCREMENT,
	correo_cliente VARCHAR(50),
	id_empleado INT,
	id_caja INT,
	total DOUBLE NOT NULL,
	fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (correo_cliente) REFERENCES clientes(correo),
	FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado),
	FOREIGN KEY (id_caja) REFERENCES cajas(id_caja)
);

-- Detalle de venta
DROP TABLE IF EXISTS detalle_ventas;
CREATE TABLE detalle_ventas (
	id_detalle INT PRIMARY KEY AUTO_INCREMENT,
	id_venta INT,
	id_producto INT,
	cantidad INT,
	precio_unitario DOUBLE,
	FOREIGN KEY (id_venta) REFERENCES ventas(id_venta),
	FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

-- Productos Pedido
CREATE TABLE productos_pedidos (
    id_producto INT,
    id_sucursal INT,
    cantidad INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id_producto, id_sucursal),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    FOREIGN KEY (id_sucursal) REFERENCES sucursales(id_sucursal)
);

-- Pedidos a proveedores
DROP TABLE IF EXISTS pedidos;
CREATE TABLE pedidos (
	id_pedido INT PRIMARY KEY AUTO_INCREMENT,
	id_proveedor INT,
	id_empleado INT,
	fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor),
	FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado)
);

-- Detalle de pedido
DROP TABLE IF EXISTS detalle_pedidos;
CREATE TABLE detalle_pedidos (
	id_detalle INT PRIMARY KEY AUTO_INCREMENT,
	id_pedido INT,
	id_producto INT,
	cantidad INT,
	FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido),
	FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);