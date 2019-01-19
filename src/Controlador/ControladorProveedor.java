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
private IProveedor pro;
private Conector Cn;
private Usuario user;
private ArrayList<Pedido> pedidos;
private ArrayList<Producto> productos;
private boolean add;
    ControladorProveedor(IProveedor pro, Conector Cn, Usuario user) throws SQLException {
        this.pro=pro;
        this.Cn=Cn;
        this.user=user;
        actualizar();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        pro.lista_pedidos.setDefaultRenderer(String.class, centerRenderer);
        pro.lista_productos.setDefaultRenderer(String.class, centerRenderer);
        pro.id_label.setText(user.getNombre());
        pro.setVisible(true);
        pro.setLocationRelativeTo(null);
        pro.actualizar.addActionListener(this);
        pro.anadir.addActionListener(this);
        pro.borrar.addActionListener(this);
        pro.enviar_pedido.addActionListener(this);
        pro.idpass.addActionListener(this);  
        ListSelectionModel modelo = pro.lista_pedidos.getSelectionModel();
        modelo.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                try{
            pro.descripcion_pedido.setText(pedidos.get(pro.lista_pedidos.getSelectedRow()).getDescripcion());        
                }catch(Exception envio){
                }
            }
        });
                }
    public void actualizar() throws SQLException{
        DefaultTableModel modelo = (DefaultTableModel) pro.lista_pedidos.getModel();
        int a =modelo.getRowCount()-1;
        for(int i=a; i>=0; i--){
        modelo.removeRow(i);
        }
        pedidos=null;
        pedidos=Cn.getPedidos(user.getId());
        for(int i=0;i<pedidos.size();i++){
        modelo.addRow(new Object[]{pedidos.get(i).getId(), pedidos.get(i).getPrecio()+"€", (pedidos.get(i).getFecha()).substring(0,10)});
        }
        modelo = (DefaultTableModel) pro.lista_productos.getModel();
        a =modelo.getRowCount()-1;
        for(int i=a; i>=0; i--){
        modelo.removeRow(i);
        }
        productos=null;
        productos=Cn.getProductos(user.getId());
        for(int i=0;i<productos.size();i++){
            String aceptado="";
            if(productos.get(i).getEstado() == 1) aceptado="Aceptado";
            else{aceptado="Pendiente";}
        modelo.addRow(new Object[]{productos.get(i).getNombre(), productos.get(i).getPrecio(user.getRoll())+"€",aceptado});
        }
    }
    public Pedido getPedido(int id){
        for(int i=0;i<pedidos.size();i++){
            if(pedidos.get(i).getId()==id) return pedidos.get(i);
        }
        return null;
    }
    public Producto getPedido(String nombre){
        for(int i=0;i<productos.size();i++){
            if(productos.get(i).getNombre().equals(nombre)) return productos.get(i);
        }
        return null;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object fuente=e.getSource();
        if(fuente==pro.actualizar){
            try {
                actualizar();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorProveedor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(fuente==pro.anadir){
            add=true;
            Producto p=new Producto();
            try {
                p=Cn.getProducto(pro.nombre.getText()+"-"+user.getNombre());
            } catch (SQLException ex) {
                Logger.getLogger(ControladorProveedor.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(pro.nombre.getText().isEmpty()){
              JOptionPane.showMessageDialog(null,"Introduzca un nombre para su producto.");
              add=false;
            }
            if(p.getNombre()!=null){
              JOptionPane.showMessageDialog(null,"El producto ya ha sido añadido.");
              add=false;
            }
            if(pro.precio.getText().isEmpty()){
              JOptionPane.showMessageDialog(null,"Introduzca un precio para su producto.");
              add=false;
            }
            if(add==true){
 
                try {
                    float prize = Float.parseFloat(pro.precio.getText());
                    p.setNombre(pro.nombre.getText()+"-"+user.getNombre());
                    p.setUser(user);
                    p.setDescripcion(pro.descripcion.getText());
                    prize=prize*100;
                    prize=(int) prize;
                    prize=prize/100;
                    p.setPreciop(prize);
                    Cn.setProducto(p);
                    actualizar();          
                    pro.nombre.setText("");
                    pro.descripcion.setText("");     
                    pro.precio.setText("");
                } catch (Exception ex) {
                    Logger.getLogger(ControladorProveedor.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null,"Introduzca un precio valido.");
                }
            }
        }
        if(fuente==pro.borrar){
            try {
                Producto p=new Producto();               
                p=Cn.getProducto((String) pro.lista_productos.getValueAt(pro.lista_productos.getSelectedRow(), 0));
                p.setEstado(2);
                Cn.updateProducto_estado(p);
                actualizar();                       
            } catch (Exception ex) {
                Logger.getLogger(ControladorProveedor.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null,"Error al borrar el producto.");
            }
        }
        if(fuente==pro.enviar_pedido){
          Pedido ped=new Pedido();
          try {
                ped=Cn.getPedido((int) pro.lista_pedidos.getValueAt(pro.lista_pedidos.getSelectedRow(), 0));
                ped.setEstado(1);
                Cn.updatePedido(ped);
                actualizar();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,"Error al borrar el producto.");
            }
        }
    }
}
    

