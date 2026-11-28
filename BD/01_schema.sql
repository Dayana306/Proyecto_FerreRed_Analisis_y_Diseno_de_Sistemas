-- =========================================================
--  FerreRed - Esquema PostgreSQL 15+ (14 tablas + 2 vistas)
--  Crear antes la BD:  CREATE DATABASE ferrered_bd;
-- =========================================================

DROP VIEW IF EXISTS vista_reporte_ventas, vista_stock_minimo;
DROP TABLE IF EXISTS devolucion, comprobante, detalle_venta, venta, detalle_compra, compra,
    inventario, producto, categoria, cliente, proveedor, metodo_pago, usuario, rol CASCADE;

CREATE TABLE rol (
    id_rol       SERIAL PRIMARY KEY,
    nombre       VARCHAR(50)  NOT NULL UNIQUE,
    descripcion  VARCHAR(150),
    estado       BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE usuario (
    id_usuario     SERIAL PRIMARY KEY,
    nombres        VARCHAR(80)  NOT NULL,
    apellidos      VARCHAR(80)  NOT NULL,
    dni            VARCHAR(8)   UNIQUE,
    usuario        VARCHAR(50)  NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    email          VARCHAR(100),
    estado         BOOLEAN      NOT NULL DEFAULT TRUE,
    id_rol         INT          NOT NULL REFERENCES rol(id_rol)
);

CREATE TABLE metodo_pago (
    id_metodo_pago SERIAL PRIMARY KEY,
    nombre         VARCHAR(50) NOT NULL UNIQUE,
    descripcion    VARCHAR(150),
    estado         BOOLEAN     NOT NULL DEFAULT TRUE
);

CREATE TABLE categoria (
    id_categoria SERIAL PRIMARY KEY,
    nombre       VARCHAR(80) NOT NULL UNIQUE,
    descripcion  VARCHAR(200),
    estado       BOOLEAN     NOT NULL DEFAULT TRUE
);

CREATE TABLE producto (
    id_producto   SERIAL PRIMARY KEY,
    codigo_barras VARCHAR(50)    NOT NULL UNIQUE,
    nombre        VARCHAR(120)   NOT NULL,
    descripcion   VARCHAR(250),
    precio_compra NUMERIC(10,2)  NOT NULL CHECK (precio_compra >= 0),
    precio_venta  NUMERIC(10,2)  NOT NULL CHECK (precio_venta >= 0),
    estado        BOOLEAN        NOT NULL DEFAULT TRUE,
    id_categoria  INT            NOT NULL REFERENCES categoria(id_categoria)
);

CREATE TABLE inventario (
    id_inventario       SERIAL PRIMARY KEY,
    stock_actual        INT       NOT NULL DEFAULT 0 CHECK (stock_actual >= 0),
    stock_minimo        INT       NOT NULL DEFAULT 0 CHECK (stock_minimo >= 0),
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_producto         INT       NOT NULL UNIQUE REFERENCES producto(id_producto) ON DELETE CASCADE
);

CREATE TABLE proveedor (
    id_proveedor SERIAL PRIMARY KEY,
    ruc          VARCHAR(11)  NOT NULL UNIQUE,
    razon_social VARCHAR(150) NOT NULL,
    telefono     VARCHAR(20),
    direccion    VARCHAR(200),
    email        VARCHAR(100),
    estado       BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE cliente (
    id_cliente SERIAL PRIMARY KEY,
    nombres    VARCHAR(100) NOT NULL,
    apellidos  VARCHAR(100),
    dni_ruc    VARCHAR(11)  UNIQUE,
    telefono   VARCHAR(20),
    direccion  VARCHAR(200),
    email      VARCHAR(100),
    estado     BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE venta (
    id_venta       SERIAL PRIMARY KEY,
    fecha          TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    subtotal       NUMERIC(10,2) NOT NULL CHECK (subtotal >= 0),
    impuesto       NUMERIC(10,2) NOT NULL CHECK (impuesto >= 0),
    total          NUMERIC(10,2) NOT NULL CHECK (total >= 0),
    estado         VARCHAR(20)   NOT NULL DEFAULT 'Completada'
                   CHECK (estado IN ('Completada','Anulada','Devuelta parcial')),
    id_cliente     INT NOT NULL REFERENCES cliente(id_cliente),
    id_usuario     INT NOT NULL REFERENCES usuario(id_usuario),
    id_metodo_pago INT NOT NULL REFERENCES metodo_pago(id_metodo_pago)
);

CREATE TABLE detalle_venta (
    id_detalle_venta SERIAL PRIMARY KEY,
    cantidad         INT           NOT NULL CHECK (cantidad > 0),
    precio_unitario  NUMERIC(10,2) NOT NULL CHECK (precio_unitario >= 0),
    subtotal         NUMERIC(10,2) NOT NULL CHECK (subtotal >= 0),
    id_venta         INT NOT NULL REFERENCES venta(id_venta) ON DELETE CASCADE,
    id_producto      INT NOT NULL REFERENCES producto(id_producto)
);

CREATE TABLE comprobante (
    id_comprobante SERIAL PRIMARY KEY,
    tipo           VARCHAR(20)   NOT NULL CHECK (tipo IN ('Boleta','Factura','Ticket','Nota de credito')),
    serie          VARCHAR(4)    NOT NULL,
    numero         VARCHAR(8)    NOT NULL,
    fecha_emision  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    subtotal       NUMERIC(10,2) NOT NULL,
    impuesto       NUMERIC(10,2) NOT NULL,
    total          NUMERIC(10,2) NOT NULL,
    estado         VARCHAR(20)   NOT NULL DEFAULT 'Emitido',
    id_venta       INT NOT NULL REFERENCES venta(id_venta) ON DELETE CASCADE,
    UNIQUE (serie, numero)
);

CREATE TABLE compra (
    id_compra    SERIAL PRIMARY KEY,
    fecha        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    subtotal     NUMERIC(10,2) NOT NULL CHECK (subtotal >= 0),
    impuesto     NUMERIC(10,2) NOT NULL CHECK (impuesto >= 0),
    total        NUMERIC(10,2) NOT NULL CHECK (total >= 0),
    estado       VARCHAR(20)   NOT NULL DEFAULT 'Registrada',
    id_proveedor INT NOT NULL REFERENCES proveedor(id_proveedor),
    id_usuario   INT NOT NULL REFERENCES usuario(id_usuario)
);

CREATE TABLE detalle_compra (
    id_detalle_compra SERIAL PRIMARY KEY,
    cantidad          INT           NOT NULL CHECK (cantidad > 0),
    precio_unitario   NUMERIC(10,2) NOT NULL CHECK (precio_unitario >= 0),
    subtotal          NUMERIC(10,2) NOT NULL CHECK (subtotal >= 0),
    id_compra         INT NOT NULL REFERENCES compra(id_compra) ON DELETE CASCADE,
    id_producto       INT NOT NULL REFERENCES producto(id_producto)
);

CREATE TABLE devolucion (
    id_devolucion SERIAL PRIMARY KEY,
    fecha         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    motivo        VARCHAR(200)  NOT NULL,
    monto         NUMERIC(10,2) NOT NULL CHECK (monto >= 0),
    estado        VARCHAR(20)   NOT NULL DEFAULT 'Procesada',
    id_venta      INT NOT NULL REFERENCES venta(id_venta),
    id_usuario    INT NOT NULL REFERENCES usuario(id_usuario)
);

-- ---------- Vistas ----------
CREATE VIEW vista_stock_minimo AS
SELECT p.id_producto, p.codigo_barras, p.nombre, c.id_categoria, c.nombre AS categoria,
       i.stock_actual, i.stock_minimo
FROM producto p
JOIN inventario i ON i.id_producto = p.id_producto
JOIN categoria  c ON c.id_categoria = p.id_categoria
WHERE p.estado = TRUE AND i.stock_actual <= i.stock_minimo;

CREATE VIEW vista_reporte_ventas AS
SELECT v.id_venta, v.fecha, v.estado, u.id_usuario, u.usuario AS cajero,
       mp.id_metodo_pago, mp.nombre AS metodo_pago,
       p.id_producto, p.nombre AS producto, p.id_categoria,
       dv.cantidad, dv.precio_unitario, dv.subtotal,
       (dv.precio_unitario - p.precio_compra) * dv.cantidad AS ganancia_estimada
FROM venta v
JOIN usuario u       ON u.id_usuario = v.id_usuario
JOIN metodo_pago mp  ON mp.id_metodo_pago = v.id_metodo_pago
JOIN detalle_venta dv ON dv.id_venta = v.id_venta
JOIN producto p      ON p.id_producto = dv.id_producto;
