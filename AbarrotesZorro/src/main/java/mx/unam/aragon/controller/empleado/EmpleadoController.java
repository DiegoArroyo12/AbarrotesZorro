package mx.unam.aragon.controller.empleado;

import mx.unam.aragon.model.entity.EmpleadoEntity;
import mx.unam.aragon.model.entity.RolEntity;
import mx.unam.aragon.model.entity.SucursalEntity;
import mx.unam.aragon.repository.EmpleadoRepository;
import mx.unam.aragon.repository.RolRepository;
import mx.unam.aragon.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;

@Controller
public class EmpleadoController {

    @Autowired
    EmpleadoRepository empleadoRepository;

    @Autowired
    SucursalRepository sucursalRepository;

    @Autowired
    RolRepository rolRepository;

    @GetMapping("/empleados")
    public String mostrarEmpleados(@RequestParam("idSucursal") Integer idSucursal, Model model) {
        // Lista de empleados de la sucursal
        List<EmpleadoEntity> empleados = empleadoRepository.findBySucursalIdAndActivoTrue(idSucursal);

        // Nombre de sucursal
        String nombreSucursal = sucursalRepository.findById(Long.valueOf(idSucursal)).map(SucursalEntity::getNombre).orElse("Nombre no encontrado");

        // Roles
        List<RolEntity> roles = rolRepository.findAll();

        model.addAttribute("sucursalEmpleado", nombreSucursal);
        model.addAttribute("empleados", empleados);
        model.addAttribute("roles", roles);

        return "empleados";
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
