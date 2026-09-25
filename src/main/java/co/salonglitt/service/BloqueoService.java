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

    public BloqueoResponseDTO findById(Integer id) {
        return aDto(obtener(id));
    }

    public List<BloqueoResponseDTO> findByUsuario(Integer usuarioId) {
        validarUsuario(usuarioId);
        return bloqueoRepository.findByUsuarioId(usuarioId).stream().map(this::aDto).toList();
    }

    public BloqueoResponseDTO create(BloqueoRequestDTO dto) {
        validarHoras(dto.horaInicio(), dto.horaFin());
        Usuario usuario = validarUsuario(dto.idusuario());
        Bloqueo b = new Bloqueo(dto.fecha(), dto.horaInicio().trim(), dto.horaFin().trim(), dto.motivo(), usuario);
        return aDto(bloqueoRepository.save(b));
    }

    public BloqueoResponseDTO update(Integer id, BloqueoRequestDTO dto) {
        Bloqueo actual = obtener(id);
        validarHoras(dto.horaInicio(), dto.horaFin());
        Usuario usuario = validarUsuario(dto.idusuario());
        actual.setFecha(dto.fecha());
        actual.setHoraInicio(dto.horaInicio().trim());
        actual.setHoraFin(dto.horaFin().trim());
        actual.setMotivo(dto.motivo());
        actual.setUsuario(usuario);
        return aDto(bloqueoRepository.save(actual));
    }

    public void delete(Integer id) {
        obtener(id);
        bloqueoRepository.deleteById(id);
    }

    private Bloqueo obtener(Integer id) {
        return bloqueoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bloqueo no encontrado con id " + id));
    }

    private Usuario validarUsuario(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id " + id));
    }

    private void validarHoras(String inicio, String fin) {
        if (inicio != null && fin != null && inicio.trim().compareTo(fin.trim()) >= 0) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }
    }

    private BloqueoResponseDTO aDto(Bloqueo b) {
        return new BloqueoResponseDTO(b.getId(), b.getUsuario().getId(), b.getUsuario().getNombreuser(),
                b.getFecha(), b.getHoraInicio(), b.getHoraFin(), b.getMotivo(), b.getCreatedAt());
    }
}