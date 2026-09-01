package co.salonglitt.service;

import co.salonglitt.dto.PromocionRequestDTO;
import co.salonglitt.dto.PromocionResponseDTO;
import co.salonglitt.entity.Producto;
import co.salonglitt.entity.Promocion;
import co.salonglitt.entity.Servicio;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.ProductoRepository;
import co.salonglitt.repository.PromocionRepository;
import co.salonglitt.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromocionService {

    private final PromocionRepository promocionRepository;
    private final ServicioRepository servicioRepository;
    private final ProductoRepository productoRepository;

    public PromocionService(PromocionRepository promocionRepository, ServicioRepository servicioRepository,
                            ProductoRepository productoRepository) {
        this.promocionRepository = promocionRepository;
        this.servicioRepository = servicioRepository;
        this.productoRepository = productoRepository;
    }

    public List<PromocionResponseDTO> findAll() {
        return promocionRepository.findAll().stream().map(this::aDto).toList();
    }

    public PromocionResponseDTO findById(Long id) {
        return aDto(obtener(id));
    }

    public List<PromocionResponseDTO> findActivas() {
        return promocionRepository.findByActivaTrue().stream().map(this::aDto).toList();
    }

    public PromocionResponseDTO create(PromocionRequestDTO dto) {
        validarFechas(dto);
        var servicio = dto.servicioId() == null ? null : obtenerServicio(dto.servicioId());
        var producto = dto.productoId() == null ? null : obtenerProducto(dto.productoId());
        if (servicio == null && producto == null) {
            throw new IllegalArgumentException("La promoción debe aplicar a un servicio o a un producto");
        }
        Promocion p = new Promocion(dto.nombre().trim(), dto.descripcion(), servicio, producto,
                dto.descuento(), dto.fechaInicio(), dto.fechaFin(), dto.activa() == null || dto.activa());
        return aDto(promocionRepository.save(p));
    }

    public PromocionResponseDTO update(Long id, PromocionRequestDTO dto) {
        Promocion actual = obtener(id);
        validarFechas(dto);
        var servicio = dto.servicioId() == null ? null : obtenerServicio(dto.servicioId());
        var producto = dto.productoId() == null ? null : obtenerProducto(dto.productoId());
        if (servicio == null && producto == null) {
            throw new IllegalArgumentException("La promoción debe aplicar a un servicio o a un producto");
        }
        actual.setNombre(dto.nombre().trim());
        actual.setDescripcion(dto.descripcion());
        actual.setServicio(servicio);
        actual.setProducto(producto);
        actual.setDescuento(dto.descuento());
        actual.setFechaInicio(dto.fechaInicio());
        actual.setFechaFin(dto.fechaFin());
        actual.setActiva(dto.activa() == null || dto.activa());
        return aDto(promocionRepository.save(actual));
    }

    public void delete(Long id) {
        obtener(id);
        promocionRepository.deleteById(id);
    }

    private Promocion obtener(Long id) {
        return promocionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Promoción no encontrada con id " + id));
    }

    private Servicio obtenerServicio(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Servicio no encontrado con id " + id));
    }

    private Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado con id " + id));
    }

    private void validarFechas(PromocionRequestDTO dto) {
        if (dto.fechaFin().isBefore(dto.fechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
    }

    private PromocionResponseDTO aDto(Promocion p) {
        return new PromocionResponseDTO(p.getId(), p.getNombre(), p.getDescripcion(),
                p.getServicio() == null ? null : p.getServicio().getId(),
                p.getServicio() == null ? null : p.getServicio().getNombre(),
                p.getProducto() == null ? null : p.getProducto().getId(),
                p.getProducto() == null ? null : p.getProducto().getNombre(),
                p.getDescuento(), p.getFechaInicio(), p.getFechaFin(), p.isActiva());
    }
}
