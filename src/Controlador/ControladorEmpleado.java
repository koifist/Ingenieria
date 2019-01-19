package Controlador;

import Modelo.*;
import Vista.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.*;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.event.*;
import javax.swing.table.*;

public class ControladorEmpleado implements ActionListener{
    //Interfaz de empleado
    private IEmpleado interfaz_empleado;
    //Conector con la base de datos
    private Conector conector;
    //Empleado que accede a la interfaz
    private Usuario empleado;
    //Array de pedidos que estan de camino
    private ArrayList<Pedido> pedidos;
    //Array de todos los proveedores del sistema
    private ArrayList<Usuario> proveedores;
    
    //Constructor
    ControladorEmpleado(IEmpleado i_empleado, Conector conector, Usuario usuario) throws SQLException {
        //Inicializamos variables globales
        this.interfaz_empleado=i_empleado;
        this.conector=conector;
        this.empleado=usuario;
        //Actualizamos el array de pedido y el JTable
        actualizar_pedidos();
        //Actualizamos la lista de proveedores
        actualizar_proveedores();
        //Alineamos los elementos del JTable al centro
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        interfaz_empleado.lista_pedidos.setDefaultRenderer(String.class, centerRenderer);
        //Ponemos el nombre del trabajador en la interfaz
        interfaz_empleado.id_label.setText(empleado.getNombre());
        //Ponemos la interfaz en el centro de la pantalla
        interfaz_empleado.setLocationRelativeTo(null);
        //Hacemos la interfaz visible
        interfaz_empleado.setVisible(true);
        //Añadimos los elementos del interfaz al ActionListener
        interfaz_empleado.confirmar_pedido.addActionListener(this);
        interfaz_empleado.realizar_pedido.addActionListener(this);
        interfaz_empleado.idpass.addActionListener(this);
        interfaz_empleado.actualizar.addActionListener(this);
        //Creamos un selection model para que cuando seleccionemos un pedido muestre su contenido por pantalla
        ListSelectionModel modelo = interfaz_empleado.lista_pedidos.getSelectionModel();
        modelo.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                //Hacemos un try para evitar conflicos al confirmar la recepcion de algun pedido
                try{
                    interfaz_empleado.descripcion.setText(pedidos.get(
                            interfaz_empleado.lista_pedidos.getSelectedRow()).getDescripcion());        
                }catch(Exception ee){}
            }
        });
    }
    
    //Devuelve un Usuario proveedor pasandole su nombre
    public Usuario get_proveedor(String nombre){
        for(int i=0;i<proveedores.size();i++){
            if(proveedores.get(i).getNombre().equals(nombre)) return proveedores.get(i);
        }
        return null;
    }
    
    //Añade todos los proveedores al case para realizar pedidos
    public void actualizar_proveedores() throws SQLException{
        proveedores=conector.getProveedores();
        for(int i=0;i<proveedores.size();i++){
            interfaz_empleado.case_prov.addItem(proveedores.get(i).getNombre());
        }
        interfaz_empleado.case_prov.setSelectedItem(null);
    }
    
    //Actualiza los pedidos
    public void actualizar_pedidos() throws SQLException{
        //Creamos el TableModel
        DefaultTableModel modelo = (DefaultTableModel) interfaz_empleado.lista_pedidos.getModel();
        int n_filas =modelo.getRowCount()-1;
        //Borramos las filas del JTable
        for(int i=n_filas; i>=0; i--){
        modelo.removeRow(i);
        }
        pedidos=null;
        //Obtenemos los pedidos que esten enviados y de camino
        pedidos=conector.getPedidos("1");
        //Añadimos los pedidos al JTable
        for(int i=0;i<pedidos.size();i++){
        modelo.addRow(new Object[]{pedidos.get(i).getId(), pedidos.get(i).getUsuario().getNombre(),
            pedidos.get(i).getPrecio()+"€", (pedidos.get(i).getFecha()).substring(0,10)});
        }
    }
    
    /*Metodo al que accedemos cuando activamos algun elemento 
      de la interfaz que se encuentra en el ActionListener*/
    @Override
    public void actionPerformed(ActionEvent e) {
        //Objeto que referencia al elemento del interfaz activado
        Object fuente=e.getSource();
        
        //Si activamos actualizar, actualiza la lista de pedidos
        if(fuente==interfaz_empleado.actualizar){
            try {
                interfaz_empleado.descripcion.setText("");
                actualizar_pedidos();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Si activamos realizar pedido y hemos seleccionado un proveedor del case
        if(fuente==interfaz_empleado.realizar_pedido && interfaz_empleado.case_prov.getSelectedItem()!=null){
            try {
                //Creamos el interfaz de compra
                ICompra interfaz_compra=new ICompra();
                //Creamos el controlador de pedido 
                ControladorPedido controlador_pedido=new ControladorPedido(empleado,conector,interfaz_compra,
                    get_proveedor((String)interfaz_empleado.case_prov.getSelectedItem()));
            } catch (SQLException ex) {
                Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Si activamos cambiar contraseña
        if(fuente==interfaz_empleado.idpass){
            ICambio interfaz_cambio=new ICambio();
            ControladorCambio controlador_cambio=new ControladorCambio(empleado,interfaz_cambio,conector);
        }
        
        //si activamos confirmar pedido
        if(fuente==interfaz_empleado.confirmar_pedido){
            //Borra el texto de la descripcion
            interfaz_empleado.descripcion.setText("");
            //obtiene del array el pedido de la fila seleccionada del JTable
            Pedido p=null;
            p=pedidos.get(interfaz_empleado.lista_pedidos.getSelectedRow());
            //Cambiamos el estado a recibido
            p.setEstado(2);
            //Obtenemos la descripcion del pedido
            String descPedido=p.getDescripcion();
            //Separamos la descripcion en Strings con el formato [cantidad   nombre_producto]
            String []add=descPedido.split("\n");
            for(int i=0;i<add.length;i++){
                //Separamos cada elemento en [cantidad][nombre_producto]
                String []canti=add[i].split("   ");
                //Obtenemos el producto de la base de datos en funcion de su nombre
                Producto prod=new Producto();
                try {
                    prod=conector.getProducto(canti[1]);
                    //Añadimos al stock la cantidad recibida
                    prod.setCantidad(prod.getCantidad()+Integer.parseInt(canti[0]));
                    //Actualizamos la cantidad
                    conector.updateProducto_cantidad(prod);
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
                }  
            }
            try {
                conector.updatePedido(p);
                actualizar_pedidos();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
}
