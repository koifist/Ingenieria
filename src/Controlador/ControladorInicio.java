package Controlador;

import java.sql.SQLException;
import java.util.ArrayList;
import Modelo.*;
import Vista.*;
import java.awt.event.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ControladorInicio implements ActionListener{
    private Inicio Inicio;
    private Conector Cn;
	public ControladorInicio(Inicio in, Conector Cn){
            this.Inicio= in;
            this.Cn=Cn;
            this.Inicio.sign_in.addActionListener(this);
            this.Inicio.sign_up.addActionListener(this);
            this.Inicio.pass.addActionListener(this);
            this.Inicio.user.addActionListener(this);
            Inicio.setLocationRelativeTo(null);
            Inicio.setVisible(true);
        }

 
    
    @Override
    public void actionPerformed(ActionEvent e){
        Object fuente = e.getSource();
        if(fuente==Inicio.sign_in || fuente==Inicio.pass || fuente==Inicio.user){
            try {
                Usuario user=Cn.getUser(Inicio.user.getText()); //To change body of generated methods, choose Tools | Templates.
                if(user.getId()==null){
                    JOptionPane.showMessageDialog(null,"Introduzca un usuario valido.");
                }else{
                    if(user.getPass().equals(Inicio.pass.getText())){
                        if(user.getRoll()==0){
                            Inicio.setVisible(false);
                            IAdministrador admin=new IAdministrador();
                            ControladorAdministrador control=new ControladorAdministrador(admin,Cn);
                            
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
                        JOptionPane.showMessageDialog(null,"La contraseña es incorrecta.");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(ControladorInicio.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        if(fuente==Inicio.sign_up){
           IRegistro reg = new IRegistro();
           ControladorRegistro control=new ControladorRegistro(reg,Cn,3);
        }
        
    }

}
