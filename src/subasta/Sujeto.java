package subasta;

/**
 * Rol "Subject" (publicador) del patrón: define el mecanismo de suscripción.
 *
 * Cualquier clase que quiera ser observada implementa esta interfaz, y el
 * cliente puede trabajar contra ella sin conocer el sujeto concreto (DIP).
 */
public interface Sujeto {

    /** Registra un observador para que reciba las próximas notificaciones. */
    void suscribir(Observador observador);

    /** Quita un observador: deja de recibir notificaciones. */
    void desuscribir(Observador observador);

    /** Recorre los observadores registrados y les entrega el evento actual. */
    void notificar();
}
