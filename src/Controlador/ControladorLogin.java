package Controlador;

import java.sql.SQLException;
import Modelo.*;
import Vista.*;
import java.awt.event.*;
import java.util.logging.*;
import javax.swing.JOptionPane;

public class ControladorLogin implements ActionListener{
    //Interfaz login
    private ILogin interfaz_login;
    //Conector a la base de datos
    private Conector conector;
        
    //Constructor
    public ControladorLogin(ILogin i_login, Conector conector){
        //Inicializamos las variables globales
        this.interfaz_login= i_login;
        this.conector=conector;
        //Colocamos la interfaz en el centro de la pantalla
        interfaz_login.setLocationRelativeTo(null);
        //Hacemos visible la interfaz
        interfaz_login.setVisible(true);
        //Añadimos los elementos de la interfaz al ActionListener
        this.interfaz_login.sign_in.addActionListener(this);
        this.interfaz_login.sign_up.addActionListener(this);
        this.interfaz_login.pass.addActionListener(this);
        this.interfaz_login.user.addActionListener(this);          
    }
        
    //Metodo al que accedemos si activamos algun elemento del interfaz que se encuentre en el AcionListener
    @Override
    public void actionPerformed(ActionEvent e){
        //Objeto que referencia al elemento del interfaz que hemos activado
        Object fuente = e.getSource();
        
        //Si activamos sign in o los JTextFields de usuario o contraseña pulsando enter
        if(fuente==interfaz_login.sign_in || fuente==interfaz_login.pass || fuente==interfaz_login.user){
            try {
                Usuario user=conector.getUsuario(interfaz_login.user.getText()); //To change body of generated methods, choose Tools | Templates.
                if(user.getId()==null){
                    JOptionPane.showMessageDialog(null,"Introduzca un usuario valido.");
                }else{
                    if(user.getPass().equals(interfaz_login.pass.getText())){
                        if(user.getRoll()==0){
                            interfaz_login.setVisible(false);
                            IAdministrador admin=new IAdministrador();
                            ControladorAdministrador control=new ControladorAdministrador(admin,conector);
                            
                        }
                        if(user.getRoll()==1){
                            interfaz_login.setVisible(false);
                            IEmpleado emp=new IEmpleado();
                            ControladorEmpleado control=new ControladorEmpleado(emp,conector,user);
                        }
                        if(user.getRoll()==2){
                            interfaz_login.setVisible(false);
                            IProveedor pro=new IProveedor();
                            ControladorProveedor control=new ControladorProveedor(pro,conector,user);
                        }
                        if(user.getRoll()==3){
                            interfaz_login.setVisible(false);
                            ICliente cli=new ICliente();
                            ControladorCliente control=new ControladorCliente(cli,conector,user);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,"La contraseña es incorrecta.");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(ControladorLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        if(fuente==interfaz_login.sign_up){
           IRegistro reg = new IRegistro();
           ControladorRegistro control=new ControladorRegistro(reg,conector,3);
        }
        
    }

}
