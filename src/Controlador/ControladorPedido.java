package Controlador;
import Modelo.*;
import Vista.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.table.*;

public class ControladorPedido implements ActionListener{
    //Interfaz de compra
    private ICompra interfaz_compra;
    //Conector de la base de datos
    private Conector conector;
    //Empleado que esta usando el software
    private Usuario empleado;
    //Proveedor al que van a hacer el pedido
    private Usuario proveedor;
    //Precio final del pedido
    private float precio_final=0;
    //Array de productos disponibles del proveedor
    ArrayList<Producto> productos;
    
    //Constructor
    public ControladorPedido(Usuario usuario, Conector conector, ICompra i_compra, Usuario proveedor) throws SQLException{
        //Inicializamos las variables globales
        this.empleado=usuario;
        this.conector=conector;
        this.interfaz_compra=i_compra;
        this.proveedor=proveedor;
        //Colocamos el nombre del empleado en el interfaz
        interfaz_compra.id_label.setText(empleado.getNombre());
        //inicializamos los productos en el case de eleccion
        inicializar_productos();
        //Colocamos la interfaz en el centro de la pantalla
        interfaz_compra.setLocationRelativeTo(null);
        //Hacemos la interfaz visible
        interfaz_compra.setVisible(true);
        //Ponemos el case en blanco
        interfaz_compra.case_productos.setSelectedItem(null);
        //Añadimos los elementos del interfaz al ActionListener
        interfaz_compra.confirmar_compra.addActionListener(this);
        interfaz_compra.case_productos.addActionListener(this);
        interfaz_compra.añadir_carro.addActionListener(this);
        //Alineamos los elementos del JTable al centro
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        interfaz_compra.carro.setDefaultRenderer(String.class, centerRenderer);
    }
    
    //Inicializa los productos del case 
    public void inicializar_productos() throws SQLException{
        productos=conector.getProductos(proveedor.getId());
        for(int i=0;i<productos.size();i++){
            interfaz_compra.case_productos.addItem(productos.get(i).getNombre());
        }
    }
    
    //Devuelve un producto del array en funcion de su nombre
    private Producto devolver_producto(String nombre){
        for(int i=0;i<productos.size();i++){
            if(productos.get(i).getNombre().equals(nombre)) return productos.get(i);
        }
        return null;
    }
    
    /*Metodo al que accedemos cuando activamos algun elemento 
      de la interfaz que se encuentra en el ActionListener*/
    @Override
    public void actionPerformed(ActionEvent e) {
        //Objeto que referencia al elemento activado
        Object fuente=e.getSource();
        //TableModel del carro
        DefaultTableModel model = (DefaultTableModel) interfaz_compra.carro.getModel();
        
        //Si seleccionamos un elemento del case de productos
        if(fuente==interfaz_compra.case_productos){
            //Ponemos el spinner a 0
            interfaz_compra.numero_productos.setValue(0);
            //Indicamos el precio por unidad del articulo seleccionado
            String seleccion=(String)interfaz_compra.case_productos.getSelectedItem();
            interfaz_compra.precio.setText(devolver_producto(seleccion).getPrecio(empleado.getRoll())+"€");
        }
        
        //Si activas añadir al carro
        if(fuente==interfaz_compra.añadir_carro){
            //Ponemos precio_final a 0
            precio_final=0;
            //Si se ha introducido algun producto
            if((int)interfaz_compra.numero_productos.getValue()>0){
                //Obtenemos el producto en funcion del nombre que hay en el case
                Producto p=new Producto();
                p=devolver_producto((String)interfaz_compra.case_productos.getSelectedItem());
                //borramos la fila si ya existe con el producto que queremos añadir para actualizar la cantidad
                for(int i=0;i<interfaz_compra.carro.getRowCount();i++){
                    if(((String)model.getValueAt(i, 0)).equals((String)interfaz_compra.case_productos.getSelectedItem())){
                        model.removeRow(i);
                    }
                }
                //Redondeamos por defecto el precio final del producto
                float precio=(int)interfaz_compra.numero_productos.getValue()*p.getPrecio(empleado.getRoll());
                precio = Math.round(precio*100);
                precio=precio/100;
                //añadimos una fila
                model.addRow(new Object[]{p.getNombre(), (int)interfaz_compra.numero_productos.getValue(),  Float.toString(precio)+"€"});
                //Si ponemos la cantidad a 0 simplemente borra el producto del JTable
                }else{
                    for(int i=0;i<interfaz_compra.carro.getRowCount();i++){
                        if(((String)model.getValueAt(i, 0)).equals((String)interfaz_compra.case_productos.getSelectedItem())){
                            model.removeRow(i);
                        }
                    }
                }
                //Actualizamos el precio final
                for(int i=0;i<interfaz_compra.carro.getRowCount();i++){
                    String precio=(String)model.getValueAt(i, 2);
                    precio = precio.substring(0, precio.length()-1);
                    precio_final = precio_final+Float.parseFloat(precio);
                    }
                //Mostramos el precio final en la interfaz
                interfaz_compra.precio_final.setText(Float.toString(precio_final)+"€");
                }
        
        //Si activamos confirmar compra
        if(fuente==interfaz_compra.confirmar_compra && interfaz_compra.carro.getRowCount()>0){
            //Creamos un nuevo pedido
            Pedido pedido=new Pedido();
            pedido.setUsuario(proveedor);
            pedido.setPrecio(precio_final);
            String descripcion="";
            //añadimos a la descripcion los productos del pedido
            for(int i=0;i<interfaz_compra.carro.getRowCount();i++){
                    descripcion=descripcion+Integer.toString((int)model.getValueAt(i, 1))+"   "+((String)model.getValueAt(i, 0))+"\n";
                    }
            pedido.setDescripcion(descripcion);
            try {
                //Añadimos el pedido a la base de datos
                conector.createPedido(pedido);
            } catch (SQLException ex) {
                Logger.getLogger(ControladorCompra.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Mostramos que todo se ha realizado correctamente
            JOptionPane.showMessageDialog(null,"Pedido realizado con exito.");
            //Cerramos la vetana
            interfaz_compra.dispose();
        }
    }
}