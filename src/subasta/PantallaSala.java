package subasta;

/**
 * Observador concreto: pantalla grande de la sala de subastas.
 *
 * Muestra el estado a todo el público presente. No sabe de dónde viene el
 * evento ni quién más está suscripto: solo recibe una Oferta y la dibuja.
 */
public class PantallaSala implements Observador {

    private final String nombrePantalla;
    private int notificaciones = 0;

    public PantallaSala(String nombrePantalla) {
        this.nombrePantalla = nombrePantalla;
    }

    @Override
    public void actualizar(Oferta oferta) {
        notificaciones++;

        // Switch sobre el enum: el compilador obliga a cubrir todos los estados.
        String texto = switch (oferta.getEstado()) {
            case EN_CURSO -> oferta.hayPostor()
                    ? "Mejor oferta: " + oferta.getMontoFormateado() + " por " + oferta.getPostor()
                    : "Sin ofertas. Base: " + oferta.getMontoFormateado();
            case ADJUDICADA -> "ADJUDICADA a " + oferta.getPostor() + " por " + oferta.getMontoFormateado();
            case DESIERTA -> "SUBASTA DESIERTA: no se recibieron ofertas";
        };

        System.out.println("[" + nombrePantalla + "] " + texto);
    }

    public int getNotificaciones() {
        return notificaciones;
    }

    public String getNombrePantalla() {
        return nombrePantalla;
    }
}
