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
        
    /*Metodo al que accedemos cuando activamos algun elemento 
      de la interfaz que se encuentra en el ActionListener*/
    @Override
    public void actionPerformed(ActionEvent e){
        //Objeto que referencia al elemento del interfaz que hemos activado
        Object fuente = e.getSource();
        
        //Si activamos sign up (Al controlador le pasamos un 3 pa que sepa que tiene que crear un cliente
        if(fuente==interfaz_login.sign_up){
           IRegistro interfaz_registro = new IRegistro();
           ControladorRegistro controlador_registro=new ControladorRegistro(interfaz_registro,conector,3);
        }
        
        //Si activamos sign in o los JTextFields de usuario o contraseña pulsando enter
        if(fuente==interfaz_login.sign_in || fuente==interfaz_login.pass || fuente==interfaz_login.user){
            //Comprobamos si el usuario existe
            try {
                Usuario user=conector.getUsuario(interfaz_login.user.getText());
                //Si no existe
                if(user.getId()==null){
                    JOptionPane.showMessageDialog(null,"Introduzca un usuario valido.");
                //Si existe
                }else{
                    //Si las contraseñas coinciden
                    if(user.getPass().equals(interfaz_login.pass.getText())){
                        //Si es el administrador crea su interfaz y controlador
                        if(user.getRoll()==0){
                            interfaz_login.setVisible(false);
                            IAdministrador interfaz_administrador=new IAdministrador();
                            ControladorAdministrador controlador_administrador=new 
                                ControladorAdministrador(interfaz_administrador,conector);
                            
                        }
                        //Si es un trabajador crea su interfaz y controlador
                        if(user.getRoll()==1){
                            interfaz_login.setVisible(false);
                            IEmpleado interfaz_empleado=new IEmpleado();
                            ControladorEmpleado controlador_empleado=new 
                                ControladorEmpleado(interfaz_empleado,conector,user);
                        }
                        //Si es un proveedor crea su interfaz y controlador
                        if(user.getRoll()==2){
                            interfaz_login.setVisible(false);
                            IProveedor interfaz_proveedor=new IProveedor();
                            ControladorProveedor controlador_proveedor=new 
                                ControladorProveedor(interfaz_proveedor,conector,user);
                        }
                        //Si es un cliente crea su interfaz y controlador
                        if(user.getRoll()==3){
                            interfaz_login.setVisible(false);
                            ICliente interfaz_cliente=new ICliente();
                            ControladorCliente controlador_cliente=new 
                                ControladorCliente(interfaz_cliente,conector,user);
                        }
                    //Si la contraseña es incorrecta
                    }else{
                        JOptionPane.showMessageDialog(null,"La contraseña es incorrecta.");
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(ControladorLogin.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(null,"Error del sistema.");
            }
        } 
    }
}