package co.salonglitt.repository;

import co.salonglitt.entity.Galeria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GaleriaRepository extends JpaRepository<Galeria, Integer> {
    List<Galeria> findByTipoIgnoreCase(String tipo);
}
