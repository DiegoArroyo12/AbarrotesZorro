package mx.unam.aragon.controller.venta;

import mx.unam.aragon.model.dto.ProductoVentaDTO;
import mx.unam.aragon.model.entity.CajaEntity;
import mx.unam.aragon.model.entity.ClienteEntity;
import mx.unam.aragon.model.entity.EmpleadoEntity;
import mx.unam.aragon.repository.CajaRepository;
import mx.unam.aragon.repository.ClienteRepository;
import mx.unam.aragon.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class VentaController {

    private Map<String, Object> ultimaVenta;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CajaRepository cajaRepository;

    @PostMapping("/detalle-venta")
    public String procesarDetalleVenta(@RequestBody Map<String, Object> payload) {
        this.ultimaVenta = payload;
        return "redirect:/detalle-venta";
    }

    @GetMapping("/detalle-venta")
    public String mostrarDetalleVenta(Model model) {
        if (ultimaVenta == null) return "redirect:/inicio";

        // Productos
        List<Map<String, Object>> productosMap = (List<Map<String, Object>>) ultimaVenta.get("productos");

        List<ProductoVentaDTO> productos = productosMap.stream().map(p -> {
            ProductoVentaDTO dto = new ProductoVentaDTO();
            dto.setNombre((String) p.get("nombre"));
            dto.setPrecio(Double.parseDouble(p.get("precio").toString()));
            dto.setCantidad((Integer) p.get("cantidad"));
            dto.setImagen((String) p.get("imagen"));
            return dto;
        }).toList();

        // IDs recibidos
        String empleadoId = String.valueOf(ultimaVenta.get("empleado"));
        String clienteId = String.valueOf(ultimaVenta.get("cliente"));
        String cajaId = String.valueOf(ultimaVenta.get("caja"));


        // Buscar entidades
        Optional<EmpleadoEntity> empleado = empleadoRepository.findById(Long.parseLong(empleadoId));
        EmpleadoEntity emp = empleado.orElse(null);
        if (emp != null) {
            model.addAttribute("idEmpleado", emp.getId());
            model.addAttribute("empleadoNombre", emp.getNombre());
        } else {
            model.addAttribute("idEmpleado", 0);
            model.addAttribute("empleadoNombre", "Desconocido");
        }
        Optional<ClienteEntity> cliente = clienteRepository.findById(clienteId);
        Optional<CajaEntity> caja = cajaRepository.findById(Long.parseLong(cajaId));

        // Información adicional
        String hora = (String) ultimaVenta.get("hora");
        String fecha = (String) ultimaVenta.get("fecha");

        double total = productos.stream()
                .mapToDouble(p -> p.getPrecio() * p.getCantidad())
                .sum();

        // Agregar al modelo
        model.addAttribute("productos", productos);
        model.addAttribute("total", total);
        model.addAttribute("idEmpleado", emp.getId());
        model.addAttribute("empleadoNombre", empleado.map(e -> e.getNombre()).orElse("Desconocido"));
        model.addAttribute("clienteId", clienteId);
        model.addAttribute("clienteNombre", cliente.map(c -> c.getNombre()).orElse("Desconocido"));
        model.addAttribute("cajaId", cajaId);
        model.addAttribute("cajaNombre", caja.map(c -> c.getNombre()).orElse("Desconocida"));
        model.addAttribute("hora", hora);
        model.addAttribute("fecha", fecha);

        return "detalleVenta";
    }
}
