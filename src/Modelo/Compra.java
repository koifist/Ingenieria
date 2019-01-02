package Modelo;

public class Compra {
	private String id;
	private Usuario user;
	private float precio;
	private String descripcion;
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
