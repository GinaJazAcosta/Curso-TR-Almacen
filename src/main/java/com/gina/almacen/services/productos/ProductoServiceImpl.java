package com.gina.almacen.services.productos;

import com.gina.almacen.dto.producto.ProductoRequest;
import com.gina.almacen.dto.producto.ProductoResponse;
import com.gina.almacen.entities.Producto;
import com.gina.almacen.enums.Categoria;
import com.gina.almacen.exceptions.RecursoNoEncontradoException;
import com.gina.almacen.mappers.ProductoMapper;
import com.gina.almacen.repositories.ProductoReposirtory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor // @RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductoServiceImpl implements ProductoService{

    private final ProductoReposirtory productoReposirtory;
    private final ProductoMapper productoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar() {
        log.info("Listando todos los productos");
        return productoReposirtory.findAll().stream()
                .map(productoMapper::entidadAResponse).toList();
    }

    @Override
    public ProductoResponse obtenerPorId(Long id) {
        return productoMapper.entidadAResponse(obtenerProductoExcepcion(id));
    }

    @Override
    public ProductoResponse registrar(ProductoRequest request) {
        log.info("Registrando producto...");
        Categoria categoria = Categoria.obtenerCategoriaPorDescripcion(request.categoria());
        Producto producto = productoMapper.requestAEntidad(request, categoria);
        productoReposirtory.save(producto);
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
        productoReposirtory.save(producto); //Puede o no estar por el @transactional
        // al colocar @transactional a la clase esta hace un commit o rollback a todos los metodos implementados de la interfaz
        //tienen que ser publicos y tener @Override
        log.info("Producto con id: {} actualizado", id);
        return productoMapper.entidadAResponse(producto);
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = obtenerProductoExcepcion(id);
        productoReposirtory.delete(producto);
        log.info("Producto con id: {} eliminado", id);
    }

    private Producto obtenerProductoExcepcion(Long id){
        log.info("Buscando producto por id: {}", id);
        return productoReposirtory.findById(id).orElseThrow(
                ()-> new RecursoNoEncontradoException("Producto no encontrado " + id)
        );
    }
}
