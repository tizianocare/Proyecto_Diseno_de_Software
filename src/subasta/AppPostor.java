package subasta;

/**
 * Observador concreto: la app móvil de un postor.
 *
 * Cada postor tiene su propia instancia, así que el mismo evento se
 * interpreta distinto según el usuario: al que va ganando se le avisa que
 * sigue al frente y al resto que lo superaron. Esa personalización vive
 * dentro del observador, no en el sujeto.
 */
public class AppPostor implements Observador {

    private final String usuario;
    private int notificaciones = 0;

    public AppPostor(String usuario) {
        if (usuario == null || usuario.isBlank()) {
            throw new IllegalArgumentException("El usuario es obligatorio.");
        }
        this.usuario = usuario;
    }

    @Override
    public void actualizar(Oferta oferta) {
        notificaciones++;

        String mensaje;
        if (oferta.getEstado() == EstadoSubasta.ADJUDICADA) {
            mensaje = usuario.equals(oferta.getPostor())
                    ? "¡Ganaste la subasta! Te quedás con el lote por " + oferta.getMontoFormateado() + "."
                    : "La subasta se adjudicó a " + oferta.getPostor() + " por " + oferta.getMontoFormateado() + ".";
        } else if (oferta.getEstado() == EstadoSubasta.DESIERTA) {
            mensaje = "La subasta quedó desierta: nadie ofertó.";
        } else if (!oferta.hayPostor()) {
            mensaje = "La subasta abrió con base " + oferta.getMontoFormateado() + ".";
        } else if (usuario.equals(oferta.getPostor())) {
            mensaje = "¡Vas ganando! Tu oferta de " + oferta.getMontoFormateado() + " está al frente.";
        } else {
            mensaje = "Te superaron: " + oferta.getPostor() + " ofertó " + oferta.getMontoFormateado() + ".";
        }

        System.out.println("[App de " + usuario + "] " + mensaje);
    }

    public String getUsuario() {
        return usuario;
    }

    public int getNotificaciones() {
        return notificaciones;
    }
}
