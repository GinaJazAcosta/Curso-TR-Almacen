package com.gina.almacen.mappers;

import com.gina.almacen.dto.ventas.DetalleVentaResponse;
import com.gina.almacen.dto.ventas.VentaRequest;
import com.gina.almacen.dto.ventas.VentaResponse;
import com.gina.almacen.entities.Sucursal;
import com.gina.almacen.entities.Venta;
import com.gina.almacen.enums.EstadoVenta;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Component
public class VentaMapper {
    private final SucursalMapper sucursalMapper;
    private final DetalleVentaMapper detalleVentaMapper;

    public Venta requestAEntidad(Sucursal sucursal) {
        return Venta.builder()
                .estadoVenta(EstadoVenta.REGISTRADA)
                .fecha(LocalDate.now())
                .sucursal(sucursal)
                .detalleVenta(new ArrayList<>())
                .build();
    }

    public VentaResponse entidadAResponse(Venta venta){
        if (venta == null) return null;

        List<DetalleVentaResponse> detalles = venta.getDetalleVenta().stream()
                .map(detalleVentaMapper::entidadAResponse)
                .toList();

        BigDecimal total = detalles.stream()
                .map(DetalleVentaResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new VentaResponse(
                venta.getId(),
                venta.getFecha().toString(),
                venta.getEstadoVenta().getDescripcion(),
                sucursalMapper.entidadAResponse(venta.getSucursal()),
                detalles,
                total
        );
    }
}
