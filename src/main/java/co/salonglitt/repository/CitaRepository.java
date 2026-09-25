package co.salonglitt.repository;

import co.salonglitt.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByClienteId(Long clienteId);

    default java.util.Optional<Cita> findById(Integer id) {
        return findById(id.longValue());
    }

    default List<Cita> findByUsuarioId(Integer usuarioId) {
        return findByClienteId(usuarioId.longValue());
    }

    List<Cita> findByEstadoIgnoreCase(String estado);
}
