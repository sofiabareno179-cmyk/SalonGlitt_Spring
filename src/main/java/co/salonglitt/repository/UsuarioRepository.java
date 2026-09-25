package co.salonglitt.repository;

import co.salonglitt.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailIgnoreCase(String email);

    default Optional<Usuario> findById(Integer id) {
        return findById(id.longValue());
    }
}
