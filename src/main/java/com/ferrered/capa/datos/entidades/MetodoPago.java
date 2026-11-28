package com.ferrered.capa.datos.entidades;

/**
 * Entidad MetodoPago (tabla metodo_pago).
 * Capa de datos - Modelo (MVC).
 */
public class MetodoPago {

    private int idMetodoPago;
    private String nombre;
    private String descripcion;
    private boolean estado;

    public MetodoPago() {
    }

    public int getIdMetodoPago() {
        return idMetodoPago;
    }

    public void setIdMetodoPago(int idMetodoPago) {
        this.idMetodoPago = idMetodoPago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
