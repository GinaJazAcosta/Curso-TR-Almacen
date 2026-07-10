package com.gina.almacen.services.ventas;

import com.gina.almacen.dto.ventas.DetalleVentaRequest;
import com.gina.almacen.dto.ventas.VentaRequest;
import com.gina.almacen.dto.ventas.VentaResponse;
import com.gina.almacen.entities.DetalleVenta;
import com.gina.almacen.entities.Producto;
import com.gina.almacen.entities.Sucursal;
import com.gina.almacen.entities.Venta;
import com.gina.almacen.enums.EstadoVenta;
import com.gina.almacen.exceptions.RecursoNoEncontradoException;
import com.gina.almacen.mappers.DetalleVentaMapper;
import com.gina.almacen.mappers.VentaMapper;
import com.gina.almacen.repositories.ProductoRepository;
import com.gina.almacen.repositories.SucursalRepository;
import com.gina.almacen.repositories.VentaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;

    private final VentaMapper ventaMapper;
    private final DetalleVentaMapper detalleVentaMapper;


    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarActivas() {
        log.info("Listando ventas activas");
        return ventaRepository.findByEstadoVenta(EstadoVenta.REGISTRADA).stream()
                .map(ventaMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> listarCanceladas() {
        log.info("Listando ventas canceladas");
        return ventaRepository.findByEstadoVenta(EstadoVenta.CANCELADA).stream()
                .map(ventaMapper::entidadAResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponse obtenerPorIdActiva(Long id) {
        log.info("Buscando venta activa con id: {}", id);
        Venta venta = ventaRepository.findByIdAndEstadoVenta(id, EstadoVenta.REGISTRADA)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se encontró una venta activa con id: " + id));
        return ventaMapper.entidadAResponse(venta);
    }

    @Override
    public VentaResponse registrar(VentaRequest request) {
        log.info("Registrando venta para sucursal con id: {}", request.idSucursal());

        Sucursal sucursal = sucursalRepository.findById(request.idSucursal())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Sucursal no encontrada con id: " + request.idSucursal()));

        Venta venta = ventaMapper.requestAEntidad(sucursal);

        for (DetalleVentaRequest detalleRequest : request.productos()) {
            log.info("Procesando producto con id: {} y cantidad: {}",
                    detalleRequest.idProducto(), detalleRequest.cantidadProducto());

            Producto producto = productoRepository.findById(detalleRequest.idProducto())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Producto no encontrado con id: " + detalleRequest.idProducto()));

            validarStockDisponible(producto, detalleRequest.cantidadProducto());

            producto.descontarCantidad(detalleRequest.cantidadProducto());

            DetalleVenta detalleVenta = detalleVentaMapper.requestAEntidad(detalleRequest, venta, producto);
            venta.agregarDetalle(detalleVenta);
        }

        Venta ventaGuardada = ventaRepository.save(venta);
        log.info("Venta registrada con id: {}", ventaGuardada.getId());

        return ventaMapper.entidadAResponse(ventaGuardada);
    }

    @Override
    public VentaResponse cancelar(Long id) {
        log.info("Cancelando venta con id: {}", id);

        Venta venta = obtenerVentaPorId(id);

        if (venta.getEstadoVenta() == EstadoVenta.CANCELADA)
            throw new IllegalStateException("La venta ya está cancelada");

        for (DetalleVenta detalle : venta.getDetalleVenta()) {
            Producto producto = detalle.getProducto();
            producto.aumentarCantidad(detalle.getCantidadProducto());
            log.info("Stock devuelto al producto con id: {}. Cantidad devuelta: {}",
                    producto.getId(), detalle.getCantidadProducto());
        }

        venta.cancelar();

        Venta ventaActualizada = ventaRepository.save(venta);
        log.info("Venta con id: {} cancelada correctamente", id);

        return ventaMapper.entidadAResponse(ventaActualizada);
    }

    private void validarStockDisponible(Producto producto, Integer cantidadSolicitada) {
        if (cantidadSolicitada > producto.getCantidad()) {
            throw new IllegalArgumentException(
                    "Stock insuficiente para el producto: " + producto.getNombre()
                            + ". Disponible: " + producto.getCantidad()
                            + ", solicitado: " + cantidadSolicitada
            );
        }
    }

    private Sucursal obtenerSucursal(Long idSucursal) {
        return sucursalRepository.findById(idSucursal)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Sucursal no encontrada con id: " + idSucursal));
    }

    private Producto obtenerProducto(Long idProducto) {
        return productoRepository.findById(idProducto)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Producto no encontrado con id: " + idProducto));
    }

    private Venta obtenerVentaPorId(Long idVenta) {
        return ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Venta no encontrada con id: " + idVenta));
    }


}
