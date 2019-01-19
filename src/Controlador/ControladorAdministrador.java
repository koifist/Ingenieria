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

public class ControladorAdministrador implements ActionListener{
    //Interfaz del administrador
    private IAdministrador interfaz_administrador;
    //Conector con la base de datos
    private Conector conector;
    //Array de productos
    private ArrayList<Producto> productos;
    
    //Constructor
    ControladorAdministrador(IAdministrador i_administrador, Conector conector) throws SQLException {
        //Inicializamos las variables globales
        this.interfaz_administrador=i_administrador;
        this.conector=conector;
        //Colocamos la interfaz en el centro de la pantalla
        interfaz_administrador.setLocationRelativeTo(null);
        //Hacemos la interfaz visible
        interfaz_administrador.setVisible(true);
        //Actualizamos el array de productos y la JTable
        actualizar();
        //Alineamos los elementos de las JTables al centro
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        interfaz_administrador.list_prod.setDefaultRenderer(String.class, centerRenderer);
        interfaz_administrador.list_prod.setDefaultRenderer(int.class, centerRenderer);
        //Añadimos elementos de la interfaz al ActionListener
        interfaz_administrador.nuevo_empleado.addActionListener(this);
        interfaz_administrador.nuevo_proveedor.addActionListener(this);
        interfaz_administrador.aceptar_producto.addActionListener(this);
        interfaz_administrador.modificar_precio.addActionListener(this);
        interfaz_administrador.borrar_producto.addActionListener(this);
        //Creamos un SelectionModel para mostrar el precio de los productos
        ListSelectionModel modelo = interfaz_administrador.list_prod.getSelectionModel();
        modelo.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                try{
                    interfaz_administrador.prize_cli.setText("");
                    interfaz_administrador.prize_prov.setText(Float.toString(productos.get(interfaz_administrador.list_prod.getSelectedRow()).getPrecio(0)));
                    if(productos.get(interfaz_administrador.list_prod.getSelectedRow()).getPrecio(3)>0){
                        interfaz_administrador.prize_cli.setText(Float.toString(productos.get(interfaz_administrador.list_prod.getSelectedRow()).getPrecio(3)));   
                    }
                }catch(Exception ee){}
            }
        });
    }
    
    //Actualiza el array de productos y la Jtable
    private void actualizar() throws SQLException {
        //Creamos el TableModel para modificar los elementos del JTable
        DefaultTableModel modelo = (DefaultTableModel) interfaz_administrador.list_prod.getModel();
        //Borramos las filas de la JTable
        int n_filas =modelo.getRowCount()-1;
        for(int i=n_filas; i>=0; i--){
        modelo.removeRow(i);
        }
        //Actualizamos el array de productos
        productos=null;
        productos=conector.getProductos("0");
        //Rellenamos la JTable con los productos
        for(int i=0;i<productos.size();i++){
            String aceptado="";
            if(productos.get(i).getEstado() == 0) aceptado="Pendiente";
            if(productos.get(i).getEstado() == 1) aceptado="Aceptado";
            if(productos.get(i).getEstado() == 2) aceptado="Borrado";
        modelo.addRow(new Object[]{productos.get(i).getNombre(),productos.get(i).getUsuario().getNombre(),aceptado,String.valueOf(productos.get(i).getCantidad())});
        }    
    }

    /*Metodo al que accedemos cuando activamos algun elemento 
      de la interfaz que se encuentra en el ActionListener*/
    @Override
    public void actionPerformed(ActionEvent e) {
        //Objeto que referencia al elemento de la interfaz que hemos activado
        Object fuente=e.getSource();
        
        //Si activamos nuevo empleado le pasamos un 1 para que sepa que tiene que registrar un empleado
        if(fuente==interfaz_administrador.nuevo_empleado){
            IRegistro interfaz_registro=new IRegistro();
            ControladorRegistro controlador_registro_empleado=new ControladorRegistro(interfaz_registro,conector,1);
        }
        
        //Si activamos nuevo proveedor le pasamos un 2 para que sepa que tiene que registrar un proveedor
        if(fuente==interfaz_administrador.nuevo_proveedor){
            IRegistro interfaz_registro=new IRegistro();
            ControladorRegistro controlador_registro_proveedor=new ControladorRegistro(interfaz_registro,conector,2);
        }
        
        //Si activamos aceptar producto
        if(fuente==interfaz_administrador.aceptar_producto){
            //Obtenemos un producto del array en funcion de la fila del JTable seleccionada
            Producto producto=new Producto();
            producto=productos.get(interfaz_administrador.list_prod.getSelectedRow());
            //Si el producto esta borrado
            if(producto.getEstado()==2){
            JOptionPane.showMessageDialog(null,"El producto ya no esta disponible.");
            }
            
            //Si el producto esta por confirmar
            if(producto.getEstado()==0){
                //Utilizamos el try/catch para comprobar si se ha introducido correctamente el precio
                try {
                    float prize=Float.parseFloat(interfaz_administrador.prize_cli.getText());
                    //Si el precio es mayor que cero
                    if(prize>0){
                        //Si el precio de venta al cliente es menor que el de compra al proveedor informamos
                        if(prize<producto.getPrecio(0)){
                            JOptionPane.showMessageDialog(null,"El precio de venta es menor que el precio de compra.");
                        }
                        //Redondeamos por defecto a 2 decimales
                        prize=prize*100;
                        prize=(int) prize;
                        prize=prize/100;
                        //Actualizamos el estado del producto y su precio de venta
                        producto.setPrecio_cliente(prize);
                        producto.setEstado(1);
                        conector.updateProducto_estado(producto);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorAdministrador.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null,"El precio introducido no es válido.");
                }
            }
            
            //Actualizamos el array de productos y el JTable
            try {
                actualizar();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorAdministrador.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Borramos el precio de la interfaz
            interfaz_administrador.prize_cli.setText("");
        }
        
        //Si activamos modificar precio
        if(fuente==interfaz_administrador.modificar_precio){
            //Obtenemos un producto del array en funcion de la fila del JTable seleccionada
            Producto producto=new Producto();
            producto=productos.get(interfaz_administrador.list_prod.getSelectedRow());
            //Si el producto esta borrado informamos que ya no esta disponible
            if(producto.getEstado()==2){
                JOptionPane.showMessageDialog(null,"El producto que quiere modificar ha sido borrado.");
            }
            //Utilziamos el try/catch para comprobar si el precio introducido es valido
            try {
                float prize=Float.parseFloat(interfaz_administrador.prize_cli.getText());
                //si el precio es mayor que 0
                    if(prize>0){
                        //Si el precio de venta al cliente es menor que el de compra al proveedor informamos
                        if(prize<producto.getPrecio(0)){
                            JOptionPane.showMessageDialog(null,"El precio de venta es menor que el precio de compra.");
                        }
                    //Redondeamos por defecto a 2 decimales
                    prize=prize*100;
                    prize=(int) prize;
                    prize=prize/100;
                    //Actualizamos el precio de venta del producto
                    producto.setPrecio_cliente(prize);
                    conector.updateProducto_estado(producto);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorAdministrador.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null,"El precio introducido no es válido.");
                }
            //Actualizamos el array de productos y el JTable
            try {
                actualizar();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorAdministrador.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Borramos el precio de la interfaz-
            interfaz_administrador.prize_cli.setText("");
        }
    
        //Si activamos borrar producto
        if(fuente==interfaz_administrador.borrar_producto){
            //Obtenemos el producto del array en funcion de la fila del JTable seleccionada    
            Producto producto=new Producto();
            producto=productos.get(interfaz_administrador.list_prod.getSelectedRow());
            if(producto.getEstado()==2){
                JOptionPane.showMessageDialog(null,"El producto ya esta borrado.");
            }
            //Cambiamos el estado a borrado
            producto.setEstado(2);
            try {
                //Actualizamos el producto y el array junto con la JTable de la interfaz
                conector.updateProducto_estado(producto);
                actualizar();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorAdministrador.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Borramos el precio de la interfaz
            interfaz_administrador.prize_cli.setText("");
        }
    }
}