package es.alejandrogarrido.homing;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.Date;

public class MensajesChat {

    public String chatId = "";
    public String mensajeId= "";
    public String emisorId= "";
    public String mensaje= "";
    public String mensajeTipo= "";
    public String mediaUrl= "";
    public String mediaThumbUrl= "";
    public long timestamp;



    @Exclude
    public String localMediaUrl = "";

    @Exclude
    public int id = 0;

    @Exclude
    public long blockTime = 0;

    public java.util.Map<String, String>getTimestamp(){return ServerValue.TIMESTAMP;}
    public void setTimestamp(long timestamp){this.timestamp=timestamp;}

    @Exclude
    public long getTimestampLong(){return timestamp;}

    public MensajesChat() {
    }
}
