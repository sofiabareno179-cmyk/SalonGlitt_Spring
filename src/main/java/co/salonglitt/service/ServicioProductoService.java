package co.salonglitt.service;

import co.salonglitt.dto.ServicioProductoRequestDTO;
import co.salonglitt.dto.ServicioProductoResponseDTO;
import co.salonglitt.entity.Producto;
import co.salonglitt.entity.Servicio;
import co.salonglitt.entity.ServicioProducto;
import co.salonglitt.entity.ServicioProductoId;
import co.salonglitt.exception.ConflictException;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.ProductoRepository;
import co.salonglitt.repository.ServicioProductoRepository;
import co.salonglitt.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioProductoService {

    private final ServicioProductoRepository servicioProductoRepository;
    private final ServicioRepository servicioRepository;
    private final ProductoRepository productoRepository;

    public ServicioProductoService(ServicioProductoRepository servicioProductoRepository,
                                   ServicioRepository servicioRepository,
                                   ProductoRepository productoRepository) {
        this.servicioProductoRepository = servicioProductoRepository;
        this.servicioRepository = servicioRepository;
        this.productoRepository = productoRepository;
    }

    public List<ServicioProductoResponseDTO> findAll() {
        return servicioProductoRepository.findAll().stream().map(this::aDto).toList();
    }

    public ServicioProductoResponseDTO findById(Integer servicioId, Integer productoId) {
        return aDto(obtener(servicioId, productoId));
    }

    public List<ServicioProductoResponseDTO> findByServicio(Integer servicioId) {
        return servicioProductoRepository.findByServicioId(servicioId).stream().map(this::aDto).toList();
    }

    public List<ServicioProductoResponseDTO> findByProducto(Integer productoId) {
        return servicioProductoRepository.findByProductoId(productoId).stream().map(this::aDto).toList();
    }

    public ServicioProductoResponseDTO create(ServicioProductoRequestDTO dto) {
        Servicio servicio = servicioRepository.findById(dto.servicioId())
                .orElseThrow(() -> new NotFoundException("Servicio no encontrado con id " + dto.servicioId()));
        Producto producto = productoRepository.findById(dto.productoId())
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con id " + dto.productoId()));
        ServicioProductoId id = new ServicioProductoId(dto.servicioId(), dto.productoId());
        if (servicioProductoRepository.existsById(id)) {
            throw new ConflictException("La relación servicio-producto ya existe");
        }
        ServicioProducto sp = new ServicioProducto(dto.servicioId(), dto.productoId());
        sp.setServicio(servicio);
        sp.setProducto(producto);
        return aDto(servicioProductoRepository.save(sp));
    }

    public void delete(Integer servicioId, Integer productoId) {
        ServicioProducto sp = obtener(servicioId, productoId);
        servicioProductoRepository.delete(sp);
    }

    private ServicioProducto obtener(Integer servicioId, Integer productoId) {
        return servicioProductoRepository.findById(new ServicioProductoId(servicioId, productoId))
                .orElseThrow(() -> new NotFoundException(
                        "Relación servicio-producto no encontrada (" + servicioId + ", " + productoId + ")"));
    }

    private ServicioProductoResponseDTO aDto(ServicioProducto sp) {
        return new ServicioProductoResponseDTO(sp.getServicioId(), sp.getProductoId());
    }
}
