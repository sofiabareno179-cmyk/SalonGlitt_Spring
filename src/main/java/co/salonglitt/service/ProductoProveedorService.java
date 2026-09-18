package co.salonglitt.service;

import co.salonglitt.dto.ProductoProveedorRequestDTO;
import co.salonglitt.dto.ProductoProveedorResponseDTO;
import co.salonglitt.entity.Producto;
import co.salonglitt.entity.ProductoProveedor;
import co.salonglitt.entity.ProductoProveedorId;
import co.salonglitt.entity.Proveedor;
import co.salonglitt.exception.ConflictException;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.ProductoProveedorRepository;
import co.salonglitt.repository.ProductoRepository;
import co.salonglitt.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoProveedorService {

    private final ProductoProveedorRepository productoProveedorRepository;
    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;

    public ProductoProveedorService(ProductoProveedorRepository productoProveedorRepository,
                                    ProductoRepository productoRepository,
                                    ProveedorRepository proveedorRepository) {
        this.productoProveedorRepository = productoProveedorRepository;
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;
    }

    public List<ProductoProveedorResponseDTO> findAll() {
        return productoProveedorRepository.findAll().stream().map(this::aDto).toList();
    }

    public ProductoProveedorResponseDTO findById(Integer productoId, Integer proveedorId) {
        return aDto(obtener(productoId, proveedorId));
    }

    public List<ProductoProveedorResponseDTO> findByProducto(Integer productoId) {
        return productoProveedorRepository.findByProductoId(productoId).stream().map(this::aDto).toList();
    }

    public List<ProductoProveedorResponseDTO> findByProveedor(Integer proveedorId) {
        return productoProveedorRepository.findByProveedorId(proveedorId).stream().map(this::aDto).toList();
    }

    public ProductoProveedorResponseDTO create(ProductoProveedorRequestDTO dto) {
        Producto producto = productoRepository.findById(dto.productoId())
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con id " + dto.productoId()));
        Proveedor proveedor = proveedorRepository.findById(dto.proveedorId())
                .orElseThrow(() -> new NotFoundException("Proveedor no encontrado con id " + dto.proveedorId()));
        ProductoProveedorId id = new ProductoProveedorId(dto.productoId(), dto.proveedorId());
        if (productoProveedorRepository.existsById(id)) {
            throw new ConflictException("La relación producto-proveedor ya existe");
        }
        ProductoProveedor pp = new ProductoProveedor(dto.productoId(), dto.proveedorId());
        pp.setProducto(producto);
        pp.setProveedor(proveedor);
        return aDto(productoProveedorRepository.save(pp));
    }

    public void delete(Integer productoId, Integer proveedorId) {
        ProductoProveedor pp = obtener(productoId, proveedorId);
        productoProveedorRepository.delete(pp);
    }

    private ProductoProveedor obtener(Integer productoId, Integer proveedorId) {
        return productoProveedorRepository.findById(new ProductoProveedorId(productoId, proveedorId))
                .orElseThrow(() -> new NotFoundException(
                        "Relación producto-proveedor no encontrada (" + productoId + ", " + proveedorId + ")"));
    }

    private ProductoProveedorResponseDTO aDto(ProductoProveedor pp) {
        String nombreProducto = pp.getProducto() != null ? pp.getProducto().getNombre() : null;
        String nombreProveedor = pp.getProveedor() != null ? pp.getProveedor().getNombreEmpresa() : null;
        return new ProductoProveedorResponseDTO(pp.getProductoId(), pp.getProveedorId(),
                nombreProducto, nombreProveedor);
    }
}
