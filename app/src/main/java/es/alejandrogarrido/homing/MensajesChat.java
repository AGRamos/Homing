package es.alejandrogarrido.homing;

import java.util.Date;

public class MensajesChat {

    private String mensajesTexto;
    private String mensajesUsuario;
    private long mensajesTiempo;


    public MensajesChat(String mensajesTexto, String mensajesUsuario) {
        this.mensajesTexto = mensajesTexto;
        this.mensajesUsuario = mensajesUsuario;
        // Inicializamos el tiempo
        mensajesTiempo = new Date().getTime();
    }

    public MensajesChat() {
    }

    public String getMensajesTexto() {
        return mensajesTexto;
    }

    public void setMensajesTexto(String mensajesTexto) {
        this.mensajesTexto = mensajesTexto;
    }

    public String getMensajesUsuario() {
        return mensajesUsuario;
    }

    public void setMensajesUsuario(String mensajesUsuario) {
        this.mensajesUsuario = mensajesUsuario;
    }

    public long getMensajesTiempo() {
        return mensajesTiempo;
    }

    public void setMensajesTiempo(long mensajesTiempo) {
        this.mensajesTiempo = mensajesTiempo;
    }
}
