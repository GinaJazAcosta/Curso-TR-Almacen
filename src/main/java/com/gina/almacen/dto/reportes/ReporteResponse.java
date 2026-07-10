package com.gina.almacen.dto.reportes;

import java.math.BigDecimal;

public record ReporteResponse(
        Long idSucursal,
        String nombreSucursal,
        BigDecimal totalFacturado,
        Long cantidadProductosVendidos
) {
}
