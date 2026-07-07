package com.gina.almacen.dto.producto;

import java.math.BigDecimal;

public record ProductoResponse(
        Long id,
        String nombre,
        String categoria,
        BigDecimal precio,
        Integer cantidad
) {}
