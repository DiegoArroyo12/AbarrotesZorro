package mx.unam.aragon.service.empleado;

import lombok.Getter;
import mx.unam.aragon.model.entity.EmpleadoEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class EmpleadoUserDetails implements UserDetails {
    private final EmpleadoEntity empleado;

    public EmpleadoUserDetails(EmpleadoEntity empleado) {
        this.empleado = empleado;
    }

    public Long getId() {
        return empleado.getId();
    }

    public String getNombre() {
        return empleado.getNombre();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return empleado.getRoles().stream()
                .map(rol -> (GrantedAuthority) rol::getNombre)
                .toList();
    }

    @Override
    public String getPassword() {
        return empleado.getPassword_hash();
    }

    @Override
    public String getUsername() {
        return empleado.getUsuario();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return empleado.isActivo();
    }
}
