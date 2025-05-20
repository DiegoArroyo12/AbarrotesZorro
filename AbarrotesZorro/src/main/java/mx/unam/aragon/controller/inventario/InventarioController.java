package mx.unam.aragon.controller.inventario;

import jakarta.validation.Valid;
import mx.unam.aragon.model.entity.InventarioEntity;
import mx.unam.aragon.model.entity.ProductoEntity;
import mx.unam.aragon.model.entity.SucursalEntity;
import mx.unam.aragon.model.entity.seriales.IdInventario;
import mx.unam.aragon.model.entity.view.ProductoInventarioView;
import mx.unam.aragon.repository.ProductoRepository;
import mx.unam.aragon.repository.SucursalRepository;
import mx.unam.aragon.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
            // Guardar imagen si fue subida
            if (archivoImagen != null && !archivoImagen.isEmpty()) {
                String nombreArchivo = UUID.randomUUID() + "_" + archivoImagen.getOriginalFilename();
                Path ruta = Paths.get(imagenesPath, nombreArchivo);
                Files.write(ruta, archivoImagen.getBytes());
                producto.setImagen("/img/productos/" + nombreArchivo);
            }

            // Guardar producto
            ProductoEntity productoGuardado = productoRepository.save(producto);

            // Crear y guardar inventario
            InventarioEntity inventario = new InventarioEntity();
            IdInventario idInventario = new IdInventario(productoGuardado.getId(), Long.valueOf(idSucursal));
            inventario.setId(idInventario);
            inventario.setProducto(productoGuardado);
            inventario.setSucursal(sucursalRepository.findById(Long.valueOf(idSucursal)).orElse(null));
            inventario.setStock(stock);
            inventarioRepository.save(inventario);

            // Guardar en las demás sucursales con stock 0
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
}
