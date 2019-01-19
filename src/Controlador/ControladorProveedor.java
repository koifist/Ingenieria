package Controlador;

import Modelo.*;
import Vista.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

public class ControladorProveedor implements ActionListener{
    //Interfaz de proveedor
    private IProveedor interfaz_proveedor;
    //Conector a la base de datos
    private Conector conector;
    //Objeto usuario del proveedor
    private Usuario proveedor;
    //Array de los pedidos que le han realizado al proveedor
    private ArrayList<Pedido> array_pedidos;
    //Array de los productos del proveedor que no se han borrado
    private ArrayList<Producto> array_productos;
    //Variable que simboliza si todos los campos del producto a añadir estan correctos
    private boolean add;
    
    //Constructor
    ControladorProveedor(IProveedor i_proveedor, Conector conector, Usuario user) throws SQLException {
        //Inicializamos las variables globales
        this.interfaz_proveedor=i_proveedor;
        this.conector=conector;
        this.proveedor=user;
        //Actualizamos tanto los arrays de pedidos y productos como los JTable
        actualizar();
        //Alineamos las casillas de los JTable en el centro
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        interfaz_proveedor.lista_pedidos.setDefaultRenderer(String.class, centerRenderer);
        interfaz_proveedor.lista_productos.setDefaultRenderer(String.class, centerRenderer);
        //Ponemos el nombre del usuario en la interfaz
        interfaz_proveedor.id_label.setText(user.getNombre());
        //Colocamos la interfaz en el centro
        interfaz_proveedor.setLocationRelativeTo(null);
        //Mostramos la interfaz
        interfaz_proveedor.setVisible(true);
        //Añadimos los botones al ActionListener
        interfaz_proveedor.actualizar.addActionListener(this);
        interfaz_proveedor.anadir.addActionListener(this);
        interfaz_proveedor.borrar.addActionListener(this);
        interfaz_proveedor.enviar_pedido.addActionListener(this);
        interfaz_proveedor.idpass.addActionListener(this);
        /*Creamos un ListSelectionModel que nos permite mostrar la descripcion de un pedido
        solo con clicar en su fila del JTable*/
        ListSelectionModel modelo = interfaz_proveedor.lista_pedidos.getSelectionModel();
        modelo.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                //Creamos un try para evitar problemas al enviar pedidos
                try{
                    //Mostramos en un cuadro de texto el contenido del pedido
                    interfaz_proveedor.descripcion_pedido.setText(array_pedidos.get(
                            interfaz_proveedor.lista_pedidos.getSelectedRow()).getDescripcion());        
                }catch(Exception descripcion){
                }
            }
        });
    }
    
    //Actualizamos los arrays y los JTable
    public void actualizar() throws SQLException{
        //Creamos el TableModel para interactuar con el JTable
        DefaultTableModel modelo = (DefaultTableModel) interfaz_proveedor.lista_pedidos.getModel();
        //Borramos todas las filas
        int n_filas =modelo.getRowCount()-1;
        for(int i=n_filas; i>=0; i--){
            modelo.removeRow(i);
        }
        //Actualizamos el array de pedidos
        array_pedidos=null;
        array_pedidos=conector.getPedidos(proveedor.getId());
        //Añadimos los pedidos a su JTable
        for(int i=0;i<array_pedidos.size();i++){
            modelo.addRow(new Object[]{array_pedidos.get(i).getId(), 
                array_pedidos.get(i).getPrecio()+"€", (array_pedidos.get(i).getFecha()).substring(0,10)});
        }
        //Cambios el modelo al JTable de productos
        modelo = (DefaultTableModel) interfaz_proveedor.lista_productos.getModel();
        //Borramos todas las filas
        n_filas =modelo.getRowCount()-1;
        for(int i=n_filas; i>=0; i--){
            modelo.removeRow(i);
        }
        //Actualizamos el array de productos solo los que no esten borrados.
        array_productos=null;
        array_productos=conector.getProductos(proveedor.getId());
        //Añadimos los productos al JTable
        for(int i=0;i<array_productos.size();i++){
            //Variable que indica el estado del producto
            String aceptado="";
            if(array_productos.get(i).getEstado() == 1) aceptado="Aceptado";
            else{aceptado="Pendiente";}
            modelo.addRow(new Object[]{array_productos.get(i).getNombre(), 
                array_productos.get(i).getPrecio(proveedor.getRoll())+"€",aceptado});
        }
    }
    
    /*Metodo al que accedemos cuando activamos algun elemento 
      de la interfaz que se encuentra en el ActionListener*/
    @Override
    public void actionPerformed(ActionEvent e) {
        //Objeto que recoje el elemento del interfaz que hemos activado
        Object fuente=e.getSource();
        //Si activamos cambiar contraseña
        if(fuente==interfaz_proveedor.idpass){
            //Creamos Interfaz de cambiar contraseña
            ICambio interfaz_cambio=new ICambio();
            //Creamos controlador de cambiar contraseña y le pasamos usuario, interfaz y conector.
            ControladorCambio controlador_cambio=new ControladorCambio(proveedor,interfaz_cambio,conector);
        }
        
        //Si activamos actualizar
        if(fuente==interfaz_proveedor.actualizar){
            try {
                actualizar();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorProveedor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Si activamos añadir producto
        if(fuente==interfaz_proveedor.anadir){
            //Inicializamos en true la variable
            add=true;
            //Si no se ha introducido un nombre
            if(interfaz_proveedor.nombre.getText().isEmpty()){
              JOptionPane.showMessageDialog(null,"Introduzca un nombre para su producto.");
              add=false;
            }
            //Si no se ha introducido un precio
            if(interfaz_proveedor.precio.getText().isEmpty()){
              JOptionPane.showMessageDialog(null,"Introduzca un precio para su producto.");
              add=false;
            } 
            if(add==true){
                //Inicializamos el objeto tipo producto
                Producto producto=new Producto();
                //Comprobamos si ya existe
                try {
                    //Le ponemos nombre al producto que es el nombre del proveedor seguido de-"Nombre del proveedor"
                    producto=conector.getProducto(interfaz_proveedor.nombre.getText()+"-"+proveedor.getNombre());
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorProveedor.class.getName()).log(Level.SEVERE, null, ex);
                }
                //Si el producto ya existe
                if(producto.getNombre()!=null){
                    JOptionPane.showMessageDialog(null,"El producto ya ha sido añadido.");
                    add=false;
                }
                //Si el producto no existe procedemos a crearlo
                if(add==true){
                    //Utilizamos el try para comprobar si el precio introducido es valido
                    try {
                        //Convertimos el strign a float
                        float prize = Float.parseFloat(interfaz_proveedor.precio.getText());
                        //Le damos valores al producto
                        producto.setNombre(interfaz_proveedor.nombre.getText()+"-"+proveedor.getNombre());
                        producto.setUsuario(proveedor);
                        producto.setDescripcion(interfaz_proveedor.descripcion.getText());
                        //Redondeamos por defecto a 2 decimales
                        prize=prize*100;
                        prize=(int) prize;
                        prize=prize/100;
                        //Si el precio introducido es 0
                        if(prize==0){
                            JOptionPane.showMessageDialog(null,"El precio que ha introducido es 0.");
                        //Si no es 0
                        }else{
                            producto.setPrecio_proveedor(prize);
                            //Creamos el producto
                            conector.createProducto(producto);
                            //actualizamos los datos de la interfaz y los arrays
                            actualizar();
                            //Vaciamos los campos de crear producto
                            interfaz_proveedor.nombre.setText("");
                            interfaz_proveedor.descripcion.setText("");     
                            interfaz_proveedor.precio.setText("");
                        }
                    //Si no se ha podido introducir ese precio salta un mensaje de alerta
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null,"Introduzca un precio valido.");
                    }
                }
            }
        }
        
        //Si activamos borrar producto
        if(fuente==interfaz_proveedor.borrar){
            try {
                //Obtenemos el producto del array en funcion de la fila del Jtable que tengamos seleccionada
                Producto producto=new Producto();               
                producto=conector.getProducto((String) interfaz_proveedor.lista_productos.getValueAt(
                        interfaz_proveedor.lista_productos.getSelectedRow(), 0));
                //Cambiamos el estado para dejarlo borrado el producto
                producto.setEstado(2);
                //Actualizamos el estado del producto
                conector.updateProducto_estado(producto);
                //Actualizamos los arrays y los JTable
                actualizar();
            //Si no se puede lanzamos mensaje de error
            } catch (Exception delete) {
                JOptionPane.showMessageDialog(null,"Error al borrar el producto.");
            }
        }
        
        //Si activamos enviar pedido
        if(fuente==interfaz_proveedor.enviar_pedido){
            //Obtenemos el pedido del array en funcion de la fila seleccionada del JTable
            Pedido pedido=new Pedido();
            try {
                pedido=conector.getPedido((int) interfaz_proveedor.lista_pedidos.getValueAt(
                        interfaz_proveedor.lista_pedidos.getSelectedRow(), 0));
                //Cambiamos el estado del pedido de 0"Pendiente de envio" a 1"De camino"
                pedido.setEstado(1);
                //Actualizamos el pedido
                conector.updatePedido(pedido);
                //Actualizamos los arrays y los JTable
                actualizar();
            //Mensaje de error si no se pudo enviar el pedido
            } catch (Exception envio) {
                JOptionPane.showMessageDialog(null,"Error al enviar el pedido.");
            }
        }
    }
}