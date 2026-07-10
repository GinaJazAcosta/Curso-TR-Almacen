package com.gina.almacen.repositories;

import com.gina.almacen.dto.reportes.ReporteResponse;
import com.gina.almacen.entities.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Venta, Long> {

    @Query("""
            SELECT new com.gina.almacen.dto.reportes.ReporteResponse(
                s.id,
                s.nombre,
                COALESCE(SUM(d.cantidadProducto * d.precioProducto), 0),
                COALESCE(SUM(d.cantidadProducto), 0)
            )
            FROM Venta v
            JOIN v.sucursal s
            JOIN v.detalleVenta d
            WHERE v.estadoVenta = com.gina.almacen.enums.EstadoVenta.REGISTRADA
            GROUP BY s.id, s.nombre
            ORDER BY s.id
            """)
    List<ReporteResponse> obtenerReporteVentasPorSucursal();
}