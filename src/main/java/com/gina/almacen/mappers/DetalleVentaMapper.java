package com.gina.almacen.mappers;

import com.gina.almacen.dto.ventas.DetalleVentaRequest;
import com.gina.almacen.dto.ventas.DetalleVentaResponse;
import com.gina.almacen.entities.DetalleVenta;
import com.gina.almacen.entities.Producto;
import com.gina.almacen.entities.Venta;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DetalleVentaMapper {

    public DetalleVenta requestAEntidad(DetalleVentaRequest request, Venta venta, Producto producto) {
        if (request == null) return null;

        return DetalleVenta.builder()
                .venta(venta)
                .producto(producto)
                .cantidadProducto(request.cantidadProducto())
                .precioProducto(producto.getPrecio()) // precio actual
                .build();
    }

    public DetalleVentaResponse entidadAResponse(DetalleVenta detalleVenta){
        if (detalleVenta==null) return null;

        BigDecimal subtotal = detalleVenta.getPrecioProducto()
                .multiply(BigDecimal.valueOf(detalleVenta.getCantidadProducto()));

        return new DetalleVentaResponse(
                detalleVenta.getProducto().getId(),
                detalleVenta.getProducto().getNombre(),
                detalleVenta.getCantidadProducto(),
                detalleVenta.getPrecioProducto(),
                subtotal
        );
    }
}
