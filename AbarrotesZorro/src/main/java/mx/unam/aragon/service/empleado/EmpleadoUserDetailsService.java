package mx.unam.aragon.service.empleado;

import mx.unam.aragon.model.entity.EmpleadoEntity;
import mx.unam.aragon.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoUserDetailsService implements UserDetailsService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EmpleadoEntity empleado = empleadoRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Verificar si el empleado está activo
        if (!empleado.isActivo()) {
            throw new BadCredentialsException("El empleado fue dado de baja");
        }

        List<GrantedAuthority> authorities = empleado.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                empleado.getUsuario(),
                empleado.getPassword_hash(),
                authorities
        );
    }
}
