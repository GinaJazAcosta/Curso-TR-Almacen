package com.gina.almacen.services.productos;

import com.gina.almacen.dto.producto.ProductoRequest;
import com.gina.almacen.dto.producto.ProductoResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoService {
        List<ProductoResponse> listar(
                String nombre, String categoria,
                BigDecimal precioMin, BigDecimal precioMax
        );
        ProductoResponse obtenerPorId(Long id);
        ProductoResponse registrar(ProductoRequest request);
        ProductoResponse actualizar(ProductoRequest request, Long id);
        void eliminar(Long id);
}
