package com.gina.almacen.controllers;


import com.gina.almacen.dto.reportes.ReporteResponse;
import com.gina.almacen.services.reportes.ReporteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@AllArgsConstructor
@Validated
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/ventas/sucursales")
    public ResponseEntity<List<ReporteResponse>> obtenerReporteVentasPorSucursal() {
        return ResponseEntity.ok(reporteService.obtenerReporteVentasPorSucursal());
    }
}
