# Evaluacion Global Integradora - Diseno de Software

**Estudiantes:**

- Lisandro Feudale
- Valentino Pepa
- Tiziano Caré

**Patron elegido:** Observer  
**Lenguaje:** Java 21  
**Repositorio:** https://github.com/tizianocare/mi-proyecto-diseno

## I. Planteamiento y justificacion del problema

La subasta es un proceso en el que varias partes deben enterarse de cambios relevantes: la pantalla de la sala muestra la mejor oferta, la aplicacion de cada postor informa si va ganando o fue superado y el sistema de auditoria registra cada evento.

Con una solucion convencional, la clase `Subasta` tendria que conocer y llamar directamente a clases como la pantalla, las aplicaciones y la bitacora. Esto produciria acoplamiento fuerte: incorporar un nuevo canal exigiriria modificar la subasta, duplicar llamadas a distintos componentes y repetir condiciones segun el tipo de consumidor. Tambien seria dificil quitar una suscripcion sin afectar al resto.

Observer es adecuado porque separa al publicador de sus consumidores. `Subasta` mantiene observadores contra una interfaz comun y publica cambios de estado; cada observador decide como interpretarlos. Una suscripcion puede agregarse o retirarse sin modificar al sujeto, y un nuevo canal puede incorporarse implementando `Observador`.

El escenario es propio del trabajo y diferente de los ejemplos vistos en clase.

## II. Diseno arquitectonico

El diagrama formal se encuentra en `diagrama-clases.puml`.

Roles aplicados:

- **Sujeto:** interfaz `Sujeto`, implementada por `Subasta`.
- **Observer:** interfaz `Observador`, implementada por `PantallaSala`, `AppPostor` y `RegistroAuditoria`.
- **Evento:** `Oferta` transporta una fotografia inmutable del estado.
- **Client:** `Main` crea el sujeto y los observadores y demuestra el flujo.

La visibilidad principal se expresa en el diagrama UML: atributos privados con `-`, metodos publicos con `+` y relaciones de implementacion o composicion.

## III. Implementacion practica

El codigo completo esta en `src/subasta/` y puede ejecutarse por consola. Los pasos son:

```bash
javac -encoding UTF-8 -d out src/subasta/*.java
java -Dfile.encoding=UTF-8 -cp out subasta.Main
```

En Windows puede utilizarse directamente:

```bat
run.bat
```

La clase `Main` realiza las siguientes pruebas:

1. crea una subasta con precio base;
2. suscribe tres tipos de observadores;
3. registra ofertas validas y genera notificaciones automaticas;
4. desuscribe a Lucia;
5. rechaza una oferta inferior;
6. cierra la subasta;
7. muestra la cantidad de notificaciones recibidas.

La salida demostrativa se conserva en `salida-utf8.txt`.

## IV. Analisis critico y principios SOLID

### Beneficios SOLID

- **Responsabilidad Unica:** `Subasta` administra el estado y las suscripciones; `PantallaSala`, `AppPostor` y `RegistroAuditoria` tienen responsabilidades independientes.
- **Abierto/Cerrado:** puede agregarse un observador nuevo sin modificar `Subasta`, siempre que implemente `Observador`.
- **Sustitucion de Liskov:** los observadores concretos pueden sustituir a la abstraccion `Observador` sin alterar al sujeto.
- **Segregacion de Interfaces:** `Observador` contiene solamente `actualizar(Oferta)`, por lo que los observadores no implementan metodos innecesarios.
- **Inversion de Dependencias:** `Subasta` depende de `Observador` y `Oferta`, no de las clases concretas.

### Ventajas

- Desacoplamiento entre productor y consumidores.
- Extensibilidad y posible incorporacion de nuevos canales.
- Una oferta valida genera automaticamente un evento para todos los suscriptores.
- La desuscripcion evita notificaciones posteriores.
- `Oferta` evita que los observadores lean directamente el estado mutable posterior.
- `BigDecimal` evita errores de redondeo monetario.

### Desventajas y tensiones

- Observer puede incrementar la complejidad frente a un sistema con pocas actualizaciones.
- El sujeto puede dejar de ser simple porque administra suscripciones y notificaciones.
- Las referencias activas pueden producir fugas de memoria si un observador no se desuscribe.
- Una notificacion no garantiza que los observadores terminen en el mismo orden.
- Aunque las colecciones tienen una copia segura para iterar, no existe una arquitectura multihilo completa.
- Las excepciones de observadores concretos se capturan en `notificar` para no interrumpir a los demas, pero esto puede ocultar fallos si no existe un registro centralizado.
- La auditoria confirma sus escrituras y conserva una copia defensiva de los registros en memoria.

### Criterio profesional

La solucion aplica Observer de forma apropiada, mantiene un acoplamiento bajo y permite demostrar tanto el rechazo de cambios invalidos como la desuscripcion dinamica. Para un entorno de produccion real convendria agregar cola de eventos, auditoria persistente centralizada y mecanismo multihilo o asincrono segun el volumen de suscriptores.
