package com.gina.almacen.entities;

import com.gina.almacen.enums.Categoria;
import com.gina.almacen.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "SUCURSALES")
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SUCURSAL")
    private Long id;

    @Column(name = "NOMBRE", length = 50, unique = true, nullable = false)
    private String nombre;

    @Column(name = "DIRECCION", length = 150, nullable = false)
    private  String direccion;

    public void actualizarSucursal(String nombre, String direccion) {
        validarDatos(nombre, direccion);
        this.nombre = nombre.trim();
        this.direccion = direccion.trim();
    }

    private void validarDatos(String nombre, String direccion) {
        StringCustomUtils.ValidarTamanio(nombre, 5,50, "El nombre es requerido y debe tener entre 5 y 50 caracteres");
        StringCustomUtils.ValidarTamanio(direccion, 10,150, "La dirección es requerida y debe tener entre 10 y 150 caracteres");
    }
}
