package mx.unam.aragon.controller.cliente;

import jakarta.validation.Valid;
import mx.unam.aragon.model.entity.ClienteEntity;
import mx.unam.aragon.service.cliente.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("nuevo-cliente")
    public String altaCliente(Model model) {
        ClienteEntity cliente = new ClienteEntity();
        model.addAttribute("cliente", cliente);
        model.addAttribute("contenido", "Alta de Cliente");
        return "cliente/nuevo_cliente"; // ← CORREGIDO: guion bajo
    }

    @PostMapping("guardar")
    public String guardarCliente(@Valid @ModelAttribute("cliente") ClienteEntity cliente,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println("Error: " + error.getDefaultMessage());
            }
            model.addAttribute("contenido", "Error al guardar cliente");
            return "cliente/nuevo_cliente"; // ← CORREGIDO también aquí
        }

        clienteService.save(cliente);
        model.addAttribute("contenido", "Cliente almacenado con éxito");
        return "cliente/nuevo_cliente"; // ← Y aquí también
    }

    @GetMapping("lista-clientes")
    public String listarClientes(Model model) {
        List<ClienteEntity> lista = clienteService.findAll();
        model.addAttribute("lista", lista);
        model.addAttribute("contenido", "Lista de Clientes");
        return "cliente/lista_clientes"; // ← Asegúrate de que también exista este archivo
    }

    @GetMapping("eliminar-cliente/{correo}")
    public String eliminarCliente(@PathVariable("correo") String correo) {
        clienteService.deleteById(correo);
        return "redirect:/cliente/lista-clientes";
    }

    @GetMapping("modificar-cliente/{correo}")
    public String modificarCliente(@PathVariable("correo") String correo, Model model) {
        ClienteEntity cliente = clienteService.findById(correo);
        model.addAttribute("cliente", cliente);
        model.addAttribute("contenido", "Modificar Cliente");
        return "cliente/nuevo_cliente"; // ← Igual aquí
    }
}
