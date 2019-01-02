package Controlador;

import java.sql.SQLException;
import java.util.ArrayList;

import Modelo.*;
import Vista.*;

public class Controlador{

	public static void main(String[] args) throws SQLException {
		Conector cn = new Conector();
		Usuario usr = cn.getUser("pepito");
	System.out.println(usr.getId()+" "+usr.getApellido1());
		ArrayList<Compra> compras = cn.getCompras(usr.getId());
		System.out.println(compras.get(0).getFecha());
		}

}
