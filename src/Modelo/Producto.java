package Modelo;

public class Producto {

    public int id;
    public String nombre;
    public String descripcion;
    public Usuario usuario;
    public float precio_cliente;
    public float precio_proveedor;
    public int cantidad;
    public int estado;

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    public int getId() {
            return id;
    }
    
    public void setId(int id) {
            this.id = id;
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
    
    public float getPrecio(int roll) {
        if(roll==3)return precio_cliente;
        else return precio_proveedor;
    }
    
    public void setPrecio_cliente(float precio_cliente) {
            this.precio_cliente = precio_cliente;
    }
    
    public void setPrecio_proveedor(float precio_proveedor) {
            this.precio_proveedor = precio_proveedor;
    }
    
    public Usuario getUsuario() {
            return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
    }
    
    public int getCantidad() {
            return cantidad;
    }
    
    public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
    }
}
