package mx.unam.aragon.repository;

import mx.unam.aragon.model.dto.ProductoVentaDTO;
import mx.unam.aragon.model.entity.DetalleVentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import mx.unam.aragon.model.dto.VentaResumenDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVentaEntity,Long> {
    @Query("SELECT new mx.unam.aragon.model.dto.VentaResumenDTO(" +
            "v.id, cl.nombre, e.nombre, s.nombre, c.nombre, v.total, v.fechaVenta) " +
            "FROM VentaEntity v " +
            "JOIN v.empleado e " +
            "JOIN v.cliente cl " +
            "JOIN e.sucursal s " +
            "JOIN v.caja c " +
            "WHERE s.id = :idSucursal")
    List<VentaResumenDTO> findResumenesBySucursal(@Param("idSucursal") Integer idSucursal);

    @Query("SELECT new mx.unam.aragon.model.dto.ProductoVentaDTO(p.nombre, dv.precioUnitario, dv.cantidad, p.imagen, dv.id) " +
            "FROM DetalleVentaEntity dv " +
            "JOIN dv.producto p " +
            "WHERE dv.venta.id = :idVenta")
    List<ProductoVentaDTO> findDetalleVentaByIdVenta(@Param("idVenta") Integer idVenta);

    @Query("SELECT new mx.unam.aragon.model.dto.VentaResumenDTO(" +
            "v.id, cl.nombre, e.nombre, s.nombre, c.nombre, v.total, v.fechaVenta) " +
            "FROM VentaEntity v " +
            "JOIN v.empleado e " +
            "JOIN v.caja c " +
            "JOIN e.sucursal s " +
            "JOIN v.cliente cl " +
            "WHERE v.id = :idVenta")
    VentaResumenDTO findResumenVentaById(@Param("idVenta") Integer idVenta);
}
