package com.gina.almacen.services.sucursales;

import com.gina.almacen.dto.sucursal.SucursalRequest;
import com.gina.almacen.dto.sucursal.SucursalResponse;
import com.gina.almacen.entities.Sucursal;
import com.gina.almacen.exceptions.RecursoNoEncontradoException;
import com.gina.almacen.mappers.SucursalMapper;
import com.gina.almacen.repositories.SucursalRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class SucursalServiceImpl implements SucursalService{
    private final SucursalRepository sucursalReposirtory;
    private final SucursalMapper sucursalMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SucursalResponse> listar() {
        log.info("Listando todas las sucursales");
        return sucursalReposirtory.findAll().stream()
                .map(sucursalMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SucursalResponse obtenerPorId(Long id) {
        return sucursalMapper.entidadAResponse(obtenerSucursalExcepcion(id));
    }

    @Override
    public SucursalResponse registrar(SucursalRequest request) {
        validarDatosUnicos(request);
        log.info("Registrando sucursal...");
        Sucursal sucursal = sucursalMapper.requestAEntidad(request);
        sucursalReposirtory.save(sucursal);
        log.info("Nueva sucursal {} registrada", sucursal.getNombre());
        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public SucursalResponse actualizar(SucursalRequest request, Long id) {
                Sucursal sucursal = obtenerSucursalExcepcion(id);
        log.info("Actualizando sucursal con id: {}", id);
        validarCambiosUnicos(request, id);
        sucursal.actualizarSucursal(
                request.nombre(),
                request.direccion()
        );
        sucursalReposirtory.save(sucursal);
        log.info("La sucursal con el id: {} actualizada", id);
        return sucursalMapper.entidadAResponse(sucursal);
    }

    @Override
    public void eliminar(Long id) {
        Sucursal sucursal = obtenerSucursalExcepcion(id);
        sucursalReposirtory.delete(sucursal);
        log.info("Sucursal con id: {} eliminada", id);
    }

    private Sucursal obtenerSucursalExcepcion(Long id){
        log.info("Buscando sucursal por id: {}", id);
        return sucursalReposirtory.findById(id).orElseThrow(
                ()-> new RecursoNoEncontradoException("Sucursal no encontrada " + id)
        );
    }

    private void validarDatosUnicos(SucursalRequest request){
        log.info("Validando sucursal...");
        if (sucursalReposirtory.existsByNombreIgnoreCase(request.nombre().trim()))
            throw  new IllegalArgumentException("Ya existe una sucursal con el nombre: {}" + request.nombre());
    }
    private void validarCambiosUnicos(SucursalRequest request, Long id){
        log.info("Validando sucursal para cambio...");
        if (sucursalReposirtory.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(), id))
            throw  new IllegalArgumentException("Ya existe una sucursal con el nombre: {}" + request.nombre());
    }
}
