/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Conector;
import Vista.IAdministrador;

/**
 *
 * @author Fernando
 */
public class ControladorAdmin {

    ControladorAdmin(IAdministrador admin, Conector Cn) {
        admin.setVisible(true);
        admin.setLocationRelativeTo(null);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
