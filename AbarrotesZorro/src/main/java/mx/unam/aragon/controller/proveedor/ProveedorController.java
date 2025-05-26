package mx.unam.aragon.controller.proveedor;

import mx.unam.aragon.model.entity.ProductosPedidosEntity;
import mx.unam.aragon.model.entity.SucursalEntity;
import mx.unam.aragon.model.entity.seriales.IdProductoSucursal;
import mx.unam.aragon.repository.ProductosPedidosRepository;
import mx.unam.aragon.repository.ProveedorRepository;
import mx.unam.aragon.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ProveedorController {

    @Autowired
    ProveedorRepository proveedorRepository;

    @Autowired
    SucursalRepository sucursalRepository;

    @Autowired
    ProductosPedidosRepository productosPedidosRepository;

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

        return "proveedor";
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
}
