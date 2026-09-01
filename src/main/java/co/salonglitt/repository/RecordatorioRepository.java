package co.salonglitt.repository;

import co.salonglitt.entity.Recordatorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordatorioRepository extends JpaRepository<Recordatorio, Long> {
    List<Recordatorio> findByCitaId(Long citaId);

    List<Recordatorio> findByEnviadoFalse();
}
