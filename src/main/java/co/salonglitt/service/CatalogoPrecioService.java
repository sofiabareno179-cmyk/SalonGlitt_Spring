package co.salonglitt.service;

import co.salonglitt.dto.CatalogoPrecioRequestDTO;
import co.salonglitt.dto.CatalogoPrecioResponseDTO;
import co.salonglitt.entity.CatalogoPrecio;
import co.salonglitt.entity.Servicio;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.CatalogoPrecioRepository;
import co.salonglitt.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogoPrecioService {

    private final CatalogoPrecioRepository catalogoPrecioRepository;
    private final ServicioRepository servicioRepository;

    public CatalogoPrecioService(CatalogoPrecioRepository catalogoPrecioRepository,
                                 ServicioRepository servicioRepository) {
        this.catalogoPrecioRepository = catalogoPrecioRepository;
        this.servicioRepository = servicioRepository;
    }

    public List<CatalogoPrecioResponseDTO> findAll() {
        return catalogoPrecioRepository.findAll().stream().map(this::aDto).toList();
    }

    public CatalogoPrecioResponseDTO findById(Long id) {
        return aDto(obtener(id));
    }

    public List<CatalogoPrecioResponseDTO> findByServicio(Long servicioId) {
        validarServicio(servicioId);
        return catalogoPrecioRepository.findByServicioId(servicioId).stream().map(this::aDto).toList();
    }

    public CatalogoPrecioResponseDTO create(CatalogoPrecioRequestDTO dto) {
        validarFechas(dto);
        var servicio = validarServicio(dto.servicioId());
        CatalogoPrecio c = new CatalogoPrecio(servicio, dto.precio(), dto.fechaInicio(), dto.fechaFin());
        return aDto(catalogoPrecioRepository.save(c));
    }

    public CatalogoPrecioResponseDTO update(Long id, CatalogoPrecioRequestDTO dto) {
        CatalogoPrecio actual = obtener(id);
        validarFechas(dto);
        var servicio = validarServicio(dto.servicioId());
        actual.setServicio(servicio);
        actual.setPrecio(dto.precio());
        actual.setFechaInicio(dto.fechaInicio());
        actual.setFechaFin(dto.fechaFin());
        return aDto(catalogoPrecioRepository.save(actual));
    }

    public void delete(Long id) {
        obtener(id);
        catalogoPrecioRepository.deleteById(id);
    }

    private CatalogoPrecio obtener(Long id) {
        return catalogoPrecioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Precio de catálogo no encontrado con id " + id));
    }

    private Servicio validarServicio(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Servicio no encontrado con id " + id));
    }

    private void validarFechas(CatalogoPrecioRequestDTO dto) {
        if (dto.fechaFin() != null && dto.fechaFin().isBefore(dto.fechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
    }

    private CatalogoPrecioResponseDTO aDto(CatalogoPrecio c) {
        return new CatalogoPrecioResponseDTO(c.getId(), c.getServicio().getId(), c.getServicio().getNombre(),
                c.getPrecio(), c.getFechaInicio(), c.getFechaFin());
    }
}
