package com.gina.almacen.specifications;

import com.gina.almacen.entities.Producto;
import com.gina.almacen.enums.Categoria;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductoSpecification {

    //EJEMPLO DE SPECIFICACION *NO SE USA*
    public Specification<Producto> porNombreContiene(String nombre) {
        return (root, query, cb) ->
            (nombre == null || nombre.isBlank())
                 ? null
                 : cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%");
    }

    public static Specification<Producto> nombreContiene(String nombre) {
        return (root, query, cb) ->
            (nombre == null || nombre.isBlank())
                ? null
                : cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%");
    }

    public static Specification<Producto> categoriaIgual(Categoria categoria) {
        return (root, query, cb) ->
            (categoria == null)
                ? null
                : cb.equal(root.get("categoria"), categoria);
    }

    public static Specification<Producto> precioMayorOIgual(BigDecimal precioMin) {
        return (root, query, cb) ->
            (precioMin == null)
                ? null
                : cb.greaterThanOrEqualTo(root.get("precio"), precioMin);
    }

    public static Specification<Producto> precioMenorOIgual(BigDecimal precioMax) {
        return (root, query, cb) ->
            (precioMax == null)
                ? null
                : cb.lessThanOrEqualTo(root.get("precio"), precioMax);
    }
}
