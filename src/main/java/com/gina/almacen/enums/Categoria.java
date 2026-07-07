package com.gina.almacen.enums;

import com.gina.almacen.exceptions.RecursoNoEncontradoException;
import com.gina.almacen.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Categoria {

    ALIMENTO("Alimento"),
    HIGIENE("Higiene"),
    JUGUETE("Juguete"),
    ELECTRONICA("Electrónica"),
    ROPA("Ropa"),
    ACCESORIO("Accesorio"),
    FARMACIA("Farmacia");

    private final String descripcion;

    public static  Categoria obtenerCategoriaPorDescripcion(String descripcion){
        StringCustomUtils.validarNoVacio(descripcion, "La descripción es requerida");
        String descripciónNormalizada = StringCustomUtils.quitarAcentos(descripcion.trim());
        for (Categoria categoria : values()){
            if (StringCustomUtils.quitarAcentos(categoria.descripcion).equalsIgnoreCase(descripciónNormalizada))
                return categoria;
        }
        throw new RecursoNoEncontradoException("No existe una categoria con la descripción: " + descripcion);
    }
}
