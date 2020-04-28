package es.alejandrogarrido.homing.ui.usuarios;


public class Usuarios {

    private String id, nombre, usuario, email;
    private Direccion direcc;
    private String tlf, tipo;
    private Rating ratings;

    public Usuarios(String id, String nombre, String usuario, String email, Direccion direcc, String tlf, String tipo, Rating ratings) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.email = email;
        this.direcc = direcc;
        this.tlf = tlf;
        this.tipo = tipo;
        this.ratings = ratings;
    }

    public Usuarios(String nombre, String email, String tlf) {
        this.nombre = nombre;
        this.email = email;
        this.tlf = tlf;
    }

    public Usuarios() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Direccion getDirecc() {
        return direcc;
    }

    public void setDirecc(Direccion direcc) {
        this.direcc = direcc;
    }

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Rating getRatings() {
        return ratings;
    }

    public void setRatings(Rating ratings) {
        this.ratings = ratings;
    }
}
