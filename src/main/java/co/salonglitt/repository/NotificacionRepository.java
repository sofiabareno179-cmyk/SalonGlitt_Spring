package co.salonglitt.repository;

import co.salonglitt.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {
    List<Notificacion> findByUsuarioIdOrderByFechaCreacionDesc(Integer usuarioId);

    List<Notificacion> findByUsuarioIdAndLeidaFalse(Integer usuarioId);
}