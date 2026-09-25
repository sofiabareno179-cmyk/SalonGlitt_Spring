package co.salonglitt.repository;

import co.salonglitt.entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
	default java.util.Optional<Servicio> findById(Integer id) {
		return findById(id.longValue());
	}
}
