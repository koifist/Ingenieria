/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Conector;
import Modelo.Pedido;
import Modelo.Producto;
import Modelo.Usuario;
import Vista.ICambio;
import Vista.ICompra;
import Vista.IEmpleado;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Fernando
 */
public class ControladorEmpleado implements ActionListener{
private IEmpleado emp;
private Conector Cn;
private Usuario user;
private ArrayList<Pedido> pedidos;
private ArrayList<Usuario> proveedores;
    ControladorEmpleado(IEmpleado emp, Conector Cn, Usuario user) throws SQLException {
        this.emp=emp;
        this.Cn=Cn;
        this.user=user;
        actualizarPedidos();
        proveedores();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        emp.lista_pedidos.setDefaultRenderer(String.class, centerRenderer);
        emp.id_label.setText(user.getNombre());
        emp.setVisible(true);
        emp.setLocationRelativeTo(null);
        emp.confirmar_pedido.addActionListener(this);
        emp.realizar_pedido.addActionListener(this);
        emp.idpass.addActionListener(this);
        emp.actualizar.addActionListener(this);
        ListSelectionModel modelo = emp.lista_pedidos.getSelectionModel();
        modelo.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                try{
            emp.descripcion.setText(pedidos.get(emp.lista_pedidos.getSelectedRow()).getDescripcion());        
                }catch(Exception ee){}
                }
        });


    }
    public Usuario getProv(String nombre){
        for(int i=0;i<proveedores.size();i++){
            if(proveedores.get(i).getNombre().equals(nombre)) return proveedores.get(i);
        }
        return null;
    }
    public Pedido getPedido(int id){
        for(int i=0;i<pedidos.size();i++){
            if(pedidos.get(i).getId()==id) return pedidos.get(i);
        }
        return null;
    }
    public void proveedores() throws SQLException{
        proveedores=Cn.getProveedores();
        for(int i=0;i<proveedores.size();i++){
            emp.case_prov.addItem(proveedores.get(i).getNombre());
        }
    emp.case_prov.setSelectedItem(null);

    }
    public void actualizarPedidos() throws SQLException{
        DefaultTableModel modelo = (DefaultTableModel) emp.lista_pedidos.getModel();
         int a =modelo.getRowCount()-1;
        for(int i=a; i>=0; i--){
        modelo.removeRow(i);
        }
        pedidos=null;
        pedidos=Cn.getPedidos("1");
        for(int i=0;i<pedidos.size();i++){
        modelo.addRow(new Object[]{pedidos.get(i).getId(), pedidos.get(i).getUser().getNombre(), pedidos.get(i).getPrecio()+"€", (pedidos.get(i).getFecha()).substring(0,10)});
        }
    }
    public void actionPerformed(ActionEvent e) {
        Object fuente=e.getSource();
        if(fuente==emp.actualizar){
            try {
                emp.descripcion.setText("");
                actualizarPedidos();
            } catch (SQLException ex) {
                Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
            }
                }
        if(fuente==emp.realizar_pedido && emp.case_prov.getSelectedItem()!=null){
            ICompra com=new ICompra();
            try {
                ControladorPedido control=new ControladorPedido(user,Cn,com,getProv((String)emp.case_prov.getSelectedItem()));
            } catch (SQLException ex) {
                Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(fuente==emp.idpass){
            ICambio cam=new ICambio();
            ControladorCambio control=new ControladorCambio(user,cam,Cn);
        }
        if(fuente==emp.confirmar_pedido){
            emp.descripcion.setText("");
            Pedido p=null;
            p=getPedido((int)emp.lista_pedidos.getValueAt(emp.lista_pedidos.getSelectedRow(),0));
            p.setEstado(2);
            String descPedido=p.getDescripcion();
            String []add=descPedido.split("\n");
            for(int i=0;i<add.length;i++){
                String []canti=add[i].split("   ");
                Producto prod=new Producto();
                try {
                    prod=Cn.getProducto(canti[1]);
                    prod.setCantidad(prod.getCantidad()+Integer.parseInt(canti[0]));
                    Cn.updateProducto_cantidad(prod);
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
                try {
                    Cn.updatePedido(p);
                    actualizarPedidos();
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
    
    
}
