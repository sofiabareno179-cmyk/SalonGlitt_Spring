package co.salonglitt.repository;

import co.salonglitt.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Integer> {
    List<Cita> findByUsuarioId(Integer usuarioId);

    List<Cita> findByEstadoIgnoreCase(String estado);
}