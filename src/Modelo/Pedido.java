package Modelo;

public class Pedido {
	public String id;
	public Usuario user;
	public float precio;
	public String descripcion;
        public boolean recepcion;
        
        
        public boolean getRecepcion() {
		return recepcion;
	}
	public void setId(boolean recepcion) {
		this.recepcion = recepcion;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Usuario getUser() {
		return user;
	}
	public void setUser(Usuario user) {
		this.user = user;
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
