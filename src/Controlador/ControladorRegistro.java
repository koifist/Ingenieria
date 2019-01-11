/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Conector;
import Vista.IRegistro;

/**
 *
 * @author Fernando
 */
class ControladorRegistro {
private int roll;
    ControladorRegistro(IRegistro reg, Conector Cn, int i) {
        this.roll=i;
        reg.setVisible(true);
        reg.setLocationRelativeTo(null);
    }
    
}
