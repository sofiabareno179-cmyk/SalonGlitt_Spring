package co.salonglitt.service;

import co.salonglitt.dto.ProductoRequestDTO;
import co.salonglitt.dto.ProductoResponseDTO;
import co.salonglitt.entity.Producto;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<ProductoResponseDTO> findAll() {
        return productoRepository.findAll().stream().map(this::aDto).toList();
    }

    public ProductoResponseDTO findById(Integer id) {
        return aDto(obtener(id));
    }

    public ProductoResponseDTO create(ProductoRequestDTO dto) {
        Producto p = new Producto(dto.nombre().trim(), dto.descripcion(), dto.precio(), dto.categoria().trim());
        return aDto(productoRepository.save(p));
    }

    public ProductoResponseDTO update(Integer id, ProductoRequestDTO dto) {
        Producto actual = obtener(id);
        actual.setNombre(dto.nombre().trim());
        actual.setDescripcion(dto.descripcion());
        actual.setPrecio(dto.precio());
        actual.setCategoria(dto.categoria().trim());
        return aDto(productoRepository.save(actual));
    }

    public void delete(Integer id) {
        obtener(id);
        productoRepository.deleteById(id);
    }

    private Producto obtener(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con id " + id));
    }

    private ProductoResponseDTO aDto(Producto p) {
        return new ProductoResponseDTO(p.getId(), p.getNombre(), p.getDescripcion(), p.getPrecio(), p.getCategoria());
    }
}