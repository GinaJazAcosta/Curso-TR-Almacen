package com.gina.almacen.entities;

import com.gina.almacen.enums.Categoria;
import com.gina.almacen.enums.EstadoVenta;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "VENTAS")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID_VENTA")
    private Long id;

    @Column(name = "ESTADO", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoVenta estadoVenta;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUCURSAL", nullable = false)
    private Sucursal sucursal;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "venta",
    orphanRemoval = true, cascade = CascadeType.ALL)
    private List<DetalleVenta> detalleVenta = new ArrayList<>();

    public void cancelar(){
        if (this.estadoVenta == EstadoVenta.CANCELADA)
            throw new IllegalStateException("La venta ya esta cancelada");
        this.estadoVenta = EstadoVenta.CANCELADA;
    }

    public void agregarDetalle(DetalleVenta detalleVenta){
        if (detalleVenta==null)
            throw new IllegalStateException("El detalle es requerido");
        if (this.detalleVenta == null)
            this.detalleVenta= new ArrayList<>();
        this.detalleVenta.add(detalleVenta);
    }

}
