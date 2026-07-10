package com.gina.almacen.dto.ventas;

import com.gina.almacen.dto.sucursal.SucursalResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;
@Valid
public record VentaRequest(
        @NotNull(message = "El ID de la sucursal es requerido")
        @Positive(message = "El ID de la sucursal dene der positivo")
        Long idSucursal,

        @NotEmpty(message = "La lista de productos es requerida  no debe estar vacia")
        List<@Valid DetalleVentaRequest> productos
) {
}
