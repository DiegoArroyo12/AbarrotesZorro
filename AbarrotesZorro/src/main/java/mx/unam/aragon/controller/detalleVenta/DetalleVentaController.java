package mx.unam.aragon.controller.detalleVenta;

import mx.unam.aragon.model.dto.ProductoVentaDTO;
import mx.unam.aragon.model.dto.VentaResumenDTO;
import mx.unam.aragon.model.entity.SucursalEntity;
import mx.unam.aragon.repository.DetalleVentaRepository;
import mx.unam.aragon.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DetalleVentaController {

    @Autowired
    SucursalRepository sucursalRepository;

    @PostMapping("venta/detalle-venta")
    public String mostrarDetalleVenta(@RequestBody List<ProductoVentaDTO> productos, Model model) {
        double total = productos.stream().mapToDouble(p -> p.getCantidad() * p.getPrecio()).sum();

        model.addAttribute("productos", productos);
        model.addAttribute("total", total);
        return "detalleVenta";
    }

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @GetMapping("/historial-ventas")
    public String mostrarHistorialVentas(@RequestParam("idSucursal") Integer idSucursal, Model model) {
        List<VentaResumenDTO> resumenes = detalleVentaRepository.findResumenesBySucursal(idSucursal);

        // Nombre de la Sucursal
        String nombreSucursal = sucursalRepository.findById(Long.valueOf(idSucursal)).map(SucursalEntity::getNombre).orElse("Nombre no encontrado");

        model.addAttribute("ventas", resumenes);
        model.addAttribute("nombreSucursal", nombreSucursal);
        model.addAttribute("idSucursal", idSucursal);
        return "historial_ventas";
    }

    @GetMapping("/ventas/detalles")
    public String verDetalleVenta(@RequestParam("id") Integer idVenta,
                                  @RequestParam("idSucursal") Integer idSucursal,
                                  Model model) {
        List<ProductoVentaDTO> detalles = detalleVentaRepository.findDetalleVentaByIdVenta(idVenta);
        VentaResumenDTO venta = detalleVentaRepository.findResumenVentaById(idVenta);

        double total = detalles.stream().mapToDouble(d -> d.getCantidad() * d.getPrecio()).sum();

        model.addAttribute("idSucursal", idSucursal);
        model.addAttribute("detalles", detalles);
        model.addAttribute("venta", venta);
        model.addAttribute("total", total);

        return "detalle_venta";
    }
}
