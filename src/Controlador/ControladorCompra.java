package Controlador;

import Modelo.*;
import Vista.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.table.*;

public class ControladorCompra implements ActionListener{
    //interfaz compra
    private ICompra interfaz_compra;
    //Conector con la base de datos
    private Conector conector;
    //Usuario tipo cliente
    private Usuario cliente;
    //Interfaz del cliente que ha entrado a comprar
    private ICliente interfaz_cliente;
    //Precio final de la compra
    private float precio_final=0;
    //Array de productos que puede el cliente comprar
    ArrayList<Producto> productos;
    
    public ControladorCompra(Usuario cliente, Conector conector, ICompra i_compra,ICliente interfaz_cliente) throws SQLException{
        //Inicializamos las variables globales
        this.cliente=cliente;
        this.conector=conector;
        this.interfaz_compra=i_compra;
        this.interfaz_cliente=interfaz_cliente;
        //Ponemos el nombre del cliente en la interfaz
        interfaz_compra.id_label.setText(cliente.getNombre());
        //inicializamos los productos
        actualizar_productos();
        //Colocamos la interfaz en el centro
        interfaz_compra.setLocationRelativeTo(null);
        //Hacemos la interfaz visible
        interfaz_compra.setVisible(true);
        //Ponemos el case de productos en blanco
        interfaz_compra.case_productos.setSelectedItem(null);
        //Añadimos elementos del interfaz al ActionListener
        interfaz_compra.confirmar_compra.addActionListener(this);
        interfaz_compra.case_productos.addActionListener(this);
        interfaz_compra.añadir_carro.addActionListener(this);
        //Bloqueamos el spinner
        ((DefaultEditor) interfaz_compra.numero_productos.getEditor()).getTextField().setEditable(false);
        interfaz_compra.numero_productos.setModel(new SpinnerNumberModel(0,0,0,1));
        //Alineamos los elementos del Jtable al 
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        interfaz_compra.carro.setDefaultRenderer(String.class, centerRenderer);
    }
    
    //Actualiza el array de productos que el cliente puede comprar
    public void actualizar_productos() throws SQLException{
        productos=conector.getProductos("1");
        for(int i=0;i<productos.size();i++){
            interfaz_compra.case_productos.addItem(productos.get(i).getNombre());
        }
    }
    
    //Devuelve un producto del array en funcion de su nombre
    private Producto get_producto(String nombre){
        for(int i=0;i<productos.size();i++){
            if(productos.get(i).getNombre().equals(nombre)) return productos.get(i);
        }
        return null;
    }
    
    /*Metodo al que accedemos cuando activamos algun elemento 
      de la interfaz que se encuentra en el ActionListener*/
    @Override
    public void actionPerformed(ActionEvent e) {
        //Objeto que referencia al elemento del interfaz que hemos activado 
        Object fuente=e.getSource();
        //TableModel del carro
        DefaultTableModel model = (DefaultTableModel) interfaz_compra.carro.getModel();
        
        //Si seleccionamos un producto del case
        if(fuente==interfaz_compra.case_productos){
            //Ponemos el spinner a 0
            interfaz_compra.numero_productos.setValue(0);
            //Obtenemos el stock del producto seleccionado
            String seleccion=(String)interfaz_compra.case_productos.getSelectedItem();
            int size=0;
            size=get_producto(seleccion).getCantidad();
            //Limitamos el spinner para que el cliente no pueda comprar mas del stock
            interfaz_compra.numero_productos.setModel(new SpinnerNumberModel(0,0,size,1));
            interfaz_compra.precio.setText(get_producto(seleccion).getPrecio(cliente.getRoll())+"€");
        }
        
        //Si activamos añadir al carro
        if(fuente==interfaz_compra.añadir_carro){
            //Ponemos el precio final de la compra a 0
            precio_final=0;
            //Si el numero de producto añadido es mayor que 0
            if((int)interfaz_compra.numero_productos.getValue()>0){
                //Obtenemos del array el producto en funcion del nombre en el case seleccionado
                Producto producto=new Producto();
                producto=get_producto((String)interfaz_compra.case_productos.getSelectedItem());
                //Borramos la fila de ese mismo producto en el carro si existe
                for(int i=0;i<interfaz_compra.carro.getRowCount();i++){
                    if(((String)model.getValueAt(i, 0)).equals(
                            (String)interfaz_compra.case_productos.getSelectedItem())){
                        model.removeRow(i);
                    }
                }
                //Calculamos el precio final del producto
                float precio=(int)interfaz_compra.numero_productos.getValue()*producto.getPrecio(cliente.getRoll());
                //Redondeamos por defecto a 2 decimales
                precio = precio*100;
                precio=(int) precio;
                precio=precio/100;     
                //Añadimos la fila al carro con la cantidad actualizada
                model.addRow(new Object[]{producto.getNombre(), (int)interfaz_compra.numero_productos.getValue(),
                    Float.toString(precio)+"€"});
                //Si la cantidad es 0
            }else{
                //Si hay algun producto igual en el carro lo borra
                for(int i=0;i<interfaz_compra.carro.getRowCount();i++){
                    if(((String)model.getValueAt(i, 0)).equals((String)interfaz_compra.case_productos.getSelectedItem())){
                        model.removeRow(i);
                    }
                }
            }
            //Calculamos el nuevo precio final en funcion del precio de cada producto y su cantidad
            for(int i=0;i<interfaz_compra.carro.getRowCount();i++){
                String precio=(String)model.getValueAt(i, 2);
                precio = precio.substring(0, precio.length()-1);
                precio_final = precio_final+Float.parseFloat(precio);
                }
            //Ponemos el precio final en la interfaz
            interfaz_compra.precio_final.setText(Float.toString(precio_final)+"€");
        }
        
        //Si activamos confirmar compra y el carro no esta vacío
        if(fuente==interfaz_compra.confirmar_compra && interfaz_compra.carro.getRowCount()>0){
            //Instanciamos una compra
            Compra compra=new Compra();
            compra.setUsuario(cliente);
            compra.setPrecio(precio_final);
            String descripcion="";
            //Rellenamos la descripcion con los productos de la compra
            for(int i=0;i<interfaz_compra.carro.getRowCount();i++){
                Producto producto=new Producto();
                producto=get_producto((String)model.getValueAt(i, 0));
                int canti=producto.getCantidad();
                canti=canti-(int)model.getValueAt(i, 1);
                producto.setCantidad(canti);
                try {
                    //Restamos del stock la cantidad de producto comprada
                    conector.updateProducto_cantidad(producto);
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorCompra.class.getName()).log(Level.SEVERE, null, ex);
                }
                    //Rellenamos la descripcion
                    descripcion=descripcion+Integer.toString((int)model.getValueAt(i, 1))+"   "+
                            ((String)model.getValueAt(i, 0))+"\n";
            }
            //Ponemos la descripcion
            compra.setDescripcion(descripcion);
            try {
                //Se crea la compra
                conector.createCompra(compra);
            } catch (SQLException ex) {
                Logger.getLogger(ControladorCompra.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Mensaje cuando la compra se realiza con exito
            JOptionPane.showMessageDialog(null,"Compra realizada con exito.");
            //Cerramos el interfaz de cliente antigua
            interfaz_cliente.dispose();
            //Creamos una nueva para que se actualicen las compras
            ICliente interfaz_cliente_nuevo=new ICliente();
            try {
                //Creamos un controlador y le pasamos la nueva interfaz
                ControladorCliente controlador_cliente=new ControladorCliente(interfaz_cliente_nuevo,conector,cliente);
            } catch (SQLException ex) {
                Logger.getLogger(ControladorCompra.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Cerramos la interfaz de compra
            interfaz_compra.dispose();
        }
    }
}