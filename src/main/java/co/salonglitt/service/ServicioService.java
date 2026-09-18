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

    public ServicioResponseDTO findById(Integer id) {
        return aDto(obtener(id));
    }

    public ServicioResponseDTO create(ServicioRequestDTO dto) {
        Servicio s = new Servicio(dto.nombre().trim(), dto.precio(), dto.duracion().trim(),
                dto.categoria().trim(), dto.imagen(), dto.idcitas());
        return aDto(servicioRepository.save(s));
    }

    public ServicioResponseDTO update(Integer id, ServicioRequestDTO dto) {
        Servicio actual = obtener(id);
        actual.setNombre(dto.nombre().trim());
        actual.setPrecio(dto.precio());
        actual.setDuracion(dto.duracion().trim());
        actual.setCategoria(dto.categoria().trim());
        actual.setImagen(dto.imagen());
        actual.setIdcitas(dto.idcitas());
        return aDto(servicioRepository.save(actual));
    }

    public void delete(Integer id) {
        obtener(id);
        servicioRepository.deleteById(id);
    }

    private Servicio obtener(Integer id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Servicio no encontrado con id " + id));
    }

    private ServicioResponseDTO aDto(Servicio s) {
        return new ServicioResponseDTO(s.getId(), s.getNombre(), s.getPrecio(),
                s.getDuracion(), s.getCategoria(), s.getImagen(), s.getIdcitas());
    }
}