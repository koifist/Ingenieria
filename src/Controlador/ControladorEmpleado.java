/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Conector;
import Modelo.Usuario;
import Vista.IEmpleado;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Fernando
 */
public class ControladorEmpleado implements ActionListener{
private IEmpleado emp;
private Conector Cn;
private Usuario user;
    ControladorEmpleado(IEmpleado emp, Conector Cn, Usuario user) {
        this.emp=emp;
        this.Cn=Cn;
        this.user=user;
        emp.setVisible(true);
        emp.setLocationRelativeTo(null);
    }

    public void actionPerformed(ActionEvent e) {
    }
    
}
