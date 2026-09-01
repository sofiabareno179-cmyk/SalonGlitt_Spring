package co.salonglitt.service;

import co.salonglitt.dto.ProductoRequestDTO;
import co.salonglitt.dto.ProductoResponseDTO;
import co.salonglitt.entity.Producto;
import co.salonglitt.entity.Proveedor;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.ProductoRepository;
import co.salonglitt.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProveedorRepository proveedorRepository;

    public ProductoService(ProductoRepository productoRepository, ProveedorRepository proveedorRepository) {
        this.productoRepository = productoRepository;
        this.proveedorRepository = proveedorRepository;
    }

    public List<ProductoResponseDTO> findAll() {
        return productoRepository.findAll().stream().map(this::aDto).toList();
    }

    public ProductoResponseDTO findById(Long id) {
        return aDto(obtener(id));
    }

    public List<ProductoResponseDTO> findByProveedor(Long proveedorId) {
        validarProveedor(proveedorId);
        return productoRepository.findByProveedorId(proveedorId).stream().map(this::aDto).toList();
    }

    public ProductoResponseDTO create(ProductoRequestDTO dto) {
        var proveedor = validarProveedor(dto.proveedorId());
        Producto p = new Producto(dto.nombre().trim(), dto.descripcion(), dto.precio(), proveedor,
                dto.activo() == null || dto.activo());
        return aDto(productoRepository.save(p));
    }

    public ProductoResponseDTO update(Long id, ProductoRequestDTO dto) {
        Producto actual = obtener(id);
        var proveedor = validarProveedor(dto.proveedorId());
        actual.setNombre(dto.nombre().trim());
        actual.setDescripcion(dto.descripcion());
        actual.setPrecio(dto.precio());
        actual.setProveedor(proveedor);
        actual.setActivo(dto.activo() == null || dto.activo());
        return aDto(productoRepository.save(actual));
    }

    public void delete(Long id) {
        obtener(id);
        productoRepository.deleteById(id);
    }

    private Producto obtener(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con id " + id));
    }

    private Proveedor validarProveedor(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Proveedor no encontrado con id " + id));
    }

    private ProductoResponseDTO aDto(Producto p) {
        return new ProductoResponseDTO(p.getId(), p.getNombre(), p.getDescripcion(), p.getPrecio(),
                p.getProveedor().getId(), p.getProveedor().getNombre(), p.isActivo());
    }
}
