package co.salonglitt.service;

import co.salonglitt.dto.CitaRequestDTO;
import co.salonglitt.dto.CitaResponseDTO;
import co.salonglitt.entity.Cita;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.CitaRepository;
import co.salonglitt.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CitaService {

    private static final String ESTADO_POR_DEFECTO = "PENDIENTE";

    private static final Set<String> ESTADOS_VALIDOS =
            Set.of("PENDIENTE", "CONFIRMADA", "COMPLETADA", "CANCELADA");

    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;

    public CitaService(CitaRepository citaRepository, UsuarioRepository usuarioRepository) {
        this.citaRepository = citaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<CitaResponseDTO> findAll() {
        return citaRepository.findAll().stream().map(this::aDto).toList();
    }

    public CitaResponseDTO findById(Integer id) {
        return aDto(obtener(id));
    }

    public List<CitaResponseDTO> findByUsuario(Integer usuarioId) {
        List<Cita> result = citaRepository.findByUsuarioId(usuarioId);
        if (result.isEmpty() && !usuarioRepository.existsById(usuarioId)) {
            throw new NotFoundException("Usuario no encontrado con id " + usuarioId);
        }
        return result.stream().map(this::aDto).toList();
    }

    public List<CitaResponseDTO> findByEstado(String estado) {
        return citaRepository.findByEstadoIgnoreCase(estado).stream().map(this::aDto).toList();
    }

    public CitaResponseDTO create(CitaRequestDTO dto) {
        Usuario usuario = validarUsuario(dto.idusuario());
        Cita c = new Cita(usuario, dto.fechahora(), resolverEstado(dto.estado()), dto.servicio().trim());
        return aDto(citaRepository.save(c));
    }

    public CitaResponseDTO update(Integer id, CitaRequestDTO dto) {
        Cita actual = obtener(id);
        Usuario usuario = validarUsuario(dto.idusuario());
        actual.setUsuario(usuario);
        actual.setFechahora(dto.fechahora());
        actual.setServicio(dto.servicio().trim());
        actual.setEstado(resolverEstado(dto.estado()));
        return aDto(citaRepository.save(actual));
    }

    public CitaResponseDTO cambiarEstado(Integer id, String estado) {
        Cita actual = obtener(id);
        actual.setEstado(resolverEstado(estado));
        return aDto(citaRepository.save(actual));
    }

    public void delete(Integer id) {
        obtener(id);
        citaRepository.deleteById(id);
    }

    private Cita obtener(Integer id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada con id " + id));
    }

    private Usuario validarUsuario(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con id " + id));
    }

    private String resolverEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return ESTADO_POR_DEFECTO;
        }
        String normalizado = estado.trim().toUpperCase();
        if (!ESTADOS_VALIDOS.contains(normalizado)) {
            throw new IllegalArgumentException("Estado no válido: " + estado
                    + ". Permitidos: " + String.join(", ", ESTADOS_VALIDOS));
        }
        return normalizado;
    }

    private CitaResponseDTO aDto(Cita c) {
        return new CitaResponseDTO(c.getId(), c.getUsuario().getId(), c.getUsuario().getNombreuser(),
                c.getFechahora(), c.getEstado(), c.getServicio());
    }
}