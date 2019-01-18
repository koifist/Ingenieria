/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.*;
import Vista.IRegistro;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.logging.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Fernando
 */
class ControladorRegistro implements ActionListener{
private int roll;
private IRegistro reg;
private Conector Cn;
private boolean correcto;
    ControladorRegistro(IRegistro reg, Conector Cn, int roll) {
        this.roll=roll;
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
        correcto=true;
        reg.user_error.setVisible(false);
        reg.pass_error.setVisible(false);
        reg.rpass_error.setVisible(false);
        reg.name_error.setVisible(false);
        reg.DNI_error.setVisible(false);
        reg.address_error.setVisible(false);
       if(reg.user.getText().isEmpty() || reg.user.getText().length()<5 || reg.user.getText().length()>15){
            JOptionPane.showMessageDialog(null,"Introduzca un usuario que contenga entre 5 y 15 caracteres");
            reg.user_error.setVisible(true);
            correcto=false;
       }
       if(reg.pass.getText().isEmpty() || reg.pass.getText().length()<5 || reg.pass.getText().length()>10 ){
           JOptionPane.showMessageDialog(null,"Introduzca una contraseña que contenga entre 5 y 10 caracteres");
           reg.pass_error.setVisible(true);
           correcto=false;
       }
       if(reg.name.getText().isEmpty()){
           JOptionPane.showMessageDialog(null,"Introduzca su nombre y apellidos");
           reg.name_error.setVisible(true);
           correcto=false;
       }
       if(reg.DNI.getText().isEmpty() || reg.DNI.getText().length()!=9){
           if(roll==1 || roll==3){
           JOptionPane.showMessageDialog(null,"Introduzca el DNI con el formato 25535449K");
           }else{
           JOptionPane.showMessageDialog(null,"Introduzca el CIF con el formato V8242066B");
    
           }
           correcto=false;
           reg.DNI_error.setVisible(true);
       }
       if(reg.address.getText().isEmpty() || reg.address.getText().length()<5){
           JOptionPane.showMessageDialog(null,"Introduzca una direccion valida.");
           reg.address_error.setVisible(true);
           correcto=false;
       }
       if(correcto==true){  
            try {
                Usuario user=Cn.getUser(reg.user.getText());
                if(user.id == null){
                    if(reg.rpass.getText().equals(reg.pass.getText())){
                            user.setId(reg.user.getText());
                            user.setPass(reg.pass.getText());
                            user.setNombre(reg.name.getText());
                            user.setDNI(reg.DNI.getText());
                            user.setRoll(this.roll);
                            user.setDireccion(reg.address.getText());
                            Cn.setUser(user);
                            reg.dispose();
                    }else{
                        JOptionPane.showMessageDialog(null,"Las contraseñas no coinciden.");
                        reg.pass_error.setVisible(true);
                        reg.rpass_error.setVisible(true);
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"El usuario que ha introducido ya existe.");
                    reg.user_error.setVisible(true);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ControladorRegistro.class.getName()).log(Level.SEVERE, null, ex);
            } 
       }
    }
    }


    
    

