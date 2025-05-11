package mx.unam.aragon.controller.almacen;

import mx.unam.aragon.model.entity.AlmacenEntity;
import mx.unam.aragon.repository.AlmacenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AlmacenController {

    @Autowired
    AlmacenRepository almacenRepository;

    @GetMapping("/almacenes")
    public List<AlmacenEntity> obtenerAlmacenes() {
        return almacenRepository.findAll();
    }
}