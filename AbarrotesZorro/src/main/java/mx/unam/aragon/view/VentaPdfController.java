package mx.unam.aragon.view;

import jakarta.servlet.http.HttpServletResponse;
import mx.unam.aragon.model.dto.DetalleVentaDTO;
import mx.unam.aragon.model.entity.*;
import mx.unam.aragon.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class VentaPdfController {

    @Autowired private VentaRepository ventaRepository;
    @Autowired private DetalleVentaRepository detalleVentaRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private EmpleadoRepository empleadoRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private CajaRepository cajaRepository;

    @PostMapping("/venta/pdf")
    public ModelAndView generarPdf(
            @RequestParam String cliente,
            @RequestParam String empleado,
            @RequestParam String caja,
            @RequestParam String sucursal,
            @RequestParam String fecha,
            @RequestParam String hora,
            @RequestParam double total,
            @RequestParam("nombres[]") List<String> nombres,
            @RequestParam("cantidades[]") List<Integer> cantidades,
            @RequestParam("precios[]") List<Double> precios,
            @RequestParam("imagenes[]") List<String> imagenes,
            HttpServletResponse response
    ) {
        List<DetalleVentaDTO> detallesDTO = new ArrayList<>();

        // 1. Construir DTOs para el PDF con ruta relativa al classpath
        for (int i = 0; i < nombres.size(); i++) {
            String imagenRelativa = imagenes.get(i);

            // Asegurar que tenga formato correcto (por ejemplo: /img/aguacate.jpg)
            if (!imagenRelativa.startsWith("/img/")) {
                imagenRelativa = "/img/" + imagenRelativa.replaceFirst("^/+", "");
            }

            detallesDTO.add(new DetalleVentaDTO(
                    nombres.get(i),
                    cantidades.get(i),
                    precios.get(i),
                    imagenRelativa
            ));
        }

        Optional<ClienteEntity> clienteEntity = clienteRepository.findByCorreo(cliente);
        Optional<EmpleadoEntity> empleadoEntity = empleadoRepository.findById(Long.parseLong(empleado));
        Optional<CajaEntity> cajaEntity = cajaRepository.findById(Long.parseLong(caja));

        // 2. Guardar la venta
        VentaEntity venta = VentaEntity.builder()
                .cliente(clienteEntity.orElse(null))
                .empleado(empleadoEntity.orElse(null))
                .caja(cajaEntity.orElse(null))
                .fechaVenta(LocalDateTime.now())
                .total(total)
                .build();

        venta = ventaRepository.save(venta);

        // 3. Guardar detalles
        for (int i = 0; i < nombres.size(); i++) {
            ProductoEntity producto = productoRepository.findByNombre(nombres.get(i)).orElse(null);
            if (producto == null) continue;

            DetalleVentaEntity detalle = DetalleVentaEntity.builder()
                    .venta(venta)
                    .producto(producto)
                    .cantidad(cantidades.get(i))
                    .precioUnitario(precios.get(i))
                    .build();

            detalleVentaRepository.save(detalle);
        }

        // 4. Preparar PDF
        response.setHeader("Content-Disposition", "attachment; filename=venta.pdf");

        ModelAndView mav = new ModelAndView(new DetalleVentaPdfView());
        mav.addObject("cliente", clienteEntity.map(ClienteEntity::getNombre).orElse("Desconocido"));
        mav.addObject("empleado", empleado);
        mav.addObject("caja", caja);
        mav.addObject("sucursal", sucursal);
        mav.addObject("fecha", fecha);
        mav.addObject("hora", hora);
        mav.addObject("detalles", detallesDTO);
        mav.addObject("total", total);

        return mav;
    }
}
