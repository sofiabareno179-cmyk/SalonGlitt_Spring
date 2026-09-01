package co.salonglitt.repository;

import co.salonglitt.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByClienteId(Long clienteId);

    List<Cita> findByEstadoIgnoreCase(String estado);
}
