package mx.unam.aragon.view;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import mx.unam.aragon.model.dto.DetalleVentaDTO;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
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

        Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.RED.darker());
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);

        Paragraph titulo = new Paragraph("DETALLE DE VENTA", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(10f);
        document.add(titulo);

        document.add(new Paragraph("Cliente: " + cliente, boldFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Empleado: " + empleado, boldFont));
        document.add(new Paragraph("Caja: " + caja, boldFont));
        document.add(new Paragraph("Sucursal: " + sucursal, boldFont));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Fecha: " + fecha, boldFont));
        document.add(new Paragraph("Hora de la compra: " + hora, boldFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        table.setWidths(new float[]{2, 4, 2, 2, 2});
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);

        String[] encabezados = {"Imagen", "Producto", "Cantidad", "Precio", "Subtotal"};
        for (String encabezado : encabezados) {
            PdfPCell header = new PdfPCell(new Phrase(encabezado, normalFont));
            header.setBackgroundColor(Color.ORANGE);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setPadding(5);
            table.addCell(header);
        }

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
                    imgCell.setPadding(5);
                    table.addCell(imgCell);
                } else {
                    table.addCell(new PdfPCell(new Phrase("Sin imagen", normalFont)));
                }
            } catch (Exception e) {
                e.printStackTrace();
                table.addCell(new PdfPCell(new Phrase("Error imagen", normalFont)));
            }

            table.addCell(new PdfPCell(new Phrase(d.getNombre(), normalFont)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(d.getCantidad()), normalFont)));
            table.addCell(new PdfPCell(new Phrase(String.format("$%.2f", d.getPrecio()), normalFont)));
            table.addCell(new PdfPCell(new Phrase(String.format("$%.2f", d.getSubtotal()), normalFont)));
        }

        document.add(table);

        document.add(new Paragraph(" "));

        Paragraph totalParrafo = new Paragraph("Total: $" + String.format("%.2f", total), tituloFont);
        totalParrafo.setAlignment(Element.ALIGN_RIGHT);
        totalParrafo.setSpacingBefore(10f);
        document.add(totalParrafo);
    }

}
