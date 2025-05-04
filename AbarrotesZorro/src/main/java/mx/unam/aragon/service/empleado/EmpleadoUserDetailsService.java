package mx.unam.aragon.service.empleado;

import mx.unam.aragon.model.entity.EmpleadoEntity;
import mx.unam.aragon.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmpleadoUserDetailsService implements UserDetailsService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EmpleadoEntity empleado = (EmpleadoEntity) empleadoRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return User.builder()
                .username(empleado.getUsuario())
                .password(empleado.getPassword_hash())
                .roles("Gerente")
                .build();
    }
}
