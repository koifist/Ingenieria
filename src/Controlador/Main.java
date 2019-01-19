package Controlador;

import Modelo.Conector;
import Vista.ILogin;
import java.sql.SQLException;

//Main
public class Main {
    public static void main(String[] args) throws SQLException{
        
        //Creamos interfaz del Login
        ILogin login= new ILogin();
        //Creamos el conector a la base de datos 
        Conector conector= new Conector();
        //Creamos el controlador y le pasamos el login y el conector
        ControladorLogin controlador_login=new ControladorLogin(login,conector);
    }
}