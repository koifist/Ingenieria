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


public class ControladorCliente implements ActionListener{
    //Interfaz de cliente
    private ICliente interfaz_cliente;
    //Conector con la base de datos
    private Conector conector;
    //Usuario cliente
    private Usuario cliente;
    //Array de compras del cliente
    private ArrayList<Compra> compras;
    
    //Constructor
    ControladorCliente(ICliente i_cliente, Conector conector, Usuario cliente) throws SQLException {
        //Inicializamos las variables globales
        this.interfaz_cliente=i_cliente;
        this.conector=conector;
        this.cliente=cliente;
        //Actualizamos el array de compras y el JTable
        actualizarCompras();
        //Colocamos la interfaz en el centro de la pantalla
        interfaz_cliente.setLocationRelativeTo(null);
        //Hacemos la interfaz visible
        interfaz_cliente.setVisible(true);
        //Colocamos el nombre del cliente en la interfaz
        interfaz_cliente.idLabel.setText(cliente.getNombre());
        //Añadimos los elementos del interfaz al ActionListener
        this.interfaz_cliente.idpass.addActionListener(this);
        this.interfaz_cliente.realizarCompra.addActionListener(this);
        //Alineamos los elementos de la tabla al centro
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        interfaz_cliente.lista_compras.setDefaultRenderer(String.class, centerRenderer);
        //Creamos un SelectionModel para mostrar la descripcion de una compra seleccionada en la JTable
        ListSelectionModel modelo = interfaz_cliente.lista_compras.getSelectionModel();
        modelo.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                //Creamos un try/catch para evitar problemas al actualiar las compras
                try{
                    interfaz_cliente.descripcion.setText(compras.get(compras.size()
                            -interfaz_cliente.lista_compras.getSelectedRow()-1).getDescripcion());
                }catch(Exception compra){}
            }
        });
    }
    
    //Actualiza el array de compras y el JTable
    public void actualizarCompras() throws SQLException{
        //Creamos el TableModel para poder modificar el JTable
        DefaultTableModel modelo = (DefaultTableModel) interfaz_cliente.lista_compras.getModel();
        int n_filas =modelo.getRowCount()-1;
        //Borramos todas las filas
        for(int i=n_filas; i>=0; i--){
            modelo.removeRow(i);
        }
        //Actualizamos el array de compras
        compras=null;
        compras=conector.getCompras(cliente.getId());
        //Insertamos las compras en el JTable
        for(int i=compras.size()-1;i>=0;i--){
            modelo.addRow(new Object[]{compras.get(i).getId(), 
                compras.get(i).getFecha(), compras.get(i).getPrecio()+"€"});
        }
    }

    /*Metodo al que accedemos cuando activamos algun elemento 
      de la interfaz que se encuentra en el ActionListener*/
    @Override
    public void actionPerformed(ActionEvent e){
        //Objeto que referencia al elemento de la interfaz que hemos activado
        Object fuente= e.getSource();
        
        //Si activamos cambiar contraseña
        if(fuente==interfaz_cliente.idpass){
            ICambio interfaz_cambio=new ICambio();
            ControladorCambio controlador_cambio= new ControladorCambio
        (cliente,interfaz_cambio,conector);
        }
        
        //Si activamos realizar compra
        if(fuente==interfaz_cliente.realizarCompra){
            //Creamos una interfaz de compra
            ICompra interfaz_compra=new ICompra();
            try {
                /*Creamos un controlador de compra y le pasamos el cliente el conector la interfaz de compra 
                y la interfaz de cliente para que pueda cerrarla y abrirla y actualizar asi la lista de compras.*/
                ControladorCompra controlador_compra=new ControladorCompra
                    (cliente,conector,interfaz_compra,interfaz_cliente);
            } catch (SQLException ex) {
                Logger.getLogger(ControladorCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
}