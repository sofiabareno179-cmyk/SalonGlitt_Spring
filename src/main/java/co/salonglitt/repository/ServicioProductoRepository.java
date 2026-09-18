package co.salonglitt.repository;

import co.salonglitt.entity.ServicioProducto;
import co.salonglitt.entity.ServicioProductoId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicioProductoRepository extends JpaRepository<ServicioProducto, ServicioProductoId> {

    List<ServicioProducto> findByServicioId(Integer servicioId);

    List<ServicioProducto> findByProductoId(Integer productoId);
}
