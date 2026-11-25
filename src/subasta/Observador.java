package subasta;

/**
 * Rol "Observer" del patrón: contrato común de todo suscriptor.
 *
 * La interfaz declara un único método (actualizar) para no obligar a los
 * suscriptores a implementar comportamiento que no necesitan (ISP).
 *
 * El parámetro es un objeto Oferta, no la subasta: así el observador no
 * depende del sujeto concreto y recibe una foto del estado en el momento
 * del evento, sin riesgo de leer un estado posterior ya modificado.
 */
public interface Observador {

    /**
     * Se invoca cada vez que el sujeto cambia de estado y notifica.
     *
     * @param oferta evento inmutable con el estado de la subasta.
     */
    void actualizar(Oferta oferta);
}
