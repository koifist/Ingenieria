package Modelo;

import java.sql.*;
import java.util.ArrayList;

public class Conector {
    private static String url = "jdbc:mysql://db4free.net:3306/insosupermercado";
    private static String user = "insosuper";
    private static String password = "inso1234";
    private Connection connection;

    public Conector() throws SQLException{
        connection=DriverManager.getConnection(url,user,password);
    }
    
    //Devuelve de la base de datos un usuario pasandole su Id
    public Usuario getUser(String Id_user) throws SQLException{

            Usuario usuario = new Usuario();
            //2. Crear objeto statement
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM usuarios "+
            "WHERE id=?");
            statement.setNString(1, Id_user);
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()) {
                usuario.setId(resultSet.getString(1));
                usuario.setPass(resultSet.getString(2));
                usuario.setNombre(resultSet.getString(3));
                usuario.setDNI(resultSet.getString(4));
                usuario.setRoll(resultSet.getInt(5));
                usuario.setDireccion(resultSet.getString(6));
            }
            statement.close();
            return usuario;
    }
    
    //Devuelve un array de usuarios que son proveedores
    public ArrayList<Usuario> getProveedores() throws SQLException{

            ArrayList<Usuario> proveedores = new ArrayList<Usuario>();
            //2. Crear objeto statement
            PreparedStatement statement=connection.prepareStatement("SELECT * FROM usuarios where roll=2");
            ResultSet resultset=statement.executeQuery();
            while(resultset.next()) {
                Usuario usuario=new Usuario();
                usuario.setId(resultset.getString(1));
                usuario.setPass(resultset.getString(2));
                usuario.setNombre(resultset.getString(3));
                usuario.setDNI(resultset.getString(4));
                usuario.setRoll(resultset.getInt(5));
                usuario.setDireccion(resultset.getString(6));
                proveedores.add(usuario);
            }
            statement.close();
            return proveedores;
    }
    
    //Crea un nuevo usuario
    public void setUser(Usuario usuario) throws SQLException{

            //2. Crear objeto statement
            PreparedStatement statement = connection.prepareStatement("INSERT INTO "
            + "usuarios (id, pass, nombre, dni, roll, direccion) VALUES (?, ?, ?, ?, ?, ?)");
            statement.setNString(1, usuario.getId());
            statement.setNString(2, usuario.getPass());
            statement.setNString(3, usuario.getNombre());
            statement.setNString(4, usuario.getDNI());
            statement.setInt(5, usuario.getRoll());
            statement.setNString(6, usuario.getDireccion());
            statement.executeUpdate();
            statement.close();
    }
    
    //Actualiza la contraseña de un usuario.
    public void updateUser(Usuario usuario) throws SQLException{

            //2. Crear objeto statement
            PreparedStatement statement = connection.prepareStatement("UPDATE usuarios"+
            " SET pass = ? WHERE id = ?");
            statement.setNString(1, usuario.getPass());
            statement.setNString(2, usuario.getId());
            statement.executeUpdate();
            statement.close();
    }
    
    //Devuelve un array de compras pasandole el Id del cliente
    public ArrayList<Compra> getCompras(String id_cliente) throws SQLException{

            ArrayList<Compra> compras = new ArrayList<Compra>();
            //2. Crear objeto statement
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM compras "+
            "WHERE usuario=?");
            statement.setNString(1, id_cliente);
            ResultSet resultSet=statement.executeQuery();
            while(resultSet.next()) {
                Compra compra=new Compra();
                compra.setId(resultSet.getInt(1));
                compra.setUser(getUser(resultSet.getString(2)));
                compra.setPrecio(resultSet.getFloat(3));
                compra.setFecha(resultSet.getString(4).substring(0, 10));
                compra.setDescripcion(resultSet.getString(5));
                compras.add(compra);
            }
            statement.close();
            return compras;
    }
    
    //Crea una nueva compra
    public void setCompra(Compra compra) throws SQLException{

            //2. Crear objeto statement
            PreparedStatement statement = connection.prepareStatement("INSERT INTO "
            + "compras (usuario, precio, descripcion) VALUES (?, ?, ?)");
            statement.setNString(1, compra.getUser().getId());
            statement.setFloat(2, compra.getPrecio());
            statement.setNString(3, compra.getDescripcion());
            statement.executeUpdate();
            statement.close();
    }
    
    //Devuelve un array de pedidos pasando el estado del pedido
    public ArrayList<Pedido> getPedidos(String estado) throws SQLException{

            ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
            //2. Crear objeto statement
            PreparedStatement statement;
            if(estado=="1"){
                statement = connection.prepareStatement("SELECT * FROM pedidos WHERE estado=1");
            }else{
                if(estado=="2"){
                statement = connection.prepareStatement("SELECT * FROM pedidos");
                }else{
                    statement = connection.prepareStatement("SELECT * FROM pedidos WHERE (usuario=?) AND (estado=0)");
                    statement.setNString(1, estado);
                }
            }
            ResultSet resultSet=statement.executeQuery();
            while(resultSet.next()) {
            Pedido pedido=new Pedido();
            pedido.setId(resultSet.getInt(1));
            pedido.setUser(getUser(resultSet.getString(2)));
            pedido.setPrecio(resultSet.getFloat(3));
            pedido.setDescripcion(resultSet.getString(4));
            pedido.setFecha(resultSet.getString(5));
            pedidos.add(pedido);
            }
            statement.close();
            return pedidos;
    }
    
    //Devuelve un pedido pasandole su Id
    public Pedido getPedido(int id_pedido) throws SQLException{
        
            //2. Crear objeto statement
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM pedidos WHERE id=?");
            statement.setInt(1, id_pedido);
            ResultSet resultSet=statement.executeQuery();
            Pedido pedido=new Pedido();
            if(resultSet.next()) {
            pedido.setId(resultSet.getInt(1));
            pedido.setUser(getUser(resultSet.getString(2)));
            pedido.setPrecio(resultSet.getFloat(3));
            pedido.setDescripcion(resultSet.getString(4));
            pedido.setFecha(resultSet.getString(5));
            }
            statement.close();
            return pedido;
    }
    
    //Crea un pedido
    public void setPedido(Pedido pedido) throws SQLException {

            //2. Crear objeto statement
            PreparedStatement statement = connection.prepareStatement("INSERT INTO "
            + "pedidos (usuario, precio, descripcion) VALUES (?, ?, ?)");
            statement.setNString(1, pedido.getUser().getId());
            statement.setFloat(2, pedido.getPrecio());
            statement.setNString(3, pedido.getDescripcion());
            statement.executeUpdate();
            statement.close();    
    }
    
    //Actualiza el estado de un pedido
    public void updatePedido(Pedido pedido) throws SQLException{

            //2. Crear objeto statement
            PreparedStatement statement = connection.prepareStatement("UPDATE pedidos"+
            " SET estado = ? WHERE id = ?");
            statement.setInt(1, pedido.getEstado());
            statement.setInt(2, pedido.getId());
            statement.executeUpdate();
            statement.close();
    }
    
    //Devuelve un producto pasandole su nombre
    public Producto getProducto (String nombre)throws SQLException{
        
            Producto producto =new Producto();
            //2. Crear objeto statement
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM productos WHERE nombre=?");
            statement.setNString(1,nombre);
            ResultSet resultSet=statement.executeQuery();
            if(resultSet.next()) {
                producto.setId(resultSet.getInt(1));
                producto.setNombre(resultSet.getString(2));
                producto.setDescripcion(resultSet.getString(3));
                producto.setUser(getUser(resultSet.getString(4)));
                producto.setPrecioc(resultSet.getFloat(5));
                producto.setPreciop(resultSet.getFloat(6));
                producto.setCantidad(resultSet.getInt(7));
                producto.setEstado(resultSet.getInt(8));
            }
            statement.close();
            return producto;
    }
    
    //Devuelve un array de productos pasandole el estado o si le pasas el id del proveedor todos sus productos que no esten borrados.
    public ArrayList<Producto> getProductos (String estado)throws SQLException{
        
            ArrayList<Producto> productos =new ArrayList<Producto>();
            //2. Crear objeto statement
            PreparedStatement statement = null;
            if(estado=="0") statement= connection.prepareStatement("SELECT * FROM productos");
            else{
                if(estado=="1") statement= connection.prepareStatement("SELECT * FROM productos WHERE estado>0");
                else{statement= connection.prepareStatement("SELECT * FROM productos WHERE (usuario=?) AND (estado<2)");
                    statement.setNString(1,estado);
                }
            }
            ResultSet resultSet=statement.executeQuery();
            while(resultSet.next()) {
                Producto producto = new Producto();
                producto.setId(resultSet.getInt(1));
                producto.setNombre(resultSet.getString(2));
                producto.setDescripcion(resultSet.getString(3));
                producto.setUser(getUser(resultSet.getString(4)));
                producto.setPrecioc(resultSet.getFloat(5));
                producto.setPreciop(resultSet.getFloat(6));
                producto.setCantidad(resultSet.getInt(7));
                producto.setEstado(resultSet.getInt(8));
                if(producto.getEstado()==2 && producto.getCantidad()==0){
                    statement= connection.prepareStatement("DELETE from productos WHERE id = ?");
                    statement.executeUpdate();
                }else{
                    productos.add(producto);
                }
            }
            statement.close();
            return productos;
    }
    
    //Crea un producto
    public void setProducto(Producto producto) throws SQLException {

            //2. Crear objeto statement
            PreparedStatement statement = connection.prepareStatement("INSERT INTO "
            + "productos (nombre, descripcion, usuario, preciop) VALUES (?, ?, ?, ?)");
            statement.setNString(1, producto.getNombre());
            statement.setNString(2, producto.getDescripcion());
            statement.setNString(3, producto.getUser().getId());
            statement.setFloat(4, producto.getPrecio(2));
            statement.executeUpdate();
            statement.close();    
    }
    
    //Añade productos al stock
    public void updateProducto_cantidad(Producto producto) throws SQLException{
        
            //2. Crear objeto statement
            PreparedStatement statement = connection.prepareStatement("UPDATE productos"+
            " SET cantidad = ? WHERE id = ?");
            statement.setInt(1, producto.getCantidad());
            statement.setInt(2, producto.getId());
            statement.executeUpdate();
            statement.close();
        
    }
    
    //Cambia el estado de un producto
    public void updateProducto_estado(Producto producto) throws SQLException{
        
            if(producto.getEstado()==2 && producto.getCantidad()==0){
                PreparedStatement statement = connection.prepareStatement("DELETE from productos"+
                " WHERE id = ?");
                statement.setInt(1, producto.getId());
                statement.executeUpdate();
                statement.close();
            }else{
                //2. Crear objeto statement
                PreparedStatement statement = connection.prepareStatement("UPDATE productos"+
                " SET estado = ? ,precioc = ? WHERE id = ?");
                statement.setInt(1, producto.getEstado());
                statement.setFloat(2, producto.getPrecio(3));
                statement.setInt(3, producto.getId());
                statement.executeUpdate();
                statement.close();
            }
    }
}