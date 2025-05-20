package mx.unam.aragon.view;

import jakarta.servlet.http.HttpServletResponse;
import mx.unam.aragon.model.dto.DetalleVentaDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class VentaPdfController {

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
        List<DetalleVentaDTO> detalles = new ArrayList<>();

        for (int i = 0; i < nombres.size(); i++) {
            String imagenRelativa = imagenes.get(i);

            // Asegurar formato /img/productos/nombre.png
            if (!imagenRelativa.startsWith("/")) {
                imagenRelativa = "/" + imagenRelativa;
            }

            // Aquí NO se genera la ruta absoluta. Solo se guarda la ruta relativa
            // que luego se resolverá dentro de DetalleVentaPdfView usando getResourceAsStream
            DetalleVentaDTO dto = new DetalleVentaDTO(
                    nombres.get(i),
                    cantidades.get(i),
                    precios.get(i),
                    imagenRelativa
            );

            detalles.add(dto);
        }

        // Descargar el PDF directamente
        response.setHeader("Content-Disposition", "attachment; filename=venta.pdf");

        ModelAndView mav = new ModelAndView(new DetalleVentaPdfView());
        mav.addObject("cliente", cliente);
        mav.addObject("empleado", empleado);
        mav.addObject("caja", caja);
        mav.addObject("sucursal", sucursal);
        mav.addObject("fecha", fecha);
        mav.addObject("hora", hora);
        mav.addObject("detalles", detalles);
        mav.addObject("total", total);

        return mav;
    }
}
