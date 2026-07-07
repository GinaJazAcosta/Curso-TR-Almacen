package com.gina.almacen.repositories;

import com.gina.almacen.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoReposirtory extends JpaRepository<Producto, Long> {

}
