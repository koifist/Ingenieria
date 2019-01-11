/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Conector;
import Modelo.Usuario;
import Vista.IProveedor;

/**
 *
 * @author Fernando
 */
public class ControladorProveedor {

    ControladorProveedor(IProveedor pro, Conector Cn, Usuario user) {
        pro.setVisible(true);
        pro.setLocationRelativeTo(null);
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
