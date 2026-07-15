# Sistema de Gestión de Almacén y Ventas (Spring Boot)

## 📌 Contexto del Proyecto

El sistema actual ya cuenta con los módulos base para la gestión de **Sucursales** y **Productos** (CRUDs básicos). El objetivo de este proyecto es completar el núcleo del negocio: el **Módulo de Ventas**, el **Filtrado Avanzado de Productos**, y un **Módulo de Reportes Financieros**.

Deberás aplicar conceptos de persistencia, transaccionalidad, mappers, DTOs y manejo de lógica de negocio para resolver las necesidades del negocio.

## 🛠️ Requerimientos Completos a Desarrollar

### 1. Filtrado Avanzado de Productos

En la clase `ProductoServiceImpl`, el método `listar` actualmente devuelve todos los productos sin distinción. Debes implementar la lógica necesaria para que los parámetros de búsqueda sean dinámicos:

- **Parámetros:** `nombre` (coincidencia parcial/ignore case), `categoria` (exacto), `precioMin` y `precioMax` (rango de precios).
- **Comportamiento:** Si un parámetro llega nulo o vacío, no debe afectar al resto de los filtros. Debes investigar y aplicar **Queries Derivadas de JPA**, `@Query` (JPQL) o **Specifications** para resolverlo de forma eficiente en la base de datos.

### 2. Módulo de Ventas (Lógica Transaccional y Stock)

Implementar los métodos de la interfaz `VentaService` respetando las siguientes reglas de negocio del mundo real:

- **A. Registro de Ventas (`registrar`):**
    - **Fotografía del Precio:** El precio asignado en `DETALLES_VENTAS.PRECIO_PRODUCTO` debe ser el precio que el producto tiene **en ese mismo instante** en la tabla `PRODUCTOS`. No se debe confiar en el precio que envíe el cliente en el Request.
    - **Control de Inventario:** Al registrar la venta, se debe **restar** la cantidad vendida de la columna `CANTIDAD` en la tabla `PRODUCTOS`.
    - **Validación de Existencias:** Si la cantidad solicitada de un producto es mayor a la disponible en el inventario, el sistema debe detenerse de inmediato y lanzar una excepción de negocio controlada (ej. `IllegalArgumentException`).
    - **Transaccionalidad Atómica:** Si una venta contiene múltiples productos y uno solo de ellos falla por falta de stock, **toda la operación debe cancelarse (Rollback)**. No deben quedar registros parciales en la base de datos.
- **B. Cancelación de Ventas (`cancelar`):**
    - **Cambio de Estado:** El estado de la venta debe pasar a `'CANCELADA'`.
    - **Devolución de Stock:** Se debe **sumar de vuelta** la cantidad de productos vendidos al stock original en la tabla `PRODUCTOS`.
    - **Validación:** Si una venta ya se encuentra en estado `'CANCELADA'`, el sistema debe impedir una nueva cancelación lanzando una excepción.
- **C. Listado de Ventas Separado:**
    - Modificar o segmentar el comportamiento del listado de ventas. El método `listar()` estándar **solo debe retornar las ventas en estado `'REGISTRADA'`**.
    - Deberás crear un endpoint/método adicional exclusivo para consultar el histórico de ventas en estado `'CANCELADA'`.

### 3. Módulo de Reportes (Lógica de Agregación)

Para dar valor agregado al negocio, añade un nuevo endpoint que exponga un reporte económico de rendimiento.

- **Requerimiento:** Diseñar un método que devuelva un listado de DTOs (`ReporteVentasSucursalResponse`) con la siguiente información agregada por cada sucursal:
    - ID de la Sucursal.
    - Nombre de la Sucursal.
    - **Total Facturado:** La sumatoria económica de todas sus ventas activas (Multiplicación de CANTIDAD_PRODUCTO * PRECIO_PRODUCTO de sus respectivos detalles). *No incluir ventas canceladas.*
    - **Cantidad de Productos Vendidos:** El conteo total de piezas/artículos que han salido de esa sucursal.
- **Nota de Evaluación:** Se evaluará críticamente la eficiencia de este método (si se realiza mediante funciones de agregación en la base de datos `SUM/GROUP BY` o si se procesa mediante Java *Streams*).

## 📐 Criterios de Evaluación Técnica

- **Uso correcto de Capas:** Separación limpia entre Controller, Service, Repository, DTOs y Mappers.
- **Manejo de Transacciones:** Uso estratégico de `@Transactional` (distinguiendo lecturas `readOnly = true` de escrituras).
- **Validación de Datos:** Respuestas limpias del servidor ante errores de negocio (evitar que el sistema lance un *Stacktrace* o error 500 genérico al cliente).
- **Legibilidad y Logs:** Inclusión de trazas de registro (`log.info`, `log.error`) que emulen el comportamiento de los servicios ya provistos.
