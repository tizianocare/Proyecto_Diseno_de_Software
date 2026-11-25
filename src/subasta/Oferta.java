package subasta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

/**
 * Evento que viaja del sujeto a los observadores.
 *
 * Es un objeto inmutable (todos sus atributos son final y no se exponen
 * setters): una vez emitido, ningún observador puede alterar el estado que
 * reciben los demás. Además, los observadores dependen de esta clase de
 * dominio y no de Subasta, lo que reduce el acoplamiento.
 *
 * El dinero se representa con BigDecimal y no con double, para no arrastrar
 * errores de redondeo en los importes.
 */
public final class Oferta {

    private static final NumberFormat MONEDA = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-AR"));

    private final String codigoSubasta;
    private final String postor;
    private final BigDecimal monto;
    private final EstadoSubasta estado;

    public Oferta(String codigoSubasta, String postor, BigDecimal monto, EstadoSubasta estado) {
        this.codigoSubasta = Objects.requireNonNull(codigoSubasta, "codigoSubasta");
        this.monto = Objects.requireNonNull(monto, "monto");
        this.estado = Objects.requireNonNull(estado, "estado");
        this.postor = postor; // puede ser null: subasta sin ofertas
    }

    public String getCodigoSubasta() {
        return codigoSubasta;
    }

    /** @return nombre del mejor postor, o null si todavía no hay ofertas. */
    public String getPostor() {
        return postor;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public EstadoSubasta getEstado() {
        return estado;
    }

    public boolean hayPostor() {
        return postor != null;
    }

    /** Importe listo para mostrar ($150.000,00), para no repetir formato en cada observador. */
    public String getMontoFormateado() {
        return formatearMonto(monto);
    }

    /**
     * Formatea un importe con separador de miles y dos decimales.
     * Es estático porque el sujeto también lo necesita antes de emitir la oferta.
     */
    public static String formatearMonto(BigDecimal monto) {
        BigDecimal recortado = monto.setScale(2, RoundingMode.HALF_UP);
        synchronized (MONEDA) { // NumberFormat no es thread-safe
            return "$" + MONEDA.format(recortado);
        }
    }

    @Override
    public String toString() {
        return "Oferta{subasta=" + codigoSubasta
                + ", postor=" + (postor == null ? "sin ofertas" : postor)
                + ", monto=" + getMontoFormateado()
                + ", estado=" + estado + "}";
    }
}
