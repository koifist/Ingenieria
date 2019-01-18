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
import Vista.IAdministrador;
import Vista.IProveedor;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Fernando
 */
public class ControladorAdmin {
private IProveedor pro;
private Conector Cn;
private Usuario user;
private ArrayList<Pedido> pedidos;
private ArrayList<Producto> productos;
    ControladorAdmin(IAdministrador admin, Conector Cn) {
        /*admin.setVisible(true);
        admin.setLocationRelativeTo(null);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.pro=pro;
        this.Cn=Cn;
        this.user=user;
        actualizarPedidos();
        actualizasProductos();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        pro.lista_pedidos.setDefaultRenderer(String.class, centerRenderer);
        pro.lista_productos.setDefaultRenderer(String.class, centerRenderer);
        pro.id_label.setText(user.getNombre());
        pro.setVisible(true);
        pro.setLocationRelativeTo(null);
        pro.actualizar.addActionListener(this);
        ListSelectionModel modelo = emp.lista_pedidos.getSelectionModel();
        modelo.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                try{
            emp.descripcion.setText(pedidos.get(emp.lista_pedidos.getSelectedRow()).getDescripcion());        
                }catch(Exception ee){}
                }
        });
    */}
}
