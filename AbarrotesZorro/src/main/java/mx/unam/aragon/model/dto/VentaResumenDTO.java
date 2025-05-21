package mx.unam.aragon.model.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class VentaResumenDTO {

    private Long idVenta;
    private String cliente;
    private String empleado;
    private String sucursal;
    private String caja;
    private double total;
    private LocalDateTime fecha;

    public VentaResumenDTO(Long idVenta, String cliente, String empleado, String sucursal, String caja, double total, LocalDateTime fecha) {
        this.idVenta = idVenta;
        this.cliente = cliente;
        this.empleado = empleado;
        this.sucursal = sucursal;
        this.caja = caja;
        this.total = total;
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "VentaResumenDTO{" +
                "idVenta=" + idVenta +
                ", cliente='" + cliente + '\'' +
                ", empleado='" + empleado + '\'' +
                ", sucursal='" + sucursal + '\'' +
                ", caja='" + caja + '\'' +
                ", total=" + total +
                ", fecha=" + fecha +
                '}';
    }
}
