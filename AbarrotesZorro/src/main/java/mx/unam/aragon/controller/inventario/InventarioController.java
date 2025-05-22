package mx.unam.aragon.controller.inventario;

import jakarta.validation.Valid;
import mx.unam.aragon.model.entity.EmpleadoEntity;
import mx.unam.aragon.model.entity.InventarioEntity;
import mx.unam.aragon.model.entity.ProductoEntity;
import mx.unam.aragon.model.entity.SucursalEntity;
import mx.unam.aragon.model.entity.seriales.IdInventario;
import mx.unam.aragon.model.entity.view.ProductoInventarioView;
import mx.unam.aragon.repository.EmpleadoRepository;
import mx.unam.aragon.repository.ProductoRepository;
import mx.unam.aragon.repository.SucursalRepository;
import mx.unam.aragon.repository.InventarioRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class InventarioController {

    @Autowired
    InventarioRepository inventarioRepository;

    @Autowired
    SucursalRepository sucursalRepository;

    @Autowired
    ProductoRepository productoRepository;

    @Autowired
    EmpleadoRepository empleadoRepository;


    @Value("${imagenes.ruta}")
    private String imagenesPath;

    @GetMapping("/inventario")
    public String mostrarInventario(@RequestParam("idSucursal") Integer idSucursal, Model model) {
        model.addAttribute("inventario", inventarioRepository.findBySucursal(idSucursal));
        model.addAttribute("idSucursal", idSucursal);

        String nombreSucursal = sucursalRepository.findById(Long.valueOf(idSucursal)).map(SucursalEntity::getNombre).orElse("Nombre no encontrado");
        List<ProductoInventarioView> productos = inventarioRepository.findProductosPorSucursal(idSucursal);

        model.addAttribute("nombreSucursal", nombreSucursal);
        model.addAttribute("productos", productos);
        model.addAttribute("nombreSucursal", nombreSucursal);
        return "inventario";
    }

    @GetMapping("/inventario/nuevo")
    public String nuevoProducto(@RequestParam("idSucursal") Integer idSucursal, Model model) {
        ProductoEntity producto = new ProductoEntity();
        model.addAttribute("producto", producto);
        model.addAttribute("idSucursal", idSucursal);
        return "nuevo_producto";
    }

    @PostMapping("/inventario/guardar")
    public String guardarInventario(@Valid @ModelAttribute("producto") ProductoEntity producto,
                                    @RequestParam("archivoImagen") MultipartFile archivoImagen,
                                    @RequestParam("idSucursal") Integer idSucursal,
                                    @RequestParam("stock") Integer stock,
                                    BindingResult result, Model model) {
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println("Error: " + error.getDefaultMessage());
            }
            model.addAttribute("contenido", "Error al guardar producto");
            return "nuevo_producto";
        }
        try {
            if (archivoImagen != null && !archivoImagen.isEmpty()) {
                String nombreArchivo = UUID.randomUUID() + "_" + archivoImagen.getOriginalFilename();
                Path ruta = Paths.get(imagenesPath, nombreArchivo);
                Files.write(ruta, archivoImagen.getBytes());
                producto.setImagen("/img/productos/" + nombreArchivo);
            }

            ProductoEntity productoGuardado = productoRepository.save(producto);

            InventarioEntity inventario = new InventarioEntity();
            IdInventario idInventario = new IdInventario(productoGuardado.getId(), Long.valueOf(idSucursal));
            inventario.setId(idInventario);
            inventario.setProducto(productoGuardado);
            inventario.setSucursal(sucursalRepository.findById(Long.valueOf(idSucursal)).orElse(null));
            inventario.setStock(stock);
            inventarioRepository.save(inventario);

            List<SucursalEntity> otrasSucursales = sucursalRepository.findAll()
                    .stream()
                    .filter(s -> !s.getId().equals(Long.valueOf(idSucursal)))
                    .toList();

            for (SucursalEntity sucursal : otrasSucursales) {
                InventarioEntity inventarioAdicional = new InventarioEntity();
                IdInventario idAdicional = new IdInventario(productoGuardado.getId(), sucursal.getId());
                inventarioAdicional.setId(idAdicional);
                inventarioAdicional.setProducto(productoGuardado);
                inventarioAdicional.setSucursal(sucursal);
                inventarioAdicional.setStock(0);
                inventarioRepository.save(inventarioAdicional);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/inventario?idSucursal=" + idSucursal;
    }

    @PostMapping("/inventario/actualizar")
    @ResponseBody
    public Map<String, String> actualizarProducto(@RequestParam Integer id,
                                                  @RequestParam String nombre,
                                                  @RequestParam Integer stock,
                                                  @RequestParam Double precio,
                                                  @RequestParam Integer idSucursal,
                                                  @RequestParam(value = "imagen", required = false)MultipartFile imagen) {
        Map<String, String> respuesta = new HashMap<>();
        ProductoEntity producto = productoRepository.findById(Long.valueOf(id)).orElse(null);
        InventarioEntity inventario = inventarioRepository.findByProductoIdAndSucursalId(id, idSucursal);

        if (producto != null && inventario != null) {
            producto.setNombre(nombre);
            producto.setPrecio(precio);
            inventario.setStock(stock);

            // Guardar Imagen
            if (imagen != null && !imagen.isEmpty()) {
                try {
                    String nombreArchivo = UUID.randomUUID() + "_" + imagen.getOriginalFilename();
                    Path ruta = Paths.get(imagenesPath, nombreArchivo);
                    Files.write(ruta, imagen.getBytes());

                    producto.setImagen("/img/productos/" + nombreArchivo);
                    respuesta.put("nuevaImagen", producto.getImagen());
                } catch (IOException e) {
                    e.printStackTrace();
                    respuesta.put("status", "error");
                    respuesta.put("mensaje", "Error al guardar la imagen");
                    return respuesta;
                }
            }

            productoRepository.save(producto);
            inventarioRepository.save(inventario);
            respuesta.put("status", "ok");
            respuesta.put("mensaje", "Producto actualizado correctamente");
        } else {
            respuesta.put("status", "error");
            respuesta.put("mensaje", "No se pudo actualizar el producto");
        }
        return respuesta;
    }
    @GetMapping("/inventario/descargar-excel")
    public void descargarExcel(@RequestParam("idSucursal") Integer idSucursal,
                               HttpServletResponse response,
                               Authentication authentication) throws IOException {
        List<ProductoInventarioView> productos = inventarioRepository.findProductosPorSucursal(idSucursal);
        String nombreSucursal = sucursalRepository.findById(Long.valueOf(idSucursal))
                .map(SucursalEntity::getNombre)
                .orElse("Sucursal");

        String username = authentication.getName();
        String nombreEmpleado = empleadoRepository.findByUsuario(username)
                .map(EmpleadoEntity::getNombre)
                .orElse("Empleado desconocido");

        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fecha = ahora.format(formatter);
        //String fecha = java.time.LocalDateTime.now().toString();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Inventario");

        sheet.createRow(0).createCell(0).setCellValue("Sucursal: " + nombreSucursal);
        sheet.createRow(1).createCell(0).setCellValue("Empleado: " + nombreEmpleado);
        sheet.createRow(2).createCell(0).setCellValue("Fecha: " + fecha);

        Row headerRow = sheet.createRow(4);
        headerRow.createCell(0).setCellValue("Nombre");
        headerRow.createCell(1).setCellValue("Stock");
        headerRow.createCell(2).setCellValue("Precio");
        headerRow.createCell(3).setCellValue("Imagen");

        Drawing<?> drawing = sheet.createDrawingPatriarch();
        CreationHelper helper = workbook.getCreationHelper();

        int rowNum = 5;
        for (ProductoInventarioView producto : productos) {
            Row row = sheet.createRow(rowNum);
            row.setHeightInPoints(60);
            row.createCell(0).setCellValue(producto.getNombre());
            row.createCell(1).setCellValue(producto.getStock());
            row.createCell(2).setCellValue(producto.getPrecio());

            String imagenUrl = producto.getImagen();
            String nombreArchivo = imagenUrl.substring(imagenUrl.lastIndexOf("/") + 1);

            ClassPathResource imgFile = new ClassPathResource("static/img/productos/" + nombreArchivo);
            if (imgFile.exists()) {
                try (InputStream is = imgFile.getInputStream()) {
                    byte[] bytes = is.readAllBytes();
                    int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

                    ClientAnchor anchor = helper.createClientAnchor();
                    anchor.setCol1(3);
                    anchor.setRow1(rowNum);
                    anchor.setCol2(4);
                    anchor.setRow2(rowNum + 1);

                    Picture pict = drawing.createPicture(anchor, pictureIdx);
                    pict.resize(1, 1);
                }
            }
            rowNum++;
        }

        for (int i = 0; i <= 2; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=inventario_sucursal_" + idSucursal + ".xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }






}
