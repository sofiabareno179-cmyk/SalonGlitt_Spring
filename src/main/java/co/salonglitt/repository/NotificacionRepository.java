package co.salonglitt.repository;

import co.salonglitt.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioIdOrderByFechaCreacionDesc(Long usuarioId);

    List<Notificacion> findByUsuarioIdAndLeidaFalse(Long usuarioId);
}
