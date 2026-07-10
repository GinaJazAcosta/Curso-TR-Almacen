package com.gina.almacen.services.productos;

import com.gina.almacen.dto.producto.ProductoRequest;
import com.gina.almacen.dto.producto.ProductoResponse;
import com.gina.almacen.entities.Producto;
import com.gina.almacen.enums.Categoria;
import com.gina.almacen.exceptions.RecursoNoEncontradoException;
import com.gina.almacen.mappers.ProductoMapper;
import com.gina.almacen.repositories.ProductoRepository;
import com.gina.almacen.specifications.ProductoSpecification;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor // @RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductoServiceImpl implements ProductoService{

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(String nombre, String categoria, BigDecimal precioMin, BigDecimal precioMax){
        //Retorna todo sin distincion
        //return productoReposirtory.findAll().stream().map(productoMapper::entidadAResponse).toList();
        log.info("Listando productos con filtros: nombre={}, categoria={}, precioMin={}, precioMax={}",
                nombre, categoria, precioMin, precioMax);

        validarRangoPrecios(precioMin, precioMax);

        Categoria categoriaEnum = null;
        if (categoria != null && !categoria.isBlank()) {
            categoriaEnum = Categoria.obtenerCategoriaPorDescripcion(categoria);
        }

        return productoRepository.findAll(
                (ProductoSpecification.nombreContiene(nombre))
                .and(ProductoSpecification.categoriaIgual(categoriaEnum))
                .and(ProductoSpecification.precioMayorOIgual(precioMin))
                .and(ProductoSpecification.precioMenorOIgual(precioMax)))
            .stream()
            .map(productoMapper::entidadAResponse)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return productoMapper.entidadAResponse(obtenerProductoExcepcion(id));
    }

    @Override
    public ProductoResponse registrar(ProductoRequest request) {
        log.info("Registrando producto...");
        Categoria categoria = Categoria.obtenerCategoriaPorDescripcion(request.categoria());
        Producto producto = productoMapper.requestAEntidad(request, categoria);
        productoRepository.save(producto);
        log.info("Nuevo producto {} registrado", producto.getNombre());
        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public ProductoResponse actualizar(ProductoRequest request, Long id) {
        Producto producto = obtenerProductoExcepcion(id);
        Categoria categoria = Categoria.obtenerCategoriaPorDescripcion(request.categoria());
        log.info("Actualizando producto con id: {}", id);
        producto.actualizar(
                request.nombre(),
                categoria,
                request.precio(),
                request.cantidad()
        );
        productoRepository.save(producto); //Puede o no estar por el @transactional
        // al colocar @transactional a la clase esta hace un commit o rollback a todos los metodos implementados de la interfaz
        //tienen que ser publicos y tener @Override
        log.info("Producto con id: {} actualizado", id);
        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = obtenerProductoExcepcion(id);
        productoRepository.delete(producto);
        log.info("Producto con id: {} eliminado", id);
    }

    private Producto obtenerProductoExcepcion(Long id){
        log.info("Buscando producto por id: {}", id);
        return productoRepository.findById(id).orElseThrow(
                ()-> new RecursoNoEncontradoException("Producto no encontrado " + id)
        );
    }

    private void validarRangoPrecios(BigDecimal precioMin, BigDecimal precioMax) {
        if (precioMin != null && precioMin.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("El precio mínimo no puede ser negativo");
        if (precioMax != null && precioMax.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("El precio máximo no puede ser negativo");
        if (precioMin != null && precioMax != null && precioMin.compareTo(precioMax) > 0)
            throw new IllegalArgumentException("El precio mínimo no puede ser mayor al precio máximo");
    }
}
