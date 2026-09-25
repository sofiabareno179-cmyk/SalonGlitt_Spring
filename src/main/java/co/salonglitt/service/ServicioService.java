package co.salonglitt.service;

import co.salonglitt.dto.ServicioRequestDTO;
import co.salonglitt.dto.ServicioResponseDTO;
import co.salonglitt.entity.Servicio;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService {
    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository) { this.servicioRepository = servicioRepository; }
    public List<ServicioResponseDTO> findAll() { return servicioRepository.findAll().stream().map(this::aDto).toList(); }
    public ServicioResponseDTO findById(Integer id) { return aDto(obtener(id)); }

    public ServicioResponseDTO create(ServicioRequestDTO dto) {
        Servicio servicio = new Servicio(dto.nombre().trim(), dto.categoria(), dto.precio(), parseDuracion(dto.duracion()), true);
        return aDto(servicioRepository.save(servicio));
    }

    public ServicioResponseDTO update(Integer id, ServicioRequestDTO dto) {
        Servicio actual = obtener(id);
        actual.setNombre(dto.nombre().trim());
        actual.setDescripcion(dto.categoria());
        actual.setPrecio(dto.precio());
        actual.setDuracionMinutos(parseDuracion(dto.duracion()));
        return aDto(servicioRepository.save(actual));
    }

    public void delete(Integer id) { servicioRepository.delete(obtener(id)); }

    private Servicio obtener(Integer id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Servicio no encontrado con id " + id));
    }

    private Integer parseDuracion(String duracion) {
        try { return Integer.valueOf(duracion.trim()); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("La duración debe ser numérica"); }
    }

    private ServicioResponseDTO aDto(Servicio servicio) {
        return new ServicioResponseDTO(Math.toIntExact(servicio.getId()), servicio.getNombre(), servicio.getPrecio(),
                String.valueOf(servicio.getDuracionMinutos()), servicio.getDescripcion(), null);
    }
}
