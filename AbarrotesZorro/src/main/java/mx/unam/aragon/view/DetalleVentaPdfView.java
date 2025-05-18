package mx.unam.aragon.view;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import mx.unam.aragon.model.dto.DetalleVentaDTO;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class DetalleVentaPdfView extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model,
                                    Document document,
                                    PdfWriter writer,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {

        String cliente = (String) model.get("cliente");
        String empleado = (String) model.get("empleado");
        String caja = (String) model.get("caja");
        Double total = (Double) model.get("total");
        List<DetalleVentaDTO> detalles = (List<DetalleVentaDTO>) model.get("detalles");

        document.add(new Paragraph("Detalle de Venta"));
        document.add(new Paragraph("Cliente: " + cliente));
        document.add(new Paragraph("Empleado: " + empleado));
        document.add(new Paragraph("Caja: " + caja));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        table.addCell("Producto");
        table.addCell("Cantidad");
        table.addCell("Precio");
        table.addCell("Subtotal");

        for (DetalleVentaDTO d : detalles) {
            table.addCell(d.getNombre());
            table.addCell(String.valueOf(d.getCantidad()));
            table.addCell(String.valueOf(d.getPrecio()));
            table.addCell(String.valueOf(d.getSubtotal()));
        }

        document.add(table);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Total: $" + total));
    }
}
