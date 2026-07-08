package com.gina.almacen.services.sucursales;

import com.gina.almacen.dto.sucursal.SucursalRequest;
import com.gina.almacen.dto.sucursal.SucursalResponse;

import java.util.List;

public interface SucursalService {
    List<SucursalResponse> listar();
    SucursalResponse obtenerPorId(Long id);
    SucursalResponse registrar(SucursalRequest request);
    SucursalResponse actualizar(SucursalRequest request, Long id);
    void eliminar(Long id);
}
