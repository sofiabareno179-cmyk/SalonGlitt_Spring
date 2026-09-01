package co.salonglitt.service;

import co.salonglitt.dto.GaleriaRequestDTO;
import co.salonglitt.dto.GaleriaResponseDTO;
import co.salonglitt.entity.Galeria;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.GaleriaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GaleriaService {

    private final GaleriaRepository galeriaRepository;

    public GaleriaService(GaleriaRepository galeriaRepository) {
        this.galeriaRepository = galeriaRepository;
    }

    public List<GaleriaResponseDTO> findAll() {
        return galeriaRepository.findAll().stream().map(this::aDto).toList();
    }

    public GaleriaResponseDTO findById(Integer id) {
        return aDto(obtener(id));
    }

    public List<GaleriaResponseDTO> findByTipo(String tipo) {
        return galeriaRepository.findByTipoIgnoreCase(tipo).stream().map(this::aDto).toList();
    }

    public GaleriaResponseDTO create(GaleriaRequestDTO dto) {
        LocalDateTime fecha = (dto.fechaSubida() == null) ? LocalDateTime.now() : dto.fechaSubida();
        String tipo = (dto.tipo() == null || dto.tipo().isBlank()) ? "imagen" : dto.tipo().trim();
        Galeria g = new Galeria(dto.titulo().trim(), dto.archivo().trim(), dto.descripcion(), fecha, tipo);
        return aDto(galeriaRepository.save(g));
    }

    public GaleriaResponseDTO update(Integer id, GaleriaRequestDTO dto) {
        Galeria actual = obtener(id);
        actual.setTitulo(dto.titulo().trim());
        actual.setArchivo(dto.archivo().trim());
        actual.setDescripcion(dto.descripcion());
        if (dto.fechaSubida() != null) {
            actual.setFechaSubida(dto.fechaSubida());
        }
        if (dto.tipo() != null && !dto.tipo().isBlank()) {
            actual.setTipo(dto.tipo().trim());
        }
        return aDto(galeriaRepository.save(actual));
    }

    public void delete(Integer id) {
        Galeria g = obtener(id);
        galeriaRepository.delete(g);
    }

    private Galeria obtener(Integer id) {
        return galeriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Elemento de galería no encontrado con id " + id));
    }

    private GaleriaResponseDTO aDto(Galeria g) {
        return new GaleriaResponseDTO(g.getId(), g.getTitulo(), g.getArchivo(), g.getDescripcion(),
                g.getFechaSubida(), g.getTipo());
    }
}
