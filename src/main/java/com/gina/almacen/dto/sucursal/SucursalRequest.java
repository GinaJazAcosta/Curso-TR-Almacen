package com.gina.almacen.dto.sucursal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SucursalRequest(
        @NotBlank(message="El nombre de la sucursal es requerido")
        @Size(min=5,max=50,message="El nombre de la sucursal es requerido y debe tener entre 5 y 50 caracteres")
        String nombre,

        @NotBlank(message = "La dirección es requerida")
        @Size(min=5,max=150,message="El nombre de la sucursal es requerido y debe tener entre 5 y 150 caracteres")
        String direccion
) { }
