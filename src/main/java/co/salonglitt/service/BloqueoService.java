package co.salonglitt.service;

import co.salonglitt.dto.BloqueoRequestDTO;
import co.salonglitt.dto.BloqueoResponseDTO;
import co.salonglitt.entity.Bloqueo;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.BloqueoRepository;
import co.salonglitt.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BloqueoService {

    private final BloqueoRepository bloqueoRepository;
    private final UsuarioRepository usuarioRepository;

    public BloqueoService(BloqueoRepository bloqueoRepository, UsuarioRepository usuarioRepository) {
        this.bloqueoRepository = bloqueoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<BloqueoResponseDTO> findAll() {
        return bloqueoRepository.findAll().stream().map(this::aDto).toList();
    }

    public BloqueoResponseDTO findById(Long id) {
        return aDto(obtener(id));
    }

    public List<BloqueoResponseDTO> findByEstilista(Long estilistaId) {
        validarEstilista(estilistaId);
        return bloqueoRepository.findByEstilistaId(estilistaId).stream().map(this::aDto).toList();
    }

    public BloqueoResponseDTO create(BloqueoRequestDTO dto) {
        if (!dto.fin().isAfter(dto.inicio())) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
        var estilista = validarEstilista(dto.estilistaId());
        Bloqueo b = new Bloqueo(estilista, dto.inicio(), dto.fin(), dto.motivo());
        return aDto(bloqueoRepository.save(b));
    }

    public BloqueoResponseDTO update(Long id, BloqueoRequestDTO dto) {
        Bloqueo actual = obtener(id);
        if (!dto.fin().isAfter(dto.inicio())) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
        }
        var estilista = validarEstilista(dto.estilistaId());
        actual.setEstilista(estilista);
        actual.setInicio(dto.inicio());
        actual.setFin(dto.fin());
        actual.setMotivo(dto.motivo());
        return aDto(bloqueoRepository.save(actual));
    }

    public void delete(Long id) {
        obtener(id);
        bloqueoRepository.deleteById(id);
    }

    private Bloqueo obtener(Long id) {
        return bloqueoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bloqueo no encontrado con id " + id));
    }

    private Usuario validarEstilista(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Estilista no encontrado con id " + id));
    }

    private BloqueoResponseDTO aDto(Bloqueo b) {
        return new BloqueoResponseDTO(b.getId(), b.getEstilista().getId(), b.getEstilista().getNombre(),
                b.getInicio(), b.getFin(), b.getMotivo());
    }
}
