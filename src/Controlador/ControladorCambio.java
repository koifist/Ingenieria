package Controlador;

import Modelo.*;
import Vista.ICambio;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.logging.*;

public class ControladorCambio implements ActionListener{
    //Usuario que va a cambiar su contrasena
    private Usuario usuario;
    //Conector a la base de datos
    private Conector conector;
    //Interfaz de cambio de contrasena
    private ICambio interfaz_cambio;
    
    //Constructor
    public ControladorCambio(Usuario usuario, ICambio interfaz_cambio, Conector conector){
        //Inicializamos las variables globales
        this.usuario=usuario;
        this.interfaz_cambio=interfaz_cambio;
        this.conector=conector;
        //Colocamos la interfaz en el centro de la pantalla
        interfaz_cambio.setLocationRelativeTo(null);
        //Hacemos la interfaz visible
        interfaz_cambio.setVisible(true);
        //Añadimos el boton de cambiar la contrasena al ActionListener
        interfaz_cambio.cambiar_datos.addActionListener(this);
    }
    
    /*Metodo al que accedemos cuando activamos algun elemento 
      de la interfaz que se encuentra en el ActionListener*/
    @Override
    public void actionPerformed(ActionEvent e) {
        //Si la contrasena actual es incorrecta  
        if(!interfaz_cambio.pass_actual.getText().equals(usuario.getPass())){
        interfaz_cambio.error.setText("Contraseña actual incorrecta.");
        //Si la contrasena actual es correcta
        }else{
            //Si la nueva contrasena no coincide al repetirla
            if(!interfaz_cambio.pass_nueva.getText().equals(interfaz_cambio.pass_nueva_confirm.getText())){
                interfaz_cambio.error.setText("La nueva contraseña no coincide.");
            //Si coinciden
            }else{
                usuario.setPass(interfaz_cambio.pass_nueva.getText());
                //Actualizamos la contrasena en la base de datos
                try{
                    conector.updateUsuario(usuario);
                    //Cerramos la interfaz
                    interfaz_cambio.dispose();
                }catch (SQLException ex) {
                    Logger.getLogger(ControladorCambio.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }
}