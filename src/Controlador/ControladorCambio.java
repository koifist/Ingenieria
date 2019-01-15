/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.*;
import Vista.ICambio;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fernando
 */
public class ControladorCambio implements ActionListener{
private Usuario user;
private Conector Cn;
private ICambio cambio;
    public ControladorCambio(Usuario user, ICambio cambio, Conector Cn){
        this.user=user;
        this.cambio=cambio;
        this.Cn=Cn;
        cambio.setVisible(true);
        cambio.setLocationRelativeTo(null);
        cambio.cambiar_datos.addActionListener(this);
    }
    public void actionPerformed(ActionEvent e) {
          
                if(!cambio.pass_actual.getText().equals(user.getPass())){
                cambio.error.setText("Contraseña actual incorrecta.");
                }else{
                if(!cambio.pass_nueva.getText().equals(cambio.pass_nueva_confirm.getText())){
                    cambio.error.setText("La nueva contraseña no coincide.");
                 }else{
                    String idpast;
                    idpast=user.getId();
                    user.setPass(cambio.pass_nueva.getText());
                try {
                    Cn.updateUser(user);
                    cambio.dispose();
                } catch (SQLException ex) {
                    Logger.getLogger(ControladorCambio.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
                
                }
            
        }
        }
    

