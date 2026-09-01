package co.salonglitt.repository;

import co.salonglitt.entity.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    Optional<Perfil> findByNombreIgnoreCase(String nombre);
}
