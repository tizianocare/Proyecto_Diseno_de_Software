# Presentacion oral EGI - Observer

## 1. Problema

- Subasta en vivo.
- Tres tipos de canales necesitan enterarse de los cambios.
- Una solucion sin patron acopla a la subasta con pantalla, apps y auditoria.

## 2. Patron elegido

- Sujeto: `Sujeto` y `Subasta`.
- Observadores: `Observador`, `PantallaSala`, `AppPostor` y `RegistroAuditoria`.
- Evento: `Oferta`.
- Cliente: `Main`.

## 3. Demostracion

- Suscripcion de cuatro observadores.
- Oferta de Lucia.
- Oferta de Bruno.
- Desuscripcion de Lucia.
- Nueva oferta y rechazo de oferta invalida.
- Cierre adjudicado.
- Resumen de notificaciones.

## 4. Relacion con SOLID

- OCP: incorporar observadores sin modificar el sujeto.
- SRP: responsabilidades separadas.
- DIP: el sujeto depende de interfaces.
- ISP: la interfaz Observer contiene una sola operacion.

## 5. Ventajas y desventajas

- Ventajas: bajo acoplamiento, extensibilidad y notificacion automatica.
- Desventajas: complejidad adicional, fugas por suscripciones activas y desafios en multihilo.

## 6. Conclusión

Observer resuelve el problema de informar a multiples componentes sin que `Subasta` conozca sus implementaciones concretas.
