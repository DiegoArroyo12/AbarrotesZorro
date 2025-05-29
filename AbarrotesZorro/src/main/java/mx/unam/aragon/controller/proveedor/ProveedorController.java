package mx.unam.aragon.controller.proveedor;


import jakarta.validation.Valid;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import mx.unam.aragon.model.entity.ClienteEntity;
import mx.unam.aragon.model.entity.ProductosPedidosEntity;
import mx.unam.aragon.model.entity.ProveedorEntity;
import mx.unam.aragon.model.entity.SucursalEntity;
import mx.unam.aragon.model.entity.seriales.IdProductoSucursal;
import mx.unam.aragon.repository.ProductoRepository;
import mx.unam.aragon.repository.ProductosPedidosRepository;
import mx.unam.aragon.repository.ProveedorRepository;
import mx.unam.aragon.repository.SucursalRepository;
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
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


import java.io.ByteArrayOutputStream;
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
    ProductoRepository productoRepository;


    @Value("${imagenes.ruta}")
    private String imagenesPath;


    @GetMapping("/proveedor/pedidos")
    public String mostrarProveedor(@RequestParam("idSucursal") Integer idSucursal,
                                   Model model) {


        String nombreSucursal = sucursalRepository.findById(Long.valueOf(idSucursal))
                .map(SucursalEntity::getNombre)
                .orElse("Nombre no encontrado");


        List<ProductosPedidosEntity> productosPedidos = productosPedidosRepository.findAll();


        model.addAttribute("idSucursal", idSucursal);
        model.addAttribute("nombreSucursal", nombreSucursal);
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
    public ResponseEntity<String> enviarPedidoPorCorreo(@RequestParam("idProveedor") Long idProveedor,
                                                        @RequestParam("idSucursal") Long idSucursal) {
        // 1. Obtener proveedor
        ProveedorEntity proveedor = proveedorRepository.findById(idProveedor).orElse(null);
        if (proveedor == null || proveedor.getCorreo() == null) {
            return ResponseEntity.badRequest().body("Proveedor no válido o sin correo");
        }


        // 2. Obtener productos del pedido
        List<ProductosPedidosEntity> productosPedidos = productosPedidosRepository.findById_IdSucursal(idSucursal);
        if (productosPedidos.isEmpty()) {
            return ResponseEntity.badRequest().body("No hay productos para este pedido");
        }


        // 3. Generar PDF
        ByteArrayOutputStream pdfStream = generarPdfDePedido(productosPedidos);


        // 4. Enviar correo
        try {
            enviarCorreoConAdjunto(proveedor.getCorreo(), "Pedido de productos", "Adjunto encontrarás el pedido.", pdfStream);
            return ResponseEntity.ok("Pedido enviado por correo a " + proveedor.getCorreo());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error al enviar correo: " + e.getMessage());
        }
    }
    private ByteArrayOutputStream generarPdfDePedido(List<ProductosPedidosEntity> productos) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            com.lowagie.text.Document document = new com.lowagie.text.Document();
            com.lowagie.text.pdf.PdfWriter.getInstance(document, out);
            document.open();
            document.add(new com.lowagie.text.Paragraph("Lista de Productos del Pedido"));


            for (ProductosPedidosEntity producto : productos) {
                String nombre = producto.getProducto().getNombre();
                int cantidad = producto.getCantidad();
                document.add(new com.lowagie.text.Paragraph(nombre + " - Cantidad: " + cantidad));
            }


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
