package mx.unam.aragon.config;

import mx.unam.aragon.service.empleado.EmpleadoUserDetailsService;
import mx.unam.aragon.util.CustomAuthFailureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private EmpleadoUserDetailsService empleadoDetailsService;

    @Autowired
    private CustomAuthFailureHandler customAuthFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/registro-acceso").permitAll()
                        .requestMatchers(HttpMethod.POST, "/venta/pdf").permitAll()
                        .requestMatchers(HttpMethod.GET, "/venta/pdf").permitAll()
                        .requestMatchers("/inicio", "/abarrotes/**", "/cliente/**", "/inventario", "/empleados/**").authenticated()
                        .anyRequest().authenticated()

                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureHandler(customAuthFailureHandler)
                        .defaultSuccessUrl("/registrar-entrada", true)
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll)
                .csrf(csrf -> csrf.ignoringRequestMatchers("/venta/pdf"))
                .userDetailsService(empleadoDetailsService)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(empleadoDetailsService).passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }
}
