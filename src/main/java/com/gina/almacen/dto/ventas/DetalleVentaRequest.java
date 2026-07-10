package com.gina.almacen.dto.ventas;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DetalleVentaRequest(
        @NotNull(message = "El id del producto es requerido")
        @Positive(message = "El ID del producto debe ser positivo")
        Long idProducto,

        @NotNull(message = "La cantidad del producto es requerida")
        @Positive(message = "La cantidad el producto debe ser requerida")
        Integer cantidadProducto
) { }
