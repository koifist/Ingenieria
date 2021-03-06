package Modelo;

public class Pedido {
    
    public int id;
    public Usuario usuario;
    public float precio;
    public String descripcion;
    public String fecha;
    public int estado;

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getFecha() {
            return fecha;
    }

    public void setFecha(String fecha) {
            this.fecha = fecha;
    }
    
    public int getId() {
            return id;
    }
    
    public void setId(int id) {
            this.id = id;
    }
    
    public Usuario getUsuario() {
            return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
    }
    
    public float getPrecio() {
            return precio;
    }
    
    public void setPrecio(float precio) {
            this.precio = precio;
    }
    
    public String getDescripcion() {
            return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
    }
}
