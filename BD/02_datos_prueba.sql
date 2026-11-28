-- Datos iniciales y de prueba de FerreRed
-- Usuarios:  admin / admin123   |   cajero / cajero123   (contraseñas en SHA-256)

INSERT INTO rol (nombre, descripcion) VALUES
 ('Administrador', 'Acceso total al sistema'),
 ('Cajero', 'Ventas y consulta de alertas de stock');

INSERT INTO usuario (nombres, apellidos, dni, usuario, password_hash, email, id_rol) VALUES
 ('Admin', 'FerreRed', '00000001', 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin@ferrered.pe', 1),
 ('Caja', 'Principal', '00000002', 'cajero', '1ed4353e845e2e537e017c0fac3a0d402d231809b7989e90da15191c1148a93f', 'caja@ferrered.pe', 2),
 ('Usuario', 'Inactivo', '00000003', 'inactivo', '942847c088f463648246a2b21a483fbeb51ab92a75c45aaa808c9fea8a9b701a', NULL, 2);
UPDATE usuario SET estado = FALSE WHERE usuario = 'inactivo';

INSERT INTO metodo_pago (nombre, descripcion) VALUES
 ('Efectivo', 'Pago en efectivo'), ('Tarjeta', 'Débito / crédito'), ('Transferencia', 'Yape, Plin o banco');

INSERT INTO categoria (nombre, descripcion) VALUES
 ('Herramientas', 'Manuales y eléctricas'), ('Pinturas', 'Pinturas y accesorios'),
 ('Eléctricos', 'Cables, interruptores, focos'), ('Gasfitería', 'Tubos, llaves, conexiones'),
 ('Construcción', 'Cemento, fierro, clavos');

INSERT INTO cliente (nombres, apellidos, dni_ruc) VALUES ('Cliente', 'Varios', NULL);

INSERT INTO proveedor (ruc, razon_social, telefono, email) VALUES
 ('20100000001', 'Distribuidora Ferretera SAC', '014567890', 'ventas@distferretera.pe'),
 ('20100000002', 'Pinturas del Perú SAC', '014561234', 'pedidos@pinturasperu.pe');

INSERT INTO producto (codigo_barras, nombre, precio_compra, precio_venta, id_categoria) VALUES
 ('7750000000011', 'Martillo de uña 16 oz', 18.00, 27.00, 1),
 ('7750000000028', 'Desarmador plano 6"',   4.50,  8.00, 1),
 ('7750000000035', 'Pintura látex blanco 1 gal', 32.00, 45.00, 2),
 ('7750000000042', 'Cable eléctrico #14 (metro)', 1.20, 2.00, 3),
 ('7750000000059', 'Tubo PVC 1/2" x 3 m', 6.00, 9.50, 4),
 ('7750000000066', 'Cemento Portland 42.5 kg', 26.00, 31.00, 5);

INSERT INTO inventario (id_producto, stock_actual, stock_minimo) VALUES
 (1, 15, 5), (2, 3, 5), (3, 8, 4), (4, 200, 50), (5, 2, 10), (6, 40, 15);
-- productos 2 y 5 aparecen en la alerta de stock mínimo
