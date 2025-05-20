package mx.unam.aragon.controller.venta;

import mx.unam.aragon.model.dto.ProductoVentaDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Controller
public class VentaController {

    private Map<String, Object> ultimaVenta;

    @PostMapping("/detalle-venta")
    public String procesarDetalleVenta(@RequestBody Map<String, Object> payload) {
        this.ultimaVenta = payload;
        return "redirect:/detalle-venta";
    }

    @GetMapping("/detalle-venta")
    public String mostrarDetalleVenta(Model model) {
        if (ultimaVenta == null) return "redirect:/inicio";

        List<Map<String, Object>> productosMap = (List<Map<String, Object>>) ultimaVenta.get("productos");

        List<ProductoVentaDTO> productos = productosMap.stream().map(p -> {
            ProductoVentaDTO dto = new ProductoVentaDTO();
            dto.setNombre((String) p.get("nombre"));
            dto.setPrecio(Double.parseDouble(p.get("precio").toString()));
            dto.setCantidad((Integer) p.get("cantidad"));
            return dto;
        }).toList();

        String empleado = (String) ultimaVenta.get("empleado");
        String cliente = (String) ultimaVenta.get("cliente");
        String caja = (String) ultimaVenta.get("caja");
        String hora = (String) ultimaVenta.get("hora");
        String fecha = (String) ultimaVenta.get("fecha");

        double total = productos.stream()
                .mapToDouble(p -> p.getPrecio() * p.getCantidad())
                .sum();

        model.addAttribute("productos", productos);
        model.addAttribute("total", total);
        model.addAttribute("empleado", empleado);
        model.addAttribute("cliente", cliente);
        model.addAttribute("caja", caja);
        model.addAttribute("hora", hora);
        model.addAttribute("fecha", fecha);

        return "detalleVenta";
    }
}
