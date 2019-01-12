package Modelo;

import java.sql.*;
import java.util.ArrayList;

public class Conector {
	private static String url = "jdbc:mysql://db4free.net:3306/insosupermercado";
	private static String usuario = "insosuper";
	private static String pass = "inso1234";
	
			
	public Usuario getUser(String Id) throws SQLException{

		Usuario usr = new Usuario();
			//1. Crear conexion
			Connection conn=DriverManager.getConnection(url,usuario,pass);
			//2. Crear objeto statement
			PreparedStatement st = conn.prepareStatement("SELECT * FROM usuarios WHERE id=?");
			st.setNString(1, Id);
			ResultSet rs=st.executeQuery();
			if(rs.next()) {
			usr.setId(rs.getString(1));
			usr.setPass(rs.getString(2));
			usr.setNombre(rs.getString(3));
			usr.setApellido1(rs.getString(4));
			usr.setApellido2(rs.getString(5));
			usr.setDni(rs.getString(6));
			usr.setRoll(rs.getInt(7));
			usr.setDireccion(rs.getString(8));
			}
                        conn.close();
			return usr;
		}
	public ArrayList<Compra> getCompras(String Id) throws SQLException{

			ArrayList<Compra> compras = new ArrayList<Compra>();
			//1. Crear conexion
			Connection conn=DriverManager.getConnection(url,usuario,pass);
			//2. Crear objeto statement
			PreparedStatement st = conn.prepareStatement("SELECT * FROM compras WHERE usuario=?");
			st.setNString(1, Id);
			ResultSet rs=st.executeQuery();
			while(rs.next()) {
			Compra c=new Compra();
			c.setId(rs.getString(1));
			c.setUser(getUser(rs.getString(2)));
			c.setPrecio(rs.getFloat(3));
			c.setDescripcion(rs.getString(4));
                        c.setFecha(rs.getString(5));
			compras.add(c);
			}
                        conn.close();
			return compras;
		}
	public ArrayList<Pedido> getPedidos(String Id) throws SQLException{

		ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
		//1. Crear conexion
		Connection conn=DriverManager.getConnection(url,usuario,pass);
		//2. Crear objeto statement
		PreparedStatement st = conn.prepareStatement("SELECT * FROM pedidos WHERE usuario=?");
		st.setNString(1, Id);
		ResultSet rs=st.executeQuery();
		while(rs.next()) {
		Pedido p=new Pedido();
		p.setId(rs.getString(1));
		p.setUser(getUser(rs.getString(2)));
		p.setPrecio(rs.getFloat(3));
		p.setDescripcion(rs.getString(4));
		pedidos.add(p);
		}
                conn.close();
		return pedidos;
	}
	public Producto getProducto(String Id) throws SQLException{

		Producto p = new Producto();
			//1. Crear conexion
			Connection conn=DriverManager.getConnection(url,usuario,pass);
			//2. Crear objeto statement
			PreparedStatement st = conn.prepareStatement("SELECT * FROM productos WHERE id=?");
			st.setNString(1, Id);
			ResultSet rs=st.executeQuery();
			if(rs.next()) {
			p.setId(rs.getString(1));
			p.setNombre(rs.getString(2));
			p.setDescripcion(rs.getString(3));
			p.setPrecioc(rs.getFloat(4));
			p.setPreciop(rs.getFloat(5));

			}
                        conn.close();
			return p;
		}

    public void setUser(Usuario user) {
    }
	
	}


