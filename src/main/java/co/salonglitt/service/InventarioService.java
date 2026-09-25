package co.salonglitt.service;

import co.salonglitt.dto.InventarioRequestDTO;
import co.salonglitt.dto.InventarioResponseDTO;
import co.salonglitt.entity.Inventario;
import co.salonglitt.entity.Producto;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.InventarioRepository;
import co.salonglitt.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;

    public InventarioService(InventarioRepository inventarioRepository, ProductoRepository productoRepository) {
        this.inventarioRepository = inventarioRepository;
        this.productoRepository = productoRepository;
    }

    public List<InventarioResponseDTO> findAll() {
        return inventarioRepository.findAll().stream().map(this::aDto).toList();
    }

    public InventarioResponseDTO findById(Integer id) {
        return aDto(obtener(id));
    }

    public InventarioResponseDTO create(InventarioRequestDTO dto) {
        validarUnicoPorProducto(dto.productoId());
        Producto producto = obtenerProducto(dto.productoId());
        Inventario i = new Inventario(dto.stock(), LocalDate.now().toString(), producto, dto.tipo());
        return aDto(inventarioRepository.save(i));
    }

    public InventarioResponseDTO update(Integer id, InventarioRequestDTO dto) {
        Inventario actual = obtener(id);
        Producto producto = obtenerProducto(dto.productoId());
        if (!actual.getProducto().getId().equals(dto.productoId())) {
            validarUnicoPorProducto(dto.productoId());
        }
        actual.setProducto(producto);
        actual.setStock(dto.stock());
        actual.setTipo(dto.tipo());
        return aDto(inventarioRepository.save(actual));
    }

    public void delete(Integer id) {
        obtener(id);
        inventarioRepository.deleteById(id);
    }

    private Inventario obtener(Integer id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Inventario no encontrado con id " + id));
    }

    private Producto obtenerProducto(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con id " + id));
    }

    private void validarUnicoPorProducto(Integer productoId) {
        inventarioRepository.findByProductoId(productoId).ifPresent(i -> {
            throw new IllegalArgumentException("Ya existe inventario registrado para el producto con id " + productoId);
        });
    }

    private InventarioResponseDTO aDto(Inventario i) {
        return new InventarioResponseDTO(i.getId(), i.getProducto().getId(), i.getProducto().getNombre(),
                i.getStock(), i.getFecha(), i.getTipo());
    }
}