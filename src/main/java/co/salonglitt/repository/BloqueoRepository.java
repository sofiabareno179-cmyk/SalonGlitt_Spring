package co.salonglitt.repository;

import co.salonglitt.entity.Bloqueo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BloqueoRepository extends JpaRepository<Bloqueo, Integer> {
    List<Bloqueo> findByUsuarioId(Integer usuarioId);
}