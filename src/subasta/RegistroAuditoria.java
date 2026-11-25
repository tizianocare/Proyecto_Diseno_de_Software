package subasta;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Observador concreto: bitácora inmutable de todo lo que pasa en la subasta.
 *
 * Es el tercer canal y el más distinto de los otros dos: no muestra nada al
 * público, acumula registros. Sirve para auditoría legal de la casa de
 * subastas y es la prueba de que un mismo evento puede tener consumidores con
 * responsabilidades totalmente diferentes.
 */
public class RegistroAuditoria implements Observador {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final String nombreArchivo;
    private final List<String> registros = new ArrayList<>();

    public RegistroAuditoria(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.isBlank()) {
            throw new IllegalArgumentException("El nombre de archivo de auditoría es obligatorio.");
        }
        this.nombreArchivo = nombreArchivo;
    }

    @Override
    public synchronized void actualizar(Oferta oferta) {
        String linea = String.format("#%03d %s | subasta=%s | postor=%s | monto=%s | estado=%s",
                registros.size() + 1,
                LocalTime.now().format(HORA),
                oferta.getCodigoSubasta(),
                oferta.hayPostor() ? oferta.getPostor() : "—",
                oferta.getMontoFormateado(),
                oferta.getEstado().getDescripcion());

        try {
            Files.writeString(Path.of(nombreArchivo), linea + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo escribir la auditoría en " + nombreArchivo + ".", e);
        }

        registros.add(linea);
        System.out.println("[" + nombreArchivo + "] " + linea);
    }

    /** Copia defensiva: el exterior no puede modificar la bitácora. */
    public synchronized List<String> getRegistros() {
        return List.copyOf(registros);
    }

    public synchronized int getNotificaciones() {
        return registros.size();
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }
}
