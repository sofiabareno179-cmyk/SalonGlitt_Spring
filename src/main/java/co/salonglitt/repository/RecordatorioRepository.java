package co.salonglitt.repository;

import co.salonglitt.entity.Recordatorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordatorioRepository extends JpaRepository<Recordatorio, Integer> {
    List<Recordatorio> findByUsuarioId(Integer usuarioId);
}