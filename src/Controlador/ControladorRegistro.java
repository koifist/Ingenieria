package Controlador;

import Modelo.*;
import Vista.IRegistro;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.logging.*;
import javax.swing.JOptionPane;

class ControladorRegistro implements ActionListener{
    //Roll del usuario que vamos a crear
    private int roll_user;
    //Interfaz de registro
    private IRegistro interfaz_registro;
    //Conector a la base de datos
    private Conector conector;
    //Variable que indica si todos los campos del registro se han rellenado correctamente
    private boolean correcto;

    //Constructor
    ControladorRegistro(IRegistro i_registro, Conector conector, int roll) {
        //Inicializamos variables globales
        this.roll_user=roll;
        this.interfaz_registro=i_registro;
        this.conector=conector;
        //Colocamos la interfaz en el centro de la pantalla
        interfaz_registro.setLocationRelativeTo(null);
        //Mostramos la interfaz
        interfaz_registro.setVisible(true);
        //Añadimos el boton sign_up al ActionListener para poder leer cuando lo presionamos
        interfaz_registro.sign_up.addActionListener(this);
        ocultarErrores();
        //Si vamos a dar de alta a la empresa el Label de DNI lo cambiamos a CIF
        if(roll_user==2) interfaz_registro.DNI_label.setText("CIF");     
    }
    
    //Ocultamos todos los asteriscos de errores en la introduzccion de valores
    private void ocultarErrores(){
        interfaz_registro.user_error.setVisible(false);
        interfaz_registro.pass_error.setVisible(false);
        interfaz_registro.rpass_error.setVisible(false);
        interfaz_registro.name_error.setVisible(false);
        interfaz_registro.DNI_error.setVisible(false);
        interfaz_registro.address_error.setVisible(false);
    }
    
    //Metodo al que se llama cuando interactuas con algun objeto de la interfaz que se encuentra en el ActionListener
    public void actionPerformed(ActionEvent e) {
        //Inicializamos la variable que indica si estan correctos todos los campos de registro
        correcto=true;
        ocultarErrores();
        
        //Si no introduces un usuario o es menor de 5 caracteres o mayor de 15
        if(interfaz_registro.user.getText().isEmpty() || interfaz_registro.user.getText().length()<5 
                || interfaz_registro.user.getText().length()>15){
            //Ventana emergente con mensaje de error
            JOptionPane.showMessageDialog(null,"Introduzca un usuario que contenga entre 5 y 15 caracteres");
            interfaz_registro.user_error.setVisible(true);
            correcto=false;
        }
       
        //Si no introduces una contraseña o es menor de 5 caracteres o mayor de 10
        if(interfaz_registro.pass.getText().isEmpty() || interfaz_registro.pass.getText().length()<5 
                || interfaz_registro.pass.getText().length()>10 ){
            //Ventana emergente con mensaje de error
            JOptionPane.showMessageDialog(null,"Introduzca una contraseña que contenga entre 5 y 10 caracteres");
            interfaz_registro.pass_error.setVisible(true);
            correcto=false;
       }
       
        //Si no has introducido un nombre
        if(interfaz_registro.name.getText().isEmpty()){
            //Ventana emergente con mensaje de error
            JOptionPane.showMessageDialog(null,"Introduzca su nombre y apellidos");
            interfaz_registro.name_error.setVisible(true);
            correcto=false;
        }
        
        //Si no has introducido el DNI
        if(interfaz_registro.DNI.getText().isEmpty() || interfaz_registro.DNI.getText().length()!=9){
            if(roll_user==2){
                //Ventana emergente con mensaje de error para proveedor
                JOptionPane.showMessageDialog(null,"Introduzca el CIF con el formato V8242066B");
            }else{
                //Ventana emergente con mensaje de error para empleado y cliente
                JOptionPane.showMessageDialog(null,"Introduzca el DNI con el formato 25535449K");
    
            }
            correcto=false;
            interfaz_registro.DNI_error.setVisible(true);
        }
       
        //Si no has introducido direccion o es menor de 5 caracteres
        if(interfaz_registro.address.getText().isEmpty() || interfaz_registro.address.getText().length()<5){
            //Ventana emergente con mensaje de error
            JOptionPane.showMessageDialog(null,"Introduzca una direccion valida.");
            interfaz_registro.address_error.setVisible(true);
            correcto=false;
        }
        
        //Si todos los campos se han introducido correctamente
        if(correcto==true){  
            try {
                //Inicializamos un usuario con el mismo id que se ha introducido el nuevo para ver si ya existe
                Usuario usuario=conector.getUsuario(interfaz_registro.user.getText());
                //Si es null significa que no existe y procedemos a crearlo
                if(usuario.id == null){
                    //Si las contraseñas coinciden creamos el user
                    if(interfaz_registro.rpass.getText().equals(interfaz_registro.pass.getText())){
                            usuario.setId(interfaz_registro.user.getText());
                            usuario.setPass(interfaz_registro.pass.getText());
                            usuario.setNombre(interfaz_registro.name.getText());
                            usuario.setDNI(interfaz_registro.DNI.getText());
                            usuario.setRoll(this.roll_user);
                            usuario.setDireccion(interfaz_registro.address.getText());
                            //Creamos el usuario
                            conector.createUsuario(usuario);
                            //Cerramos la interfaz sin salir del programa
                            interfaz_registro.dispose();
                    }else{
                        //Ventana emergente con mensaje de error
                        JOptionPane.showMessageDialog(null,"Las contraseñas no coinciden.");
                        interfaz_registro.pass_error.setVisible(true);
                        interfaz_registro.rpass_error.setVisible(true);
                    }
                }else{
                    //Ventana emergente con mensaje de error
                    JOptionPane.showMessageDialog(null,"El usuario que ha introducido ya existe.");
                    interfaz_registro.user_error.setVisible(true);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ControladorRegistro.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
}


    
    

