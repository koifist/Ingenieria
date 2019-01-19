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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


public class ControladorCliente implements ActionListener{
    private ICliente cli;
    private Conector Cn;
    private Usuario user;
    private ArrayList<Compra> compras;
    
    ControladorCliente(ICliente cli, Conector Cn, Usuario user) throws SQLException {
        this.cli=cli;
        this.Cn=Cn;
        this.user=user;
        actualizarCompras();
        cli.setVisible(true);
        cli.setLocationRelativeTo(null);
        cli.idLabel.setText(user.getNombre());
        this.cli.idpass.addActionListener(this);
        this.cli.realizarCompra.addActionListener(this);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        cli.lista_compras.setDefaultRenderer(String.class, centerRenderer);
        ListSelectionModel modelo = cli.lista_compras.getSelectionModel();
        modelo.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {    
                    cli.descripcion.setText(compras.get(compras.size()-cli.lista_compras.getSelectedRow()-1).getDescripcion());
            }
        });
        }
        public void actualizarCompras() throws SQLException{
        DefaultTableModel modelo = (DefaultTableModel) cli.lista_compras.getModel();
        int a =modelo.getRowCount()-1;
        for(int i=a; i>=0; i--){
        modelo.removeRow(i);
        }
        compras=null;
        compras=Cn.getCompras(user.getId());
        for(int i=compras.size()-1;i>=0;i--){
        modelo.addRow(new Object[]{compras.get(i).getId(), compras.get(i).getFecha(), compras.get(i).getPrecio()+"€"});
        }
        }

    @Override
    public void actionPerformed(ActionEvent e){
        Object fuente= e.getSource();
        if(fuente==cli.idpass){
            ICambio cambio=new ICambio();
            ControladorCambio control= new ControladorCambio(user,cambio,Cn);
        }
        if(fuente==cli.realizarCompra){
            ICompra com=new ICompra();
            try {
                ControladorCompra control=new ControladorCompra(user,Cn,com,cli);
            } catch (SQLException ex) {
                Logger.getLogger(ControladorCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
