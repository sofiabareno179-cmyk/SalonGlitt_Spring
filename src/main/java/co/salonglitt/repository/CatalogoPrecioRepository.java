package co.salonglitt.repository;

import co.salonglitt.entity.CatalogoPrecio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CatalogoPrecioRepository extends JpaRepository<CatalogoPrecio, Long> {
    List<CatalogoPrecio> findByServicioId(Long servicioId);
}
