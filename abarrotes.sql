DROP DATABASE IF EXISTS abarrotes;
CREATE DATABASE IF NOT EXISTS abarrotes;
USE abarrotes;

-- Empleados
DROP TABLE IF EXISTS empleados;
CREATE TABLE empleados (
	id_empleado INT PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(50) NOT NULL,
	usuario VARCHAR(25) NOT NULL UNIQUE,
	password_hash VARCHAR(100) NOT NULL
);

-- Roles
DROP TABLE IF EXISTS roles;
CREATE TABLE roles (
	id_rol INT PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(25) NOT NULL UNIQUE
);

-- Rol del empleado
DROP TABLE IF EXISTS empleado_roles;
CREATE TABLE empleado_roles (
	id_empleado INT NOT NULL,
	id_rol INT NOT NULL,
	FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado),
	FOREIGN KEY (id_rol) REFERENCES roles(id_rol),
	PRIMARY KEY (id_empleado, id_rol)
);

-- Cajas
DROP TABLE IF EXISTS cajas;
CREATE TABLE cajas (
  id_caja INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL
);

-- Historial de accesos
DROP TABLE IF EXISTS historial_accesos;
CREATE TABLE historial_accesos (
  id_acceso INT PRIMARY KEY AUTO_INCREMENT,
  id_empleado INT NOT NULL,
  id_caja INT NOT NULL,
  fecha_entrada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  fecha_salida TIMESTAMP NULL,
  FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado),
  FOREIGN KEY (id_caja) REFERENCES cajas(id_caja)
);

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
	imagen VARCHAR(255), -- guarda ruta de imagen o base64
	precio DOUBLE NOT NULL
);

-- Almacenes
DROP TABLE IF EXISTS almacenes;
CREATE TABLE almacenes (
	id_almacen INT PRIMARY KEY AUTO_INCREMENT,
	nombre VARCHAR(100),
	ubicacion VARCHAR(255)
);

-- Inventario por almacén
DROP TABLE IF EXISTS inventarios;
CREATE TABLE inventarios (
	id_producto INT NOT NULL,
	id_almacen INT NOT NULL,
	stock INT NOT NULL DEFAULT 0,
	PRIMARY KEY (id_producto, id_almacen),
	FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
	FOREIGN KEY (id_almacen) REFERENCES almacenes(id_almacen)
);

-- Movimientos de inventario (entrada/salida)
DROP TABLE IF EXISTS movimientos_inventarios;
CREATE TABLE movimientos_inventarios (
	id_movimiento INT PRIMARY KEY AUTO_INCREMENT,
	id_producto INT,
	id_almacen INT,
	cantidad INT,
	tipo ENUM('ENTRADA', 'SALIDA'),
	fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	referencia VARCHAR(50), -- referencia de venta o pedido
	FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
	FOREIGN KEY (id_almacen) REFERENCES almacenes(id_almacen)
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

-- Pedidos a proveedores
DROP TABLE IF EXISTS pedidos;
CREATE TABLE pedidos (
	id_pedido INT PRIMARY KEY AUTO_INCREMENT,
	id_proveedor INT,
	id_empleado INT,
	total DOUBLE,
	fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	estado ENUM('PENDIENTE', 'RECIBIDO', 'CANCELADO') DEFAULT 'PENDIENTE',
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
	precio_unitario DOUBLE,
	FOREIGN KEY (id_pedido) REFERENCES pedidos(id_pedido),
	FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);