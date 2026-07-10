package com.gina.almacen.services.reportes;

import com.gina.almacen.dto.reportes.ReporteResponse;
import com.gina.almacen.repositories.ReporteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ReporteResponse> obtenerReporteVentasPorSucursal() {
        log.info("Consultando reporte de ventas por sucursal");
        return reporteRepository.obtenerReporteVentasPorSucursal();
    }
}