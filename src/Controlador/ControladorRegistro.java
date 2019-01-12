/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Conector;
import Modelo.Usuario;
import Vista.IRegistro;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fernando
 */
class ControladorRegistro implements ActionListener{
private int roll;
private IRegistro reg;
private Conector Cn;
    ControladorRegistro(IRegistro reg, Conector Cn, int i) {
        this.roll=i;
        this.reg=reg;
        this.Cn=Cn;
        reg.setLocationRelativeTo(null);
        reg.setVisible(true);
        reg.sign_up.addActionListener(this);
        reg.user_error.setVisible(false);
        reg.pass_error.setVisible(false);
        reg.rpass_error.setVisible(false);
        reg.name_error.setVisible(false);
        reg.DNI_error.setVisible(false);
        reg.address_error.setVisible(false);

        
    }

@Override
    public void actionPerformed(ActionEvent e) {
        reg.error.setText("");
        reg.user_error.setVisible(false);
        reg.pass_error.setVisible(false);
        reg.rpass_error.setVisible(false);
        reg.name_error.setVisible(false);
        reg.DNI_error.setVisible(false);
        reg.address_error.setVisible(false);
       if(reg.user.getText().equals("")){
           reg.error.setText("Rellene todos los campos.");
           reg.user_error.setVisible(true);
       }
       if(reg.pass.getText().equals("")){
           reg.error.setText("Rellene todos los campos.");
           reg.pass_error.setVisible(true);
       }
       if(reg.rpass.getText().equals("")){
           reg.error.setText("Rellene todos los campos.");
           reg.rpass_error.setVisible(true);
       }
       if(reg.name.getText().equals("")){
           reg.error.setText("Rellene todos los campos.");
           reg.name_error.setVisible(true);
       }
       if(reg.DNI.getText().equals("")){
           reg.error.setText("Rellene todos los campos.");
           reg.DNI_error.setVisible(true);
       }
       if(reg.address.getText().equals("")){
           reg.error.setText("Rellene todos los campos.");
           reg.address_error.setVisible(true);
       }
       if(reg.error.getText().equals("")){
            try {
                Usuario user=Cn.getUser(reg.user.getText());
                if(user.id == null){
                    if(reg.rpass.getText().equals(reg.pass.getText())){
                        user.id=reg.user.getText();
                        user.pass=reg.pass.getText();
                        user.nombre=reg.name.getText();
                        user.dni=reg.DNI.getText();
                        user.direccion=reg.address.getText();
                        Cn.setUser(user);
                    }else{
                    reg.error.setText("La contraseña no coincide.");
                    reg.pass_error.setVisible(true);
                    reg.rpass_error.setVisible(true);
                    }
                }else{
                reg.error.setText("El usuario ya existe.");
                reg.user_error.setVisible(true);
                }
            } catch (SQLException ex) {
                reg.error.setText("Error al introducir los campos.");
            }
       }
    }
    
}
