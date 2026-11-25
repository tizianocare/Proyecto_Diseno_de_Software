package subasta;

import java.math.BigDecimal;

/**
 * Clase de prueba (rol Client del patrón).
 *
 * Simula el flujo real de una subasta en vivo:
 *   1. se arma la subasta y se registran los canales interesados;
 *   2. cada oferta cambia el estado y dispara una notificación automática;
 *   3. un postor se retira: deja de recibir avisos sin tocar la subasta;
 *   4. una oferta inválida es rechazada y no se notifica;
 *   5. el cierre notifica el resultado a todos los suscriptores activos.
 *
 * El cliente es el único lugar del programa donde se usan las clases
 * concretas (new PantallaSala, new AppPostor...). La subasta nunca las nombra.
 */
public class Main {

    public static void main(String[] args) {
        separador("SubastaYa · demostración del patrón Observer");

        // --- 1. Se arma la subasta -------------------------------------
        Subasta subasta = new Subasta("SUB-1042", new BigDecimal("140000"));
        System.out.println("Lote: reloj de colección de 1948 · Subasta " + subasta.getCodigo()
                + " · base " + subasta.getOfertaActualFormateada());

        // --- 2. Se registran los canales interesados --------------------
        PantallaSala pantalla = new PantallaSala("Pantalla sala principal");
        RegistroAuditoria auditoria = new RegistroAuditoria("auditoria-2026.log");
        AppPostor appLucia = new AppPostor("Lucía");
        AppPostor appBruno = new AppPostor("Bruno");

        subasta.suscribir(pantalla);
        subasta.suscribir(auditoria);
        subasta.suscribir(appLucia);
        subasta.suscribir(appBruno);

        separador("Suscriptores registrados: " + subasta.cantidadObservadores()
                + " | canales de 3 tipos distintos");

        // --- 3. Primeras ofertas: la notificación es automática ---------
        subasta.ofertar("Lucía", new BigDecimal("150000"));
        subasta.ofertar("Bruno", new BigDecimal("165000"));

        // --- 4. Lucía se retira: desuscribir corta los avisos -----------
        subasta.desuscribir(appLucia);
        separador("Lucía se retira de la subasta · suscriptores activos: "
                + subasta.cantidadObservadores());

        subasta.ofertar("Bruno", new BigDecimal("180000"));

        // --- 5. Oferta inválida: se rechaza y NO se notifica ------------
        separador("Bruno intenta ofertar por debajo de la oferta actual");
        try {
            subasta.ofertar("Bruno", new BigDecimal("100000"));
        } catch (IllegalArgumentException e) {
            System.out.println("Oferta rechazada: " + e.getMessage());
            System.out.println("(no se notificó a ningún observador)");
        }

        // --- 6. Cierre: se notifica el resultado ------------------------
        subasta.cerrar();

        // --- 7. Resumen: quién recibió cuántas notificaciones -----------
        separador("Resumen");
        System.out.println("Pantalla de sala .... " + pantalla.getNotificaciones() + " notificaciones");
        System.out.println("Auditoría ........... " + auditoria.getNotificaciones() + " notificaciones");
        System.out.println("App de Lucía ........ " + appLucia.getNotificaciones()
                + " notificaciones (se desuscribió a mitad de la subasta)");
        System.out.println("App de Bruno ........ " + appBruno.getNotificaciones() + " notificaciones");
        System.out.println("Resultado ........... " + subasta.getEstado().getDescripcion()
                + " · " + subasta.getPostorActual() + " · " + subasta.getOfertaActualFormateada());
    }

    private static void separador(String titulo) {
        System.out.println("\n============================================================");
        System.out.println(titulo);
        System.out.println("============================================================");
    }
}
