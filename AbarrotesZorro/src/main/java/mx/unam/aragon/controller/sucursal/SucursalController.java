package mx.unam.aragon.controller.sucursal;

import mx.unam.aragon.model.entity.SucursalEntity;
import mx.unam.aragon.repository.SucursalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SucursalController {

    @Autowired
    SucursalRepository sucursalRepository;

    @GetMapping("/sucursales")
    public List<SucursalEntity> obtenerSucursales() {
        return sucursalRepository.findAll();
    }
}