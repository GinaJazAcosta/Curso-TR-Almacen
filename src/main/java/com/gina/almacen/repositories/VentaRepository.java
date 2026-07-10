package com.gina.almacen.repositories;

import com.gina.almacen.entities.Venta;
import com.gina.almacen.enums.EstadoVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findByEstadoVenta(EstadoVenta estadoVenta);
    Optional<Venta> findByIdAndEstadoVenta(Long id, EstadoVenta estadoVenta);
}
