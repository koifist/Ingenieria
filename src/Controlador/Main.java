/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Conector;
import Vista.Inicio;
import java.sql.SQLException;

/**
 *
 * @author Fernando
 */
public class Main {
    public static void main(String[] args) throws SQLException{
        Inicio In= new Inicio();
        Conector Cn= new Conector();
        ControladorInicio control=new ControladorInicio(In,Cn);
    }
}
