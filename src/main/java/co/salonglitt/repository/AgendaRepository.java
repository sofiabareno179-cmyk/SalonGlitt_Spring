package co.salonglitt.repository;

import co.salonglitt.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Long> {
    List<Agenda> findByEstilistaId(Long estilistaId);

    List<Agenda> findByEstilistaIdAndFecha(Long estilistaId, LocalDate fecha);
}
