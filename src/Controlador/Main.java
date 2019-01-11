/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.Conector;
import Vista.Inicio;

/**
 *
 * @author Fernando
 */
public class Main {
    public static void main(String[] args){
        Inicio In= new Inicio();
        Conector Cn= new Conector();
        Controlador control=new Controlador(In,Cn);
    }
}
