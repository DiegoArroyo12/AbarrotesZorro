package mx.unam.aragon.controller.venta;

import mx.unam.aragon.model.dto.ProductoVentaDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DetalleVentaController {

    @PostMapping("/detalle-venta")
    public String mostrarDetalleVenta(@RequestBody List<ProductoVentaDTO> productos, Model model) {
        double total = productos.stream().mapToDouble(p -> p.getCantidad() * p.getPrecio()).sum();

        model.addAttribute("productos", productos);
        model.addAttribute("total", total);
        return "detalleVenta";
    }
}
