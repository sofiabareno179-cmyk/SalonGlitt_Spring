package co.salonglitt.service;

import co.salonglitt.dto.InventarioRequestDTO;
import co.salonglitt.dto.InventarioResponseDTO;
import co.salonglitt.entity.Inventario;
import co.salonglitt.entity.Producto;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.InventarioRepository;
import co.salonglitt.repository.ProductoRepository;
import org.springframework.stereotype.Service;

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

    public InventarioResponseDTO findById(Long id) {
        return aDto(obtener(id));
    }

    public InventarioResponseDTO create(InventarioRequestDTO dto) {
        validarUnicoPorProducto(dto.productoId());
        var producto = obtenerProducto(dto.productoId());
        Inventario i = new Inventario(producto, dto.cantidadTotal(), dto.stockMinimo(), null);
        return aDto(inventarioRepository.save(i));
    }

    public InventarioResponseDTO update(Long id, InventarioRequestDTO dto) {
        Inventario actual = obtener(id);
        var producto = obtenerProducto(dto.productoId());
        if (!actual.getProducto().getId().equals(dto.productoId())) {
            validarUnicoPorProducto(dto.productoId());
        }
        actual.setProducto(producto);
        actual.setCantidadTotal(dto.cantidadTotal());
        actual.setStockMinimo(dto.stockMinimo());
        return aDto(inventarioRepository.save(actual));
    }

    public void delete(Long id) {
        obtener(id);
        inventarioRepository.deleteById(id);
    }

    private Inventario obtener(Long id) {
        return inventarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Inventario no encontrado con id " + id));
    }

    private Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con id " + id));
    }

    private void validarUnicoPorProducto(Long productoId) {
        inventarioRepository.findByProductoId(productoId).ifPresent(i -> {
            throw new IllegalArgumentException("Ya existe inventario registrado para el producto con id " + productoId);
        });
    }

    private InventarioResponseDTO aDto(Inventario i) {
        return new InventarioResponseDTO(i.getId(), i.getProducto().getId(), i.getProducto().getNombre(),
                i.getCantidadTotal(), i.getStockMinimo(), i.getUltimaActualizacion());
    }
}
