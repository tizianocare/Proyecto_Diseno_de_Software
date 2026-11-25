package subasta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Rol "Concrete Subject" del patrón Observer.
 *
 * Es el sujeto concreto: mantiene el estado de interés (mejor oferta y estado
 * de la subasta) y la lista de suscriptores. Cada cambio relevante llama a
 * notificar(), que recorre la lista y avisa a todos.
 *
 * La clase NO conoce a PantallaSala, AppPostor ni RegistroAuditoria: solo ve
 * la interfaz Observador. Esa es la pieza que evita el acoplamiento fuerte.
 */
public class Subasta implements Sujeto {

    private final String codigo;
    private final BigDecimal precioBase;

    /** Estado de interés que los observadores quieren conocer. */
    private BigDecimal ofertaActual;
    private String postorActual;
    private EstadoSubasta estado;

    /** Lista de suscriptores. Privada: nadie de afuera la manipula. */
    private final List<Observador> observadores = new ArrayList<>();

    public Subasta(String codigo, BigDecimal precioBase) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código de subasta es obligatorio.");
        }
        if (precioBase == null || precioBase.signum() <= 0) {
            throw new IllegalArgumentException("El precio base debe ser mayor que cero.");
        }
        this.codigo = codigo;
        this.precioBase = precioBase;
        this.ofertaActual = precioBase; // hasta la primera oferta, rige la base
        this.postorActual = null;
        this.estado = EstadoSubasta.EN_CURSO;
    }

    // ------------------------------------------------------------------
    // Gestión de suscripciones (contrato de Sujeto)
    // ------------------------------------------------------------------

    @Override
    public synchronized void suscribir(Observador observador) {
        if (observador == null) {
            throw new IllegalArgumentException("No se puede suscribir un observador nulo.");
        }
        if (!observadores.contains(observador)) { // evita notificaciones duplicadas
            observadores.add(observador);
        }
    }

    @Override
    public synchronized void desuscribir(Observador observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificar() {
        Oferta oferta = new Oferta(codigo, postorActual, ofertaActual, estado);
        final List<Observador> suscriptores;
        synchronized (this) {
            suscriptores = new ArrayList<>(observadores);
        }

        // Se itera sobre una copia: un observador podría desuscribirse (o
        // suscribir a otro) dentro de su propio actualizar(), y modificar la
        // lista mientras se recorre produciría ConcurrentModificationException.
        for (Observador observador : suscriptores) {
            try {
                observador.actualizar(oferta);
            } catch (RuntimeException e) {
                // Un canal roto no debe abortar la notificación de los demás
                // ni dejar la subasta en un estado inconsistente.
                System.err.println("[Subasta " + codigo + "] Falló un observador ("
                        + observador.getClass().getSimpleName() + "): " + e.getMessage());
            }
        }
    }

    // ------------------------------------------------------------------
    // Lógica de negocio de la subasta
    // ------------------------------------------------------------------

    /**
     * Registra una oferta y notifica a todos los suscriptores.
     *
     * @throws IllegalStateException    si la subasta ya está cerrada.
     * @throws IllegalArgumentException si el postor o el monto no son válidos.
     */
    public void ofertar(String postor, BigDecimal monto) {
        if (estado != EstadoSubasta.EN_CURSO) {
            throw new IllegalStateException("La subasta " + codigo + " ya está " + estado.getDescripcion() + ".");
        }
        if (postor == null || postor.isBlank()) {
            throw new IllegalArgumentException("La oferta debe indicar un postor.");
        }
        if (monto == null || monto.compareTo(ofertaActual) <= 0) {
            throw new IllegalArgumentException("La oferta de " + postor
                    + " debe superar la oferta actual de " + Oferta.formatearMonto(ofertaActual) + ".");
        }

        this.ofertaActual = monto;
        this.postorActual = postor;

        System.out.println("\n>> " + postor + " oferta " + getOfertaActualFormateada() + " en " + codigo);
        notificar(); // único punto de disparo del evento
    }

    /** Cierra la subasta: adjudica al mejor postor o la declara desierta. */
    public void cerrar() {
        if (estado != EstadoSubasta.EN_CURSO) {
            throw new IllegalStateException("La subasta " + codigo + " ya fue cerrada.");
        }
        this.estado = (postorActual == null) ? EstadoSubasta.DESIERTA : EstadoSubasta.ADJUDICADA;

        System.out.println("\n>> Cierre de la subasta " + codigo + " (" + estado.getDescripcion() + ")");
        notificar();
    }

    // ------------------------------------------------------------------
    // Consultas
    // ------------------------------------------------------------------

    public String getCodigo() {
        return codigo;
    }

    public BigDecimal getPrecioBase() {
        return precioBase;
    }

    public BigDecimal getOfertaActual() {
        return ofertaActual;
    }

    public String getOfertaActualFormateada() {
        return Oferta.formatearMonto(ofertaActual);
    }

    public String getPostorActual() {
        return postorActual;
    }

    public EstadoSubasta getEstado() {
        return estado;
    }

    /** Cantidad de suscriptores activos (se usa en la clase de prueba). */
    public int cantidadObservadores() {
        return observadores.size();
    }
}
