/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.*;
import Vista.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Fernando
 */
public class ControladorCompra implements ActionListener{
    private ICompra com;
    private Conector Cn;
    private Usuario user;
    private ICliente cli;
    private float preciofin=0;
    ArrayList<Producto> productos;
    public ControladorCompra(Usuario user, Conector Cn, ICompra com,ICliente cli) throws SQLException{
        this.user=user;
        this.Cn=Cn;
        this.com=com;
        this.cli=cli;
            com.id_label.setText(user.getId());
            inicializarProductos();
            com.setLocationRelativeTo(null);
            com.setVisible(true);
            com.case_productos.setSelectedItem(null);
            com.confirmar_compra.addActionListener(this);
            com.case_productos.addActionListener(this);
            com.añadir_carro.addActionListener(this);
            ((DefaultEditor) com.numero_productos.getEditor()).getTextField().setEditable(false);
            com.numero_productos.setModel(new SpinnerNumberModel(0,0,0,1));
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment( JLabel.CENTER );
            com.carro.setDefaultRenderer(String.class, centerRenderer);
    }
    public void inicializarProductos() throws SQLException{
        productos=Cn.getProductos("1");
        for(int i=0;i<productos.size();i++){
            com.case_productos.addItem(productos.get(i).getNombre());
        }
    }
    private Producto getPro(String nombre){
        for(int i=0;i<productos.size();i++){
            if(productos.get(i).getNombre().equals(nombre)) return productos.get(i);
        }
        return null;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Object fuente=e.getSource();
        DefaultTableModel model = (DefaultTableModel) com.carro.getModel();
        if(fuente==com.case_productos){
            com.numero_productos.setValue(0);
            String seleccion=(String)com.case_productos.getSelectedItem();
            int size=0;
            size=getPro(seleccion).getCantidad();
            com.numero_productos.setModel(new SpinnerNumberModel(0,0,size,1));
            com.precio.setText(getPro(seleccion).getPrecio(user.getRoll())+"€");
        }
        if(fuente==com.añadir_carro){
            preciofin=0;
            if((int)com.numero_productos.getValue()>0){
                Producto p=new Producto();
                p=getPro((String)com.case_productos.getSelectedItem());
                for(int i=0;i<com.carro.getRowCount();i++){
                    if(((String)model.getValueAt(i, 0)).equals((String)com.case_productos.getSelectedItem())){
                        model.removeRow(i);
                    }
                }
                float precio=(int)com.numero_productos.getValue()*p.getPrecio(user.getRoll());
                precio = precio*100;
                precio=(int) precio;
                precio=precio/100;            
                model.addRow(new Object[]{p.getNombre(), (int)com.numero_productos.getValue(),  Float.toString(precio)+"€"});
                }else{
                for(int i=0;i<com.carro.getRowCount();i++){
                    if(((String)model.getValueAt(i, 0)).equals((String)com.case_productos.getSelectedItem())){
                        model.removeRow(i);
                    }
                }
                }               
                for(int i=0;i<com.carro.getRowCount();i++){
                    String precio=(String)model.getValueAt(i, 2);
                    precio = precio.substring(0, precio.length()-1);
                    preciofin = preciofin+Float.parseFloat(precio);
                    }
                com.precio_final.setText(Float.toString(preciofin)+"€");
                }
        if(fuente==com.confirmar_compra && com.carro.getRowCount()>0){
            Compra compra=new Compra();
            compra.setUser(user);
            compra.setPrecio(preciofin);
            String descripcion="";
            for(int i=0;i<com.carro.getRowCount();i++){
                Producto p=new Producto();
                    p=getPro((String)model.getValueAt(i, 0));
                    int canti=p.getCantidad();
                    canti=canti-(int)model.getValueAt(i, 1);
                    p.setCantidad(canti);
                try {
                    Cn.updateProducto_cantidad(p);
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorCompra.class.getName()).log(Level.SEVERE, null, ex);
                }
                    descripcion=descripcion+Integer.toString((int)model.getValueAt(i, 1))+"\t"+((String)model.getValueAt(i, 0))+"\n";
                    }
            compra.setDescripcion(descripcion);
            try {
                Cn.setCompra(compra);
            } catch (SQLException ex) {
                Logger.getLogger(ControladorCompra.class.getName()).log(Level.SEVERE, null, ex);
            }
            cli.dispose();
            ICliente cliente2=new ICliente();
            try {
                ControladorCliente control2=new ControladorCliente(cliente2,Cn,user);
            } catch (SQLException ex) {
                Logger.getLogger(ControladorCompra.class.getName()).log(Level.SEVERE, null, ex);
            }
            com.dispose();
        }
        }
    }

