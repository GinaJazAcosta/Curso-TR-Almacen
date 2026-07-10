package com.gina.almacen.dto.ventas;

import com.gina.almacen.dto.sucursal.SucursalResponse;
import java.math.BigDecimal;
import java.util.List;

public record VentaResponse(
        Long id,
        String fecha, //LocalDate
        String estado, //EstadoVenta
        SucursalResponse sucursal, //Sucursal
        List<DetalleVentaResponse> detalles, //List<DetalleVenta>
        BigDecimal total
) { }
