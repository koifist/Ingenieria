package Modelo;

import java.sql.*;
import java.util.ArrayList;

public class Conector {
    private static String url = "jdbc:mysql://db4free.net:3306/insosupermercado";
    private static String usuario = "insosuper";
    private static String pass = "inso1234";
    private Connection conn;

    public Conector() throws SQLException{
        conn=DriverManager.getConnection(url,usuario,pass);
    }
			
    public Usuario getUser(String Id) throws SQLException{

            Usuario usr = new Usuario();
            //2. Crear objeto statement
            PreparedStatement st = conn.prepareStatement("SELECT * FROM usuarios "+
            "WHERE id=?");
            st.setNString(1, Id);
            ResultSet rs=st.executeQuery();
            if(rs.next()) {
                usr.setId(rs.getString(1));
                usr.setPass(rs.getString(2));
                usr.setNombre(rs.getString(3));
                usr.setDNI(rs.getString(4));
                usr.setRoll(rs.getInt(5));
                usr.setDireccion(rs.getString(6));
            }
            st.close();
            return usr;
    }
    public ArrayList<Usuario> getUsers(String Id) throws SQLException{

            ArrayList<Usuario> users = new ArrayList<Usuario>();
            //2. Crear objeto statement
            PreparedStatement st=null; 
            if(Id=="0") st= conn.prepareStatement("SELECT * FROM usuarios where roll>0");
            if(Id=="1") st=conn.prepareStatement("SELECT * FROM usuarios where roll=2");
            ResultSet rs=st.executeQuery();
            while(rs.next()) {
                Usuario u=new Usuario();
                u.setId(rs.getString(1));
                u.setPass(rs.getString(2));
                u.setNombre(rs.getString(3));
                u.setDNI(rs.getString(4));
                u.setRoll(rs.getInt(5));
                u.setDireccion(rs.getString(6));
                users.add(u);
            }
            st.close();
            return users;
    }
    public void setUser(Usuario user) throws SQLException{

            //2. Crear objeto statement
            PreparedStatement st = conn.prepareStatement("INSERT INTO "
            + "usuarios (id, pass, nombre, dni, roll, direccion) VALUES (?, ?, ?, ?, ?, ?)");
            st.setNString(1, user.getId());
            st.setNString(2, user.getPass());
            st.setNString(3, user.getNombre());
            st.setNString(4, user.getDNI());
            st.setInt(5, user.getRoll());
            st.setNString(6, user.getDireccion());
            st.executeUpdate();
            st.close();
    }
    public void updateUser(Usuario user) throws SQLException{

            //2. Crear objeto statement
            PreparedStatement st = conn.prepareStatement("UPDATE usuarios"+
            " SET pass = ? WHERE id = ?");
            st.setNString(1, user.getPass());
            st.setNString(2, user.getId());
            st.executeUpdate();
            st.close();
    }
    public ArrayList<Compra> getCompras(String Id) throws SQLException{

            ArrayList<Compra> compras = new ArrayList<Compra>();
            //2. Crear objeto statement
            PreparedStatement st = conn.prepareStatement("SELECT * FROM compras "+
            "WHERE usuario=?");
            st.setNString(1, Id);
            ResultSet rs=st.executeQuery();
            while(rs.next()) {
                Compra c=new Compra();
                c.setId(rs.getInt(1));
                c.setUser(getUser(rs.getString(2)));
                c.setPrecio(rs.getFloat(3));
                c.setFecha(rs.getString(4).substring(0, 10));
                c.setDescripcion(rs.getString(5));
                compras.add(c);
            }
            st.close();
            return compras;
    }
    public void setCompra(Compra compra) throws SQLException{

            //2. Crear objeto statement
            PreparedStatement st = conn.prepareStatement("INSERT INTO "
            + "compras (usuario, precio, descripcion) VALUES (?, ?, ?)");
            st.setNString(1, compra.getUser().getId());
            st.setFloat(2, compra.getPrecio());
            st.setNString(3, compra.getDescripcion());
            st.executeUpdate();
            st.close();
    }
    public ArrayList<Pedido> getPedidos(String Id) throws SQLException{

            ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
            //2. Crear objeto statement
            PreparedStatement st;
            if(Id=="1"){
                st = conn.prepareStatement("SELECT * FROM pedidos WHERE estado=1");
            }else{
                if(Id=="2"){
                st = conn.prepareStatement("SELECT * FROM pedidos");
                }else{
                    st = conn.prepareStatement("SELECT * FROM pedidos WHERE (usuario=?) AND (estado=0)");
                    st.setNString(1, Id);
                }
            }

            ResultSet rs=st.executeQuery();
            while(rs.next()) {
            Pedido p=new Pedido();
            p.setId(rs.getInt(1));
            p.setUser(getUser(rs.getString(2)));
            p.setPrecio(rs.getFloat(3));
            p.setDescripcion(rs.getString(4));
            p.setFecha(rs.getString(5));
            pedidos.add(p);
            }
            st.close();
            return pedidos;
    }
    public void setPedido(Pedido pedido) throws SQLException {

            //2. Crear objeto statement
            PreparedStatement st = conn.prepareStatement("INSERT INTO "
            + "pedidos (usuario, precio, descripcion) VALUES (?, ?, ?)");
            st.setNString(1, pedido.getUser().getId());
            st.setFloat(2, pedido.getPrecio());
            st.setNString(3, pedido.getDescripcion());
            st.executeUpdate();
            st.close();    
    }
    public void updatePedido(Pedido pedido) throws SQLException{

            //2. Crear objeto statement
            PreparedStatement st = conn.prepareStatement("UPDATE pedidos"+
            " SET estado = ? WHERE id = ?");
            st.setInt(1, pedido.getEstado());
            st.setInt(2, pedido.getId());
            st.executeUpdate();
            st.close();
    }
    public Producto getProducto (String nombre)throws SQLException{
            Producto p =new Producto();
            //2. Crear objeto statement
            PreparedStatement st = conn.prepareStatement("SELECT * FROM productos WHERE nombre=?");
            st.setNString(1,nombre);
            ResultSet rs=st.executeQuery();
            if(rs.next()) {
                p.setId(rs.getInt(1));
                p.setNombre(rs.getString(2));
                p.setDescripcion(rs.getString(3));
                p.setUser(getUser(rs.getString(4)));
                p.setPrecioc(rs.getFloat(5));
                p.setPreciop(rs.getFloat(6));
                p.setCantidad(rs.getInt(7));
                p.setEstado(rs.getInt(8));
            }
            st.close();
            return p;
    }
    public ArrayList<Producto> getProductos (String estado)throws SQLException{
            ArrayList<Producto> productos =new ArrayList<Producto>();
            //2. Crear objeto statement
            PreparedStatement st = null;
            if(estado=="0") st= conn.prepareStatement("SELECT * FROM productos WHERE estado=0");
            else{
                if(estado=="1") st= conn.prepareStatement("SELECT * FROM productos WHERE estado=1");
                else{st= conn.prepareStatement("SELECT * FROM productos WHERE usuario=?");
                    st.setNString(1,estado);
                }
            }
            ResultSet rs=st.executeQuery();
            while(rs.next()) {
                Producto p = new Producto();
                p.setId(rs.getInt(1));
                p.setNombre(rs.getString(2));
                p.setDescripcion(rs.getString(3));
                p.setUser(getUser(rs.getString(4)));
                p.setPrecioc(rs.getFloat(5));
                p.setPreciop(rs.getFloat(6));
                p.setCantidad(rs.getInt(7));
                p.setEstado(rs.getInt(8));
                productos.add(p);
            }
            st.close();
            return productos;
    }
    public void updateProductoc(Producto producto) throws SQLException{
            //2. Crear objeto statement
            PreparedStatement st = conn.prepareStatement("UPDATE productos"+
            " SET cantidad = ? WHERE id = ?");
            st.setInt(1, producto.getCantidad());
            st.setInt(2, producto.getId());
            st.executeUpdate();
            st.close();
    }
    public void updateProductoe(Producto producto) throws SQLException{
            //2. Crear objeto statement
            PreparedStatement st = conn.prepareStatement("UPDATE productos"+
            " SET estado = ? WHERE id = ?");
            st.setInt(1, producto.getEstado());
            st.setInt(2, producto.getId());
            st.executeUpdate();
            st.close();
    }
}