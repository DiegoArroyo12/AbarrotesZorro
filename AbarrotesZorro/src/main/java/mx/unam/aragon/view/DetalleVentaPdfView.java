package mx.unam.aragon.view;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import mx.unam.aragon.model.dto.DetalleVentaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
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

        String rutaBaseLocal = (String) model.get("rutaLocal");
        String cliente = (String) model.get("cliente");
        String empleado = (String) model.get("empleado");
        String sucursal = (String) model.get("sucursal");
        String direccion = (String) model.get("direccion");
        String fecha = (String) model.get("fecha");
        String hora = (String) model.get("hora");
        String caja = (String) model.get("caja");
        Double total = (Double) model.get("total");
        List<DetalleVentaDTO> detalles = (List<DetalleVentaDTO>) model.get("detalles");

        Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.RED.darker());
        Font clienteFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, Color.BLACK);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLACK);

        try {
            InputStream logoStream = getClass().getResourceAsStream("/static/img/abarroteslogo.png");
            if (logoStream != null) {
                Image logo = Image.getInstance(logoStream.readAllBytes());
                logo.setAlignment(Image.ALIGN_CENTER);
                logo.scaleToFit(250, 250);
                logo.setSpacingAfter(10f);
                document.add(logo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Paragraph titulo2 = new Paragraph("Gracias por su compra:", tituloFont);
        titulo2.setAlignment(Element.ALIGN_CENTER);
        titulo2.setSpacingAfter(10f);
        document.add(titulo2);


        Paragraph p1 = new Paragraph(cliente, clienteFont);
        p1.setAlignment(Element.ALIGN_CENTER);
        document.add(p1);

        document.add(new Paragraph(" "));

        Paragraph p2 = new Paragraph("Te atendió: " + empleado, boldFont);
        p2.setAlignment(Element.ALIGN_CENTER);
        document.add(p2);

        Paragraph p3 = new Paragraph("Caja: " + caja, boldFont);
        p3.setAlignment(Element.ALIGN_CENTER);
        document.add(p3);

        Paragraph p4 = new Paragraph("Sucursal: " + sucursal, boldFont);
        p4.setAlignment(Element.ALIGN_CENTER);
        document.add(p4);

        Paragraph p5 = new Paragraph("Dirección: " + direccion, boldFont);
        p5.setAlignment(Element.ALIGN_CENTER);
        document.add(p5);

        document.add(new Paragraph(" "));

        Paragraph p6 = new Paragraph("Fecha: " + fecha, boldFont);
        p6.setAlignment(Element.ALIGN_CENTER);
        document.add(p6);

        Paragraph p7 = new Paragraph("Hora: " + hora, boldFont);
        p7.setAlignment(Element.ALIGN_CENTER);
        document.add(p7);

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
            InputStream is = null;
            try {
                if (d.getImagen() != null) {
                    String imagenRelativa = d.getImagen();

                    // Si la imagen comienza con /img/productos/, recórtalo para evitar duplicación
                    if (imagenRelativa.startsWith("/img/productos/")) {
                        imagenRelativa = imagenRelativa.replaceFirst("^/img/productos/", "");
                    }

                    File file = new File(rutaBaseLocal + File.separator + imagenRelativa);
                    if (file.exists()) {
                        is = new FileInputStream(file);
                    } else {
                        // Intenta cargar desde el classpath (para imágenes dentro del proyecto)
                        is = getClass().getResourceAsStream("/static" + d.getImagen());
                    }
                }

                if (is == null) {
                    is = getClass().getResourceAsStream("/static/img/abarrotes.png"); // imagen por defecto
                }

                if (is != null) {
                    Image img = Image.getInstance(is.readAllBytes());
                    img.scaleAbsolute(40f, 40f);
                    PdfPCell imgCell = new PdfPCell(img, true);
                    imgCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    imgCell.setPadding(5);
                    table.addCell(imgCell);
                } else {
                    table.addCell(new PdfPCell(new Phrase("Sin Imagen", normalFont)));
                }

            } catch (Exception e) {
                e.printStackTrace();
                table.addCell(new PdfPCell(new Phrase("Error Imagen", normalFont)));
            }

            PdfPCell nombreCell = new PdfPCell(new Phrase(d.getNombre(), normalFont));
            nombreCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            nombreCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(nombreCell);

            PdfPCell cantidadCell = new PdfPCell(new Phrase(String.valueOf(d.getCantidad()), normalFont));
            cantidadCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cantidadCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cantidadCell);

            PdfPCell precioCell = new PdfPCell(new Phrase(String.format("$%.2f", d.getPrecio()), normalFont));
            precioCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            precioCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(precioCell);

            PdfPCell subtotalCell = new PdfPCell(new Phrase(String.format("$%.2f", d.getSubtotal()), normalFont));
            subtotalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            subtotalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(subtotalCell);

        }

        document.add(table);

        document.add(new Paragraph(" "));

        Paragraph totalParrafo = new Paragraph("Total: $" + String.format("%.2f", total), tituloFont);
        totalParrafo.setAlignment(Element.ALIGN_RIGHT);
        totalParrafo.setSpacingBefore(10f);
        document.add(totalParrafo);
    }

}
