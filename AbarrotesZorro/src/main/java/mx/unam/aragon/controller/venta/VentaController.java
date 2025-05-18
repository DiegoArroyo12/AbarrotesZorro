package mx.unam.aragon.controller.venta;

import mx.unam.aragon.model.dto.ProductoVentaDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Controller
public class VentaController {

    @PostMapping("/detalle-venta")
    public String mostrarDetalleVenta(@RequestBody Map<String, Object> payload, Model model) {
        // Extraemos la lista de productos
        List<Map<String, Object>> productosMap = (List<Map<String, Object>>) payload.get("productos");

        List<ProductoVentaDTO> productos = productosMap.stream().map(p -> {
            ProductoVentaDTO dto = new ProductoVentaDTO();
            dto.setNombre((String) p.get("nombre"));
            dto.setPrecio(Double.parseDouble(p.get("precio").toString()));
            dto.setCantidad((Integer) p.get("cantidad"));
            return dto;
        }).toList();

        String empleado = (String) payload.get("empleado");
        String cliente = (String) payload.get("cliente");
        String caja = (String) payload.get("caja");

        double total = productos.stream()
                .mapToDouble(p -> p.getPrecio() * p.getCantidad())
                .sum();

        model.addAttribute("productos", productos);
        model.addAttribute("total", total);
        model.addAttribute("empleado", empleado);
        model.addAttribute("cliente", cliente);
        model.addAttribute("caja", caja);

        return "detalleVenta";
    }

}
