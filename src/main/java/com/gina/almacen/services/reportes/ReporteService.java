package com.gina.almacen.services.reportes;

import com.gina.almacen.dto.reportes.ReporteResponse;

import java.util.List;

public interface ReporteService {
    List<ReporteResponse> obtenerReporteVentasPorSucursal();
}
