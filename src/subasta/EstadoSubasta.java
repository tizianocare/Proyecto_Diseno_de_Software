package subasta;

/**
 * Estados posibles del ciclo de vida de una subasta.
 * Se usa un enum, y no textos sueltos, para que los observadores decidan
 * con un conjunto cerrado de valores y el compilador controle la exhaustividad.
 */
public enum EstadoSubasta {

    EN_CURSO("en curso"),
    ADJUDICADA("adjudicada"),
    DESIERTA("desierta");

    private final String descripcion;

    EstadoSubasta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
