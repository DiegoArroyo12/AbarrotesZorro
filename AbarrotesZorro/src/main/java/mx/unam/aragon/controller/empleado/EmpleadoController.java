package mx.unam.aragon.controller.empleado;

import jakarta.validation.Valid;
import mx.unam.aragon.model.entity.EmpleadoEntity;
import mx.unam.aragon.model.entity.RolEntity;
import mx.unam.aragon.model.entity.SucursalEntity;
import mx.unam.aragon.repository.EmpleadoRepository;
import mx.unam.aragon.repository.RolRepository;
import mx.unam.aragon.repository.SucursalRepository;
import mx.unam.aragon.service.empleado.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmpleadoController {

    @Autowired
    EmpleadoRepository empleadoRepository;

    @Autowired
    SucursalRepository sucursalRepository;

    @Autowired
    RolRepository rolRepository;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/empleados")
    public String mostrarEmpleados(@RequestParam("idSucursal") Integer idSucursal, Model model) {
        // Lista de empleados de la sucursal
        List<EmpleadoEntity> empleados = empleadoRepository.findBySucursalIdAndActivoTrue(idSucursal);

        // Nombre de sucursal
        String nombreSucursal = sucursalRepository.findById(Long.valueOf(idSucursal)).map(SucursalEntity::getNombre).orElse("Nombre no encontrado");

        // Roles
        List<RolEntity> roles = rolRepository.findAll();

        model.addAttribute("sucursalEmpleado", nombreSucursal);
        model.addAttribute("idSucursal", idSucursal);
        model.addAttribute("empleados", empleados);
        model.addAttribute("roles", roles);

        return "empleados";
    }

    @GetMapping("/empleados/nuevo")
    public String altaEmpleado(@RequestParam("idSucursal") Integer idSucursal, Model model) {
        EmpleadoEntity empleado = new EmpleadoEntity();
        model.addAttribute("empleado", empleado);

        // Sucursales
        model.addAttribute("sucursales", sucursalRepository.findAll());
        // Roles
        model.addAttribute("roles", rolRepository.findAll());
        model.addAttribute("idSucursal", idSucursal);
        return "nuevo_empleado";
    }

    @PostMapping("/empleados/guardar")
    public String guardarEmpleado(@Valid @ModelAttribute("empleado") EmpleadoEntity empleado,
                                  @RequestParam("sucursal") Long sucursalId,
                                  @RequestParam("rol") Long rolId,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                System.out.println("Error: " + error.getDefaultMessage());
            }
            model.addAttribute("contenido", "Error al guardar empleado");
            return "nuevo_empleado";
        }

        SucursalEntity sucursal = sucursalRepository.findById(sucursalId).orElse(null);
        RolEntity rol = rolRepository.findById(rolId).orElse(null);

        if (sucursal != null && rol != null) {
            empleado.setSucursal(sucursal);
            empleado.setActivo(true);

            // Encriptar la contraseña antes de guardar
            empleado.setPassword_hash(passwordEncoder.encode(empleado.getPassword_hash()));

            empleado.getRoles().add(rol);
            empleadoService.save(empleado);
        }

        model.addAttribute("contenido", "Empleado Creado Exitosamente");
        return "redirect:/empleados?idSucursal=" + sucursalId;
    }

    @PostMapping("/empleados/actualizar")
    @ResponseBody
    public String actualizarEmpleado(@RequestParam Integer id,
                                     @RequestParam String nombre,
                                     @RequestParam String usuario,
                                     @RequestParam Long rol) {
        EmpleadoEntity empleado = empleadoRepository.findById(Long.valueOf(id)).orElse(null);
        if (empleado != null) {
            empleado.setNombre(nombre);
            empleado.setUsuario(usuario);

            // Buscar el rol por ID
            RolEntity nuevoRol = rolRepository.findById(rol).orElse(null);
            if (nuevoRol != null) {
                empleado.getRoles().clear(); // Borra el rol actual
                empleado.getRoles().add(nuevoRol); // Agrega el nuevo rol
            }

            empleadoRepository.save(empleado);
            return "Empleado actualizado correctamente";
        }
        return "Error al actualizar empleado";
    }

    @DeleteMapping("/empleados/eliminar")
    @ResponseBody
    public String inactivarEmpleado(@RequestParam Long id) {
        try {
            EmpleadoEntity empleado = empleadoRepository.findById(id).orElse(null);
            if (empleado != null) {
                empleado.setActivo(false);
                empleadoRepository.save(empleado);
                return "Empleado inactivado correctamente";
            } else {
                return "Empleado no encontrado";
            }
        } catch (Exception e) {
            return "Error al inactivar empleado: " + e.getMessage();
        }
    }
}
