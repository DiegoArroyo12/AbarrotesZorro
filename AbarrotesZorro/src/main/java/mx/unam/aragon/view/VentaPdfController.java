package mx.unam.aragon.view;
import jakarta.servlet.http.HttpServletResponse;
import mx.unam.aragon.model.dto.DetalleVentaDTO;
import mx.unam.aragon.view.DetalleVentaPdfView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

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
            HttpServletResponse response
    ) {
        List<DetalleVentaDTO> detalles = new ArrayList<>();
        for (int i = 0; i < nombres.size(); i++) {
            detalles.add(new DetalleVentaDTO(nombres.get(i), cantidades.get(i), precios.get(i)));
        }

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
