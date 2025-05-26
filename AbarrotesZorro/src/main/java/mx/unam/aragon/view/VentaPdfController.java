package mx.unam.aragon.view;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mx.unam.aragon.model.dto.DetalleVentaDTO;
import mx.unam.aragon.model.entity.*;
import mx.unam.aragon.model.entity.seriales.IdProductoSucursal;
import mx.unam.aragon.repository.*;
import mx.unam.aragon.service.venta.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

@Controller
public class VentaPdfController {

    @Autowired private VentaRepository ventaRepository;
    @Autowired private DetalleVentaRepository detalleVentaRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private EmpleadoRepository empleadoRepository;
    @Autowired private ProductoRepository productoRepository;
    @Autowired private CajaRepository cajaRepository;
    @Autowired private SucursalRepository sucursalRepository;
    @Autowired private ProductosPedidosRepository productosPedidosRepository;

    @Value("${correo.usuario}")
    private String fromEmail;

    @Value("${correo.contrasena}")
    private String emailPassword;

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
            HttpSession session
    ) {
        List<DetalleVentaDTO> detallesDTO = new ArrayList<>();
        for (int i = 0; i < nombres.size(); i++) {
            String imagenRelativa = imagenes.get(i);
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

        // Obtener entidades relacionadas
        Optional<ClienteEntity> clienteEntity = clienteRepository.findByCorreo(cliente);
        Optional<EmpleadoEntity> empleadoEntity = empleadoRepository.findById(Long.parseLong(empleado));
        Optional<CajaEntity> cajaEntity = cajaRepository.findById(Long.parseLong(caja));
        Optional<SucursalEntity> sucursalEntity = sucursalRepository.findById(Long.parseLong(sucursal));

        // Guardar la venta principal
        VentaEntity venta = VentaEntity.builder()
                .cliente(clienteEntity.orElse(null))
                .empleado(empleadoEntity.orElse(null))
                .caja(cajaEntity.orElse(null))
                .fechaVenta(LocalDateTime.now())
                .total(total)
                .build();
        venta = ventaRepository.save(venta);

        // Guardar los detalles de la venta
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

            // Guardar en Productos Pedidos
            if (sucursalEntity.isPresent()) {
                IdProductoSucursal id = new IdProductoSucursal(producto.getId(), sucursalEntity.get().getId());

                ProductosPedidosEntity existente = productosPedidosRepository.findById(id).orElse(null);
                if (existente != null) {
                    existente.setCantidad(existente.getCantidad() + cantidades.get(i));
                    productosPedidosRepository.save(existente);
                } else {
                    ProductosPedidosEntity nuevo = ProductosPedidosEntity.builder()
                            .id(id)
                            .cantidad(cantidades.get(i))
                            .build();
                    productosPedidosRepository.save(nuevo);
                }
            }
        }

        // Modelo para el PDF y correo
        Map<String, Object> model = new HashMap<>();
        model.put("cliente", clienteEntity.map(ClienteEntity::getNombre).orElse("Desconocido"));
        model.put("empleado", empleadoEntity.map(EmpleadoEntity::getNombre).orElse("Desconocido"));
        model.put("caja", caja);
        model.put("sucursal", sucursalEntity.map(SucursalEntity::getNombre).orElse("Desconocido"));
        model.put("fecha", fecha);
        model.put("hora", hora);
        model.put("detalles", detallesDTO);
        model.put("total", total);

        try {
            File tempPdf = File.createTempFile("venta-", ".pdf");
            generarPdfEnArchivo(tempPdf, model);

            clienteEntity.ifPresent(clienteFinal -> {
                enviarCorreoConAdjunto(clienteFinal.getCorreo(), tempPdf);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        session.removeAttribute("ventaTemp");

        return new ModelAndView("redirect:/inicio");

    }


    private void generarPdfEnArchivo(File archivo, Map<String, Object> model) throws Exception {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(archivo));
        Color cremaSuave = new Color(243, 238, 228);
        writer.setPageEvent(new PageBackgroundEvent(cremaSuave));
        document.open();
        new DetalleVentaPdfView().buildPdfDocument(model, document, null, null, null);
        document.close();
    }


    private void enviarCorreoConAdjunto(String destinatario, File archivoPdf) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, emailPassword);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Gracias por tu compra");
            message.setSentDate(new Date());

            // Texto del correo
            MimeBodyPart texto = new MimeBodyPart();
            texto.setText("Adjunto encontrarás el comprobante de tu compra.");

            // Adjunto PDF
            MimeBodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(new DataHandler(new FileDataSource(archivoPdf)));
            adjunto.setFileName("venta.pdf");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);
            multipart.addBodyPart(adjunto);

            message.setContent(multipart);

            Transport.send(message);
            archivoPdf.delete();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
