package co.salonglitt.config;

import co.salonglitt.entity.Usuario;
import co.salonglitt.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return username -> usuarioRepository.findByEmailIgnoreCase(username)
                .map(this::toUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    @Bean
    @Profile("!test")
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/usuarios/**").hasRole("ADMIN")
                    .requestMatchers("/api/perfiles/**").hasRole("ADMIN")
                    .requestMatchers("/api/inventarios/**", "/api/proveedores/**").hasAnyRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/servicios/**", "/api/productos/**", "/api/promociones/**").hasRole("ADMIN")
                    .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated())
                .httpBasic(basic -> {})
                .formLogin(form -> form.disable());
        return http.build();
    }

    @Bean
    @Profile("test")
    SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    private org.springframework.security.core.userdetails.UserDetails toUserDetails(Usuario usuario) {
        String rol = usuario.getPerfil() == null ? usuario.getRol() : usuario.getPerfil().getNombre();
        return User.withUsername(usuario.getEmail())
                .password(usuario.getPasswordHash())
                .roles(rol == null ? "CLIENTE" : rol.toUpperCase())
                .disabled(!usuario.isActivo())
                .build();
    }
}