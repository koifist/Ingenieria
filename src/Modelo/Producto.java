package Modelo;

public class Producto {

	public int id;
	public String nombre;
	public String descripcion;
        public Usuario user;
	public float precioc;
	public float preciop;
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
            if(roll==3)return precioc;
            else return preciop;
	}
	public void setPrecioc(float precioc) {
		this.precioc = precioc;
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
