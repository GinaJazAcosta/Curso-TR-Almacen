package com.gina.almacen.dto;

public record CustomErrorResponse(
        int codigo,
        String mensaje
) {
}
