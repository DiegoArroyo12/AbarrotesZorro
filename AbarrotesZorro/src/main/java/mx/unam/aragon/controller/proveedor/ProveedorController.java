package mx.unam.aragon.controller.proveedor;


import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPTable;
import jakarta.validation.Valid;
import jakarta.mail.internet.MimeMessage;
import mx.unam.aragon.model.entity.*;
import mx.unam.aragon.repository.*;
import org.springframework.mail.javamail.MimeMessageHelper;
import mx.unam.aragon.model.entity.seriales.IdProductoSucursal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Controller
public class ProveedorController {

    @Autowired
    ProveedorRepository proveedorRepository;

    @Autowired
    SucursalRepository sucursalRepository;

    @Autowired
    ProductosPedidosRepository productosPedidosRepository;

    @Autowired
    DetallePedidoRepository detallePedidoRepository;

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    InventarioRepository inventarioRepository;

    @Autowired
    EmpleadoRepository empleadoRepository;


    @Value("${imagenes.ruta}")
    private String imagenesPath;


    @GetMapping("/proveedor/pedidos")
    public String mostrarProveedor(@RequestParam("idSucursal") Integer idSucursal,
                                   @RequestParam("idEmpleado") Integer idEmpleado,
                                   Model model) {

        String nombreSucursal = sucursalRepository.findById(Long.valueOf(idSucursal))
                .map(SucursalEntity::getNombre)
                .orElse("Nombre no encontrado");

        String direccionSucursal = sucursalRepository.findById(Long.valueOf(idSucursal))
                .map(SucursalEntity::getUbicacion)
                .orElse("Dirección no encontrada");

        EmpleadoEntity empleado = empleadoRepository.findById(Long.valueOf(idEmpleado)).orElse(null);
        if (empleado != null) {
            model.addAttribute("nombreEmpleado", empleado.getNombre());
            model.addAttribute("idEmpleado", empleado.getId());
        }

        List<ProductosPedidosEntity> productosPedidos = productosPedidosRepository.findAll();


        model.addAttribute("idSucursal", idSucursal);
        model.addAttribute("nombreSucursal", nombreSucursal);
        model.addAttribute("direccionSucursal", direccionSucursal);
        model.addAttribute("productosPedidos", productosPedidos);
        model.addAttribute("proveedores", proveedorRepository.findAll());
        model.addAttribute("productos", productoRepository.findAll());

        return "proveedor";
    }


    @GetMapping("/proveedor/nuevo")
    public String mostrarFormularioProveedor(@RequestParam("idSucursal") Integer idSucursal, Model model) {
        model.addAttribute("proveedor", new ProveedorEntity());
        model.addAttribute("idSucursal", idSucursal);
        return "nuevo_proveedor";
    }


    @PostMapping("/proveedor/guardar")
    public String guardarProveedor(@RequestParam("idSucursal") Integer idSucursal,
                                   @Valid @ModelAttribute("proveedor") ProveedorEntity proveedor,
                                   BindingResult result, Model model) {
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println("Error: " + error.getDefaultMessage());
            }
            model.addAttribute("contenido", "Error al guardar proveedor");
            return "nuevo_proveedor";
        }


        proveedorRepository.save(proveedor);
        model.addAttribute("contenido", "Proveedor Creado Exitosamente");
        return "redirect:/proveedor/pedidos?idSucursal=" + idSucursal;
    }


    @PostMapping("/productos-pedidos/actualizar")
    @ResponseBody
    public ResponseEntity<String> actualizarCantidadProducto(
            @RequestParam("idProducto") Long idProducto,
            @RequestParam("idSucursal") Long idSucursal,
            @RequestParam("cantidad") Integer cantidad) {


        IdProductoSucursal id = new IdProductoSucursal(idProducto, idSucursal);
        ProductosPedidosEntity producto = productosPedidosRepository.findById(id)
                .orElse(null);


        if (producto == null) {
            return ResponseEntity.badRequest().body("Producto no encontrado");
        }


        producto.setCantidad(cantidad);
        productosPedidosRepository.save(producto);


        return ResponseEntity.ok("Cantidad actualizada");
    }


    @PostMapping("/productos-pedidos/eliminar")
    @ResponseBody
    public ResponseEntity<String> eliminarProductoPedido(
            @RequestParam("idProducto") Long idProducto,
            @RequestParam("idSucursal") Long idSucursal) {


        IdProductoSucursal id = new IdProductoSucursal(idProducto, idSucursal);
        if (productosPedidosRepository.existsById(id)) {
            productosPedidosRepository.deleteById(id);
            return ResponseEntity.ok("Producto eliminado correctamente");
        }
        return ResponseEntity.badRequest().body("Producto no encontrado");
    }


    @PostMapping("/productos-pedidos/agregar")
    @ResponseBody
    public ResponseEntity<String> agregarProductoPedido(
            @RequestParam("idProducto") Long idProducto,
            @RequestParam("idSucursal") Long idSucursal,
            @RequestParam("cantidad") Integer cantidad) {


        IdProductoSucursal id = new IdProductoSucursal(idProducto, idSucursal);
        if (productosPedidosRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("El producto ya existe en el pedido para esta sucursal");
        }


        ProductosPedidosEntity nuevoProducto = new ProductosPedidosEntity();
        nuevoProducto.setId(id);
        nuevoProducto.setCantidad(cantidad);


        productosPedidosRepository.save(nuevoProducto);


        return ResponseEntity.ok("Producto agregado correctamente");
    }
    @PostMapping("/proveedor/enviar-pedido")
    @ResponseBody
    public ResponseEntity<String> enviarPedidoPorCorreo(@RequestParam("idSucursal") Long idSucursal,
                                                        @RequestParam("idProveedor") Long idProveedor,
                                                        @RequestParam("idEmpleado") Integer idEmpleado,
                                                        @RequestParam("nombreEmpleado") String nombreEmpleado,
                                                        @RequestParam("nombreSucursal") String nombreSucursal,
                                                        @RequestParam("direccionSucursal") String direccionSucursal) {
        List<ProductosPedidosEntity> productosPedidos = productosPedidosRepository.findById_IdSucursal(idSucursal);
        if (productosPedidos.isEmpty()) {
            return ResponseEntity.badRequest().body("No hay productos seleccionados para esta sucursal");
        }

        ProveedorEntity proveedor = proveedorRepository.findById(idProveedor).orElse(null);
        if (proveedor == null || proveedor.getCorreo() == null) {
            return ResponseEntity.badRequest().body("Proveedor no encontrado o no tiene correo");
        }

        ByteArrayOutputStream pdfStream = generarPdfDeProductosPedidos(productosPedidos, proveedor, nombreEmpleado, nombreSucursal, direccionSucursal);

        try {
            enviarCorreoConAdjunto(
                    proveedor.getCorreo(),
                    "Pedido de productos",
                    "Adjunto encontrarás el pedido.",
                    pdfStream
            );


            // Crear un nuevo pedido
            PedidoEntity nuevoPedido = new PedidoEntity();
            EmpleadoEntity empleado = empleadoRepository.findById(Long.valueOf(idEmpleado)).orElse(null);
            if (empleado != null) {
                nuevoPedido.setEmpleado(empleado);
            }
            nuevoPedido.setFecha(LocalDateTime.now());
            nuevoPedido.setProveedor(proveedor);
            pedidoRepository.save(nuevoPedido);

            // Crear los detalles del pedido
            for (ProductosPedidosEntity productoPedido : productosPedidos) {
                DetallePedidoEntity detalle = new DetallePedidoEntity();
                detalle.setPedido(nuevoPedido);
                detalle.setProducto(productoPedido.getProducto());
                detalle.setCantidad(productoPedido.getCantidad());
                detallePedidoRepository.save(detalle);
            }


            // Actualizar inventario después de enviar el correo
            for (ProductosPedidosEntity pedido : productosPedidos) {
                Long IdProducto = pedido.getProducto().getId();
                Long IdSucursal = pedido.getSucursal().getId();
                int cantidad = pedido.getCantidad();

                InventarioEntity inventario = inventarioRepository.findById(new IdProductoSucursal(IdProducto, IdSucursal)).orElse(null);

                if (inventario != null) {
                    inventario.setStock(inventario.getStock() + cantidad);
                    inventarioRepository.save(inventario);
                }
            }

            // Eliminar productos pedidos después de enviar el correo
            productosPedidosRepository.deleteAll(productosPedidos);
            return ResponseEntity.ok("Pedido enviado por correo a " + proveedor.getCorreo());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al enviar correo: " + e.getMessage());
        }
    }


    private void addCellHeader(PdfPTable table, String texto, Font font, Color backgroundColor) {
        com.lowagie.text.pdf.PdfPCell cell = new com.lowagie.text.pdf.PdfPCell(new Paragraph(texto, font));
        cell.setBackgroundColor(backgroundColor);
        cell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private ByteArrayOutputStream generarPdfDeProductosPedidos(List<ProductosPedidosEntity> productosPedidos, ProveedorEntity proveedor, String nombreEmpleado, String nombreSucursal, String direccionSucursal) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            try {
                InputStream logoStream = getClass().getResourceAsStream("/static/img/abarroteslogo.png");
                if (logoStream != null) {
                    com.lowagie.text.Image logo = com.lowagie.text.Image.getInstance(logoStream.readAllBytes());
                    logo.setAlignment(Image.ALIGN_CENTER);
                    logo.scaleToFit(250, 250);
                    logo.setSpacingAfter(10f);
                    document.add(logo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            document.add(new Paragraph(" "));

            Font tituloFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 15, Color.BLACK);
            document.add(new Paragraph("Pedido al Proveedor: " + proveedor.getNombre(), tituloFont));

            document.add(new Paragraph(" "));

            document.add(new Paragraph("Solicitado por: " + nombreEmpleado));
            document.add(new Paragraph("Sucursal: " + nombreSucursal));
            document.add(new Paragraph("Dirección: " + direccionSucursal));
            document.add(new Paragraph("Fecha: " + LocalDate.now()));
            document.add(new Paragraph("Hora: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
            document.add(new Paragraph("\nPedido:\n", new Font(Font.HELVETICA, 14, Font.BOLD)));
            document.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4f, 2f});

            Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Color headerColor = new Color(255, 204, 0);

            addCellHeader(table, "Producto", headerFont, headerColor);
            addCellHeader(table, "Cantidad", headerFont, headerColor);

            for (ProductosPedidosEntity ppe : productosPedidos) {
                com.lowagie.text.pdf.PdfPCell productoCell = new com.lowagie.text.pdf.PdfPCell(new Paragraph(ppe.getProducto().getNombre()));
                productoCell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                productoCell.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
                productoCell.setPadding(5);
                table.addCell(productoCell);

                com.lowagie.text.pdf.PdfPCell cantidadCell = new com.lowagie.text.pdf.PdfPCell(new Paragraph(String.valueOf(ppe.getCantidad())));
                cantidadCell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                cantidadCell.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
                cantidadCell.setPadding(5);
                table.addCell(cantidadCell);
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }



    @Autowired
    private org.springframework.mail.javamail.JavaMailSender mailSender;


    @Value("${correo.usuario}")
    private String correoEmisor;


    private void enviarCorreoConAdjunto(String destinatario, String asunto, String mensaje, ByteArrayOutputStream adjunto) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);


        helper.setFrom(correoEmisor);
        helper.setTo(destinatario);
        helper.setSubject(asunto);
        helper.setText(mensaje);
        helper.addAttachment("pedido.pdf", new ByteArrayResource(adjunto.toByteArray()));


        mailSender.send(message);
    }








}
