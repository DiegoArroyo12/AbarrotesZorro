package mx.unam.aragon.controller.inventario;

import jakarta.validation.Valid;
import mx.unam.aragon.model.entity.EmpleadoEntity;
import mx.unam.aragon.model.entity.InventarioEntity;
import mx.unam.aragon.model.entity.ProductoEntity;
import mx.unam.aragon.model.entity.SucursalEntity;
import mx.unam.aragon.model.entity.seriales.IdProductoSucursal;
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
import org.springframework.security.core.Authentication;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Optional;

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
    public String mostrarInventario(@RequestParam("idSucursal") Integer idSucursal,
                                    Model model,
                                    Authentication authentication) {
        model.addAttribute("inventario", inventarioRepository.findBySucursal(idSucursal));
        model.addAttribute("idSucursal", idSucursal);

        String nombreSucursal = sucursalRepository.findById(Long.valueOf(idSucursal))
                .map(SucursalEntity::getNombre)
                .orElse("Nombre no encontrado");
        List<ProductoInventarioView> productos = inventarioRepository.findProductosPorSucursal(idSucursal);

        model.addAttribute("nombreSucursal", nombreSucursal);
        model.addAttribute("productos", productos);

        String username = authentication.getName();
        EmpleadoEntity empleado = empleadoRepository.findByUsuario(username).orElse(null);
        if (empleado != null && empleado.getSucursal() != null) {
            model.addAttribute("sucursalEmpleadoId", empleado.getSucursal().getId());
            model.addAttribute("nombreEmpleado", empleado.getNombre());
        } else {
            model.addAttribute("sucursalEmpleadoId", -1); // Valor por defecto si no tiene sucursal
        }

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

            ProductoEntity productoGuardado;
            Optional<ProductoEntity> productoExistente = productoRepository.findByNombre(producto.getNombre());

            if (productoExistente.isPresent()) {
                productoGuardado = productoExistente.get();
                // Solo actualizar la imagen si se subió una nueva
                if (producto.getImagen() != null) {
                    productoGuardado.setImagen(producto.getImagen());
                }
            } else {
                productoGuardado = productoRepository.save(producto);
            }

            IdProductoSucursal idProductoSucursal = new IdProductoSucursal(productoGuardado.getId(), Long.valueOf(idSucursal));
            InventarioEntity inventarioExistente = inventarioRepository.findById(idProductoSucursal).orElse(null);

            if (inventarioExistente == null) {
                InventarioEntity inventario = new InventarioEntity();
                inventario.setId(idProductoSucursal);
                inventario.setProducto(productoGuardado);
                inventario.setSucursal(sucursalRepository.findById(Long.valueOf(idSucursal)).orElse(null));
                inventario.setStock(stock);
                inventarioRepository.save(inventario);
            } else {
                // Solo actualiza el stock si quieres permitirlo
                inventarioExistente.setStock(stock);
                inventarioRepository.save(inventarioExistente);
            }

            List<SucursalEntity> otrasSucursales = sucursalRepository.findAll()
                    .stream()
                    .filter(s -> !s.getId().equals(Long.valueOf(idSucursal)))
                    .toList();

            for (SucursalEntity sucursal : otrasSucursales) {
                IdProductoSucursal idAdicional = new IdProductoSucursal(productoGuardado.getId(), sucursal.getId());

                if (!inventarioRepository.existsById(idAdicional)) {
                    InventarioEntity inventarioAdicional = new InventarioEntity();
                    inventarioAdicional.setId(idAdicional);
                    inventarioAdicional.setProducto(productoGuardado);
                    inventarioAdicional.setSucursal(sucursal);
                    inventarioAdicional.setStock(0);
                    inventarioRepository.save(inventarioAdicional);
                }
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
        String username = authentication.getName();
        EmpleadoEntity empleado = empleadoRepository.findByUsuario(username).orElse(null);

        if (empleado != null && empleado.getRoles().stream().anyMatch(rol -> "Gerente".equals(rol.getNombre()))) {
            Long idSucursalEmpleado = empleado.getSucursal().getId();
            if (!idSucursalEmpleado.equals(Long.valueOf(idSucursal))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permiso para exportar esta sucursal.");
                return;
            }
        }

        List<ProductoInventarioView> productos = inventarioRepository.findProductosPorSucursal(idSucursal);
        String nombreSucursal = sucursalRepository.findById(Long.valueOf(idSucursal))
                .map(SucursalEntity::getNombre)
                .orElse("Sucursal");

        String nombreEmpleado = empleado != null ? empleado.getNombre() : "Empleado desconocido";
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String fecha = ahora.format(formatter);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Inventario");

        sheet.createRow(0).createCell(0).setCellValue("Sucursal: " + nombreSucursal);
        sheet.createRow(1).createCell(0).setCellValue("Empleado: " + nombreEmpleado);
        sheet.createRow(2).createCell(0).setCellValue("Fecha y Hora: " + fecha);
        Row headerRow = sheet.createRow(4);
        headerRow.createCell(0).setCellValue("Producto");
        headerRow.createCell(1).setCellValue("Stock");
        headerRow.createCell(2).setCellValue("Precio");

        int rowNum = 5;
        for (ProductoInventarioView producto : productos) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(producto.getNombre());
            row.createCell(1).setCellValue(producto.getStock());
            row.createCell(2).setCellValue(producto.getPrecio());
        }

        for (int i = 0; i <= 2; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=inventario_sucursal_" + idSucursal + ".xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @DeleteMapping("/inventario/eliminar")
    @ResponseBody
    public Map<String, String> eliminarProducto(@RequestParam("id") Long idProducto,
                                                @RequestParam("idSucursal") Long idSucursal) {
        Map<String, String> respuesta = new HashMap<>();
        try {
            IdProductoSucursal id = new IdProductoSucursal(idProducto, idSucursal);
            inventarioRepository.deleteById(id);
            respuesta.put("status", "ok");
            respuesta.put("mensaje", "Producto eliminado del inventario de esta sucursal");
        } catch (Exception e) {
            respuesta.put("status", "error");
            respuesta.put("mensaje", "No se pudo eliminar el producto");
        }
        return respuesta;
    }

}
