package Controlador;

import java.sql.SQLException;
import java.util.ArrayList;

import Modelo.*;
import Vista.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controlador implements ActionListener{
    private Inicio Inicio;
    private Conector Cn;
	public Controlador(Inicio in, Conector Cn){
            this.Inicio= in;
            this.Cn=Cn;
            this.Inicio.sign_in.addActionListener(this);
            this.Inicio.sign_up.addActionListener(this);
            Inicio.setLocationRelativeTo(null);
            Inicio.setVisible(true);
        }

 
    
    public void actionPerformed(ActionEvent e){
        Object fuente = e.getSource();
        if(fuente==Inicio.sign_in){
   
            try {
                Usuario user=Cn.getUser(Inicio.user.getText()); //To change body of generated methods, choose Tools | Templates.
                if(user.getId()==null){
                    Inicio.error.setText("El usuario no existe");
                }else{
                    if(user.getPass().equals(Inicio.pass.getText())){
                        if(user.getRoll()==0){
                         Inicio.setVisible(false);
                         IAdministrador admin=new IAdministrador();
                         ControladorAdmin control=new ControladorAdmin(admin,Cn);
               
                        }
                        if(user.getRoll()==1){
                            Inicio.setVisible(false);
                            IEmpleado emp=new IEmpleado();
                            ControladorEmpleado control=new ControladorEmpleado(emp,Cn,user);
                        }
                        if(user.getRoll()==2){
                            Inicio.setVisible(false);
                            IProveedor pro=new IProveedor();
                            ControladorProveedor control=new ControladorProveedor(pro,Cn,user);
                        }
                        if(user.getRoll()==3){
                            Inicio.setVisible(false);
                            ICliente cli=new ICliente();
                            ControladorCliente control=new ControladorCliente(cli,Cn,user);
                        }
                    }else{
                        Inicio.error.setText("Contraseña incorrecta");
                    }
                }
            } catch (SQLException ex) {
                Inicio.error.setText("Error al introducir los datos");
            }
        }
        if(fuente==Inicio.sign_up){
           IRegistro reg = new IRegistro();
           ControladorRegistro control=new ControladorRegistro(reg,Cn,3);
        }
        
    }

}
