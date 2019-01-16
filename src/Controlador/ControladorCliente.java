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
        iniciarCompras();
        cli.setVisible(true);
        cli.setLocationRelativeTo(null);
        cli.idLabel.setText(user.getId());
            this.cli.verCompra.addActionListener(this);
            this.cli.idpass.addActionListener(this);
            this.cli.realizarCompra.addActionListener(this);
        }
        public void iniciarCompras() throws SQLException{
        compras = new ArrayList<Compra>();
        compras=Cn.getCompras(user.getId());
        DefaultTableModel model = (DefaultTableModel) cli.lista_compras.getModel();
        for(int i=compras.size()-1;i>=0;i--){
        model.addRow(new Object[]{compras.get(i).getId(), compras.get(i).getFecha(), compras.get(i).getPrecio()});
        }
        }

    @Override
    public void actionPerformed(ActionEvent e){
        Object fuente= e.getSource();
        if(fuente==cli.verCompra){
            try {
            compras=Cn.getCompras(user.getId());
            } catch (SQLException ex) {
            Logger.getLogger(ControladorCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            cli.compraDescripcion.setText(compras.get(compras.size()-cli.lista_compras.getSelectedRow()-1).getDescripcion());
            cli.idCompra.setText(compras.get(compras.size()-cli.lista_compras.getSelectedRow()-1).getId());
        }
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
