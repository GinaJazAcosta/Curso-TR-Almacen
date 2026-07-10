package com.gina.almacen.enums;

import com.gina.almacen.exceptions.RecursoNoEncontradoException;
import com.gina.almacen.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public enum EstadoVenta {
    REGISTRADA(1L,"Registrada"),
    CANCELADA(0L, "Cancelada");

    private final Long codigo;
    private final String descripcion;

    public static  EstadoVenta obtenerEstadoVentaPorDescripcion(String descripcion){
        StringCustomUtils.validarNoVacio(descripcion, "La descripción es requerida");
        String descripciónNormalizada = StringCustomUtils.quitarAcentos(descripcion.trim());
        for (EstadoVenta estadoVenta : values()){
            if (StringCustomUtils.quitarAcentos(estadoVenta.descripcion).equalsIgnoreCase(descripciónNormalizada))
                return estadoVenta;
        }
        throw new RecursoNoEncontradoException("No existe una categoria con la descripción: " + descripcion);
    }

    public static  EstadoVenta obtenerEstadoVentaPorCodigo(Long codigo){
        for (EstadoVenta estadoVenta : values()){
            if (Objects.equals(estadoVenta.codigo, codigo))
                return estadoVenta;
        }
        throw new RecursoNoEncontradoException("No existe una venta con el codigo: " + codigo);
    }
}
