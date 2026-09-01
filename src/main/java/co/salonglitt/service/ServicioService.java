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

    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public List<ServicioResponseDTO> findAll() {
        return servicioRepository.findAll().stream().map(this::aDto).toList();
    }

    public ServicioResponseDTO findById(Long id) {
        return aDto(obtener(id));
    }

    public ServicioResponseDTO create(ServicioRequestDTO dto) {
        Servicio s = new Servicio(dto.nombre().trim(), dto.descripcion(), dto.precio(),
                dto.duracionMinutos(), dto.activo() == null || dto.activo());
        return aDto(servicioRepository.save(s));
    }

    public ServicioResponseDTO update(Long id, ServicioRequestDTO dto) {
        Servicio actual = obtener(id);
        actual.setNombre(dto.nombre().trim());
        actual.setDescripcion(dto.descripcion());
        actual.setPrecio(dto.precio());
        actual.setDuracionMinutos(dto.duracionMinutos());
        actual.setActivo(dto.activo() == null || dto.activo());
        return aDto(servicioRepository.save(actual));
    }

    public void delete(Long id) {
        obtener(id);
        servicioRepository.deleteById(id);
    }

    private Servicio obtener(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Servicio no encontrado con id " + id));
    }

    private ServicioResponseDTO aDto(Servicio s) {
        return new ServicioResponseDTO(s.getId(), s.getNombre(), s.getDescripcion(), s.getPrecio(),
                s.getDuracionMinutos(), s.isActivo());
    }
}
