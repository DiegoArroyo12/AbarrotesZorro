package mx.unam.aragon.controller.venta;

import mx.unam.aragon.model.dto.ProductoVentaDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("carrito")
public class VentaController {

    @ModelAttribute("carrito")
    public List<ProductoVentaDTO> carrito() {
        return new ArrayList<>();
    }

    @PostMapping("/realizar-compra")
    public String realizarCompra(@ModelAttribute("carrito") List<ProductoVentaDTO> carrito, Model model) {
        double total = carrito.stream()
                .mapToDouble(p -> p.getPrecio() * p.getCantidad())
                .sum();

        model.addAttribute("carrito", carrito);
        model.addAttribute("total", total);

        return "detalleVenta";
    }
}
