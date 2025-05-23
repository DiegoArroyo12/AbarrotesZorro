package mx.unam.aragon.view;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import mx.unam.aragon.model.dto.DetalleVentaDTO;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
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
        String sucursal = (String) model.get("sucursal");
        String fecha = (String) model.get("fecha");
        String hora = (String) model.get("hora");
        String caja = (String) model.get("caja");
        Double total = (Double) model.get("total");
        List<DetalleVentaDTO> detalles = (List<DetalleVentaDTO>) model.get("detalles");

        document.add(new Paragraph("Detalle de Venta"));
        document.add(new Paragraph("Cliente: " + cliente));
        document.add(new Paragraph("Empleado: " + empleado));
        document.add(new Paragraph("Caja: " + caja));
        document.add(new Paragraph("Sucursal: " + sucursal));
        document.add(new Paragraph("Fecha: " + fecha));
        document.add(new Paragraph("Hora de la compra: " + hora));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidths(new float[]{2, 4, 2, 2, 2});
        table.setWidthPercentage(100);

        table.addCell("Imagen");
        table.addCell("Producto");
        table.addCell("Cantidad");
        table.addCell("Precio");
        table.addCell("Subtotal");

        for (DetalleVentaDTO d : detalles) {
            try {
                InputStream is = getClass().getResourceAsStream("/static" + d.getImagen());
                if (is == null) {
                    is = getClass().getResourceAsStream("/static/img/default.jpg");
                }

                if (is != null) {
                    Image img = Image.getInstance(is.readAllBytes());
                    img.scaleAbsolute(40f, 40f);
                    PdfPCell imgCell = new PdfPCell(img, true);
                    imgCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(imgCell);
                } else {
                    table.addCell("Sin imagen");
                }
            } catch (Exception e) {
                e.printStackTrace();
                table.addCell("Error imagen");
            }

            table.addCell(d.getNombre());
            table.addCell(String.valueOf(d.getCantidad()));
            table.addCell(String.format("$%.2f", d.getPrecio()));
            table.addCell(String.format("$%.2f", d.getSubtotal()));
        }

        document.add(table);
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Total: $" + String.format("%.2f", total)));
    }
}
