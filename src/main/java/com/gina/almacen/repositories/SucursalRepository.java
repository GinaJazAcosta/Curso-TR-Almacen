package com.gina.almacen.repositories;

import com.gina.almacen.entities.Sucursal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    //SELECT COUNT(*) FROM SUCURSALES WHERE NOMBRE = ?;
    boolean existsByNombreIgnoreCase(String nombre);

    //SELECT COUNT(*) FROM SUCURSALES WHERE NOMBRE = (?) AND ID<> (?);
    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long Id);

    //CONSULTA DERIVADA
    Optional<Sucursal> findByNombre(String nombre);

    //CONSULTA JPQL
    @Query("SELECT s FROM Sucursal s WHERE s.nombre = :nombre")
    Optional<Sucursal> buscarPorNombre(@Param("nombre") String nombre);

    //CONSULTA NATIVA
    @Query(nativeQuery = true, value = "SELECT * FROM SUCURSALES WHERE NOMBRE = :nombre")
    Optional<Sucursal> buscarProNombreSQL(@Param("nombre") String nombre);
}
