package es.alejandrogarrido.homing.ui.usuarios;

import java.util.List;

public class Rating {

    private List notas;

    public Rating(List notas) {
        this.notas = notas;
    }

    public Rating() {
    }

    public List getNotas() {
        return notas;
    }

    public void setNotas(List notas) {
        this.notas = notas;
    }
}
