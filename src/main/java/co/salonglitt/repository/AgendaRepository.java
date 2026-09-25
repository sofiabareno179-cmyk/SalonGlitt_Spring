package co.salonglitt.repository;

import co.salonglitt.entity.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendaRepository extends JpaRepository<Agenda, Integer> {
    List<Agenda> findByUsuarioId(Integer usuarioId);

    default List<Agenda> findByEstilistaId(Long estilistaId) {
        return findByUsuarioId(Math.toIntExact(estilistaId));
    }
}
