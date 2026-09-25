package co.salonglitt.repository;

import co.salonglitt.entity.ProductoProveedor;
import co.salonglitt.entity.ProductoProveedorId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoProveedorRepository extends JpaRepository<ProductoProveedor, ProductoProveedorId> {

    List<ProductoProveedor> findByProveedorId(Integer proveedorId);

    List<ProductoProveedor> findByProductoId(Integer productoId);
}
