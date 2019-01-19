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
private IAdministrador adm;
private Conector Cn;
private ArrayList<Producto> productos;
    ControladorAdministrador(IAdministrador adm, Conector Cn) throws SQLException {
        this.adm=adm;
        this.Cn=Cn;
        adm.setVisible(true);
        adm.setLocationRelativeTo(null);
        actualizar();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        adm.list_prod.setDefaultRenderer(String.class, centerRenderer);
        adm.list_prod.setDefaultRenderer(int.class, centerRenderer);
        adm.nuevo_empleado.addActionListener(this);
        adm.nuevo_proveedor.addActionListener(this);
        adm.aceptar_producto.addActionListener(this);
        adm.modificar_precio.addActionListener(this);
        adm.borrar_producto.addActionListener(this);
        ListSelectionModel modelo = adm.list_prod.getSelectionModel();
        modelo.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                try{
                adm.prize_cli.setText("");
                adm.prize_prov.setText(Float.toString(productos.get(adm.list_prod.getSelectedRow()).getPrecio(0)));
                if(productos.get(adm.list_prod.getSelectedRow()).getPrecio(3)>0){
                adm.prize_cli.setText(Float.toString(productos.get(adm.list_prod.getSelectedRow()).getPrecio(3)));   
                }
                }catch(Exception ee){}
                }
        });
    }
     private void actualizar() throws SQLException {
        DefaultTableModel modelo = (DefaultTableModel) adm.list_prod.getModel();
        int a =modelo.getRowCount()-1;
        for(int i=a; i>=0; i--){
        modelo.removeRow(i);
        }
        productos=null;
        productos=Cn.getProductos("0");
        for(int i=0;i<productos.size();i++){
            String aceptado="";
            if(productos.get(i).getEstado() == 0) aceptado="Pendiente";
            if(productos.get(i).getEstado() == 1) aceptado="Aceptado";
            if(productos.get(i).getEstado() == 2) aceptado="Borrado";
        modelo.addRow(new Object[]{productos.get(i).getNombre(),productos.get(i).getUser().getNombre(),aceptado,String.valueOf(productos.get(i).getCantidad())});
        }    
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object fuente=e.getSource();
        if(fuente==adm.nuevo_empleado){
            IRegistro reg=new IRegistro();
            ControladorRegistro control=new ControladorRegistro(reg,Cn,1);
        }
        if(fuente==adm.nuevo_proveedor){
            IRegistro reg=new IRegistro();
            ControladorRegistro control=new ControladorRegistro(reg,Cn,2);
        }
        if(fuente==adm.aceptar_producto){
            Producto p=new Producto();
            p=productos.get(adm.list_prod.getSelectedRow());
            if(p.getEstado()==2){
            JOptionPane.showMessageDialog(null,"El producto ya no esta disponible.");
            }
            if(p.getEstado()==0){
            try {
                float prize=Float.parseFloat(adm.prize_cli.getText());
                if(prize>0){
                if(prize<p.getPrecio(0)){
                JOptionPane.showMessageDialog(null,"El precio de venta es menor que el precio de compra.");
                }
                prize=prize*100;
                prize=(int) prize;
                prize=prize/100;
                p.setPrecioc(prize);
                p.setEstado(1);
                Cn.updateProducto_estado(p);
                }
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorAdministrador.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null,"El precio introducido no es válido.");
                }
            }
            try {
            actualizar();
            } catch (SQLException ex) {
            Logger.getLogger(ControladorAdministrador.class.getName()).log(Level.SEVERE, null, ex);
            }
            adm.prize_cli.setText("");
        }
        if(fuente==adm.modificar_precio){
            Producto p=new Producto();
            p=productos.get(adm.list_prod.getSelectedRow());
            if(p.getEstado()==2){
            JOptionPane.showMessageDialog(null,"El producto ya no esta disponible.");
            }
            if(p.getEstado()==1){
            try {
                float prize=Float.parseFloat(adm.prize_cli.getText());
                if(prize>0){
                if(prize<p.getPrecio(0)){
                JOptionPane.showMessageDialog(null,"El precio de venta es menor que el precio de compra.");
                }
                prize=prize*100;
                prize=(int) prize;
                prize=prize/100;
                p.setPrecioc(prize);
                Cn.updateProducto_estado(p);
                }
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorAdministrador.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null,"El precio introducido no es válido.");
                
            }
            }
            try {
            actualizar();
            } catch (SQLException ex) {
            Logger.getLogger(ControladorAdministrador.class.getName()).log(Level.SEVERE, null, ex);
            }
            adm.prize_cli.setText("");
        }
    
    if(fuente==adm.borrar_producto){
            Producto p=new Producto();
            p=productos.get(adm.list_prod.getSelectedRow());
            if(p.getEstado()==2){
            JOptionPane.showMessageDialog(null,"El producto ya esta borrado.");
            }
            p.setEstado(2);
            try {
                Cn.updateProducto_estado(p);
                actualizar();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorAdministrador.class.getName()).log(Level.SEVERE, null, ex);
            }
            adm.prize_cli.setText("");
        }
    }

   
}
