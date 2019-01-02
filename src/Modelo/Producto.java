package Modelo;

public class Producto {

	private String id;
	private String nombre;
	private String descripcion;
	private float precioc;
	private float preciop;
	private Usuario user;
	private int cantidad;
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public float getPrecioc() {
		return precioc;
	}
	public void setPrecioc(float precioc) {
		this.precioc = precioc;
	}
	public float getPreciop() {
		return preciop;
	}
	public void setPreciop(float preciop) {
		this.preciop = preciop;
	}
	public Usuario getUser() {
		return user;
	}
	public void setUser(Usuario user) {
		this.user = user;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
}
