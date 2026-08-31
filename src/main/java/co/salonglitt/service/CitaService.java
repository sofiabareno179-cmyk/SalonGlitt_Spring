package co.salonglitt.service;

import co.salonglitt.dto.CitaRequestDTO;
import co.salonglitt.dto.CitaResponseDTO;
import co.salonglitt.entity.Cita;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.CitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitaService {

    private static final String ESTADO_POR_DEFECTO = "Espera";

<<<<<<< HEAD
    private final CitaRepository citaRepository;
=======
    private static final String ESTADO_POR_DEFECTO = "PENDIENTE";

    private static final java.util.Set<String> ESTADOS_VALIDOS =
            java.util.Set.of("PENDIENTE", "CONFIRMADA", "COMPLETADA", "CANCELADA");

    private final Map<Long, Cita> datos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong();
>>>>>>> c941d9769e25da0beb4b0ae1a46f61ac155acdec
    private final UsuarioService usuarioService;

    public CitaService(CitaRepository citaRepository, UsuarioService usuarioService) {
        this.citaRepository = citaRepository;
        this.usuarioService = usuarioService;
    }

    public List<CitaResponseDTO> findAll() {
        return citaRepository.findAll().stream().map(this::aDto).toList();
    }

    public CitaResponseDTO findById(Integer id) {
        return aDto(obtener(id));
    }

    public List<CitaResponseDTO> findByUsuario(Integer usuarioId) {
        validarUsuario(usuarioId);
        return citaRepository.findByUsuarioId(usuarioId).stream().map(this::aDto).toList();
    }

    public List<CitaResponseDTO> findByEstado(String estado) {
        return citaRepository.findByEstadoIgnoreCase(estado).stream().map(this::aDto).toList();
    }

    public CitaResponseDTO create(CitaRequestDTO dto) {
        Usuario usuario = validarUsuario(dto.usuarioId());
        Cita c = new Cita(usuario, dto.fechahora(), resolverEstado(dto.estado()), dto.servicio());
        return aDto(citaRepository.save(c));
    }

    public CitaResponseDTO update(Integer id, CitaRequestDTO dto) {
        Cita actual = obtener(id);
        Usuario usuario = validarUsuario(dto.usuarioId());
        actual.setUsuario(usuario);
        actual.setFechahora(dto.fechahora());
        actual.setEstado(resolverEstado(dto.estado()));
        actual.setServicio(dto.servicio());
        return aDto(citaRepository.save(actual));
    }

    public CitaResponseDTO cambiarEstado(Integer id, String estado) {
        Cita actual = obtener(id);
<<<<<<< HEAD
        actual.setEstado(estado.trim());
        return aDto(citaRepository.save(actual));
=======
        String nuevoEstado = resolverEstado(estado);
        Cita c = new Cita(actual.id(), actual.clienteId(), actual.clienteNombre(), actual.servicioId(),
                actual.servicioNombre(), actual.fechaHora(), nuevoEstado);
        datos.put(id, c);
        return aDto(c);
>>>>>>> c941d9769e25da0beb4b0ae1a46f61ac155acdec
    }

    public void delete(Integer id) {
        Cita c = obtener(id);
        citaRepository.delete(c);
    }

    private Cita obtener(Integer id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada con id " + id));
    }

    private Usuario validarUsuario(Integer usuarioId) {
        return usuarioService.obtener(usuarioId);
    }

    private String resolverEstado(String estado) {
<<<<<<< HEAD
        return (estado == null || estado.isBlank()) ? ESTADO_POR_DEFECTO : estado.trim();
=======
        if (estado == null || estado.isBlank()) {
            return ESTADO_POR_DEFECTO;
        }
        String normalizado = estado.trim().toUpperCase();
        if (!ESTADOS_VALIDOS.contains(normalizado)) {
            throw new IllegalArgumentException("Estado no válido: " + estado
                    + ". Permitidos: " + String.join(", ", ESTADOS_VALIDOS));
        }
        return normalizado;
>>>>>>> c941d9769e25da0beb4b0ae1a46f61ac155acdec
    }

    private CitaResponseDTO aDto(Cita c) {
        return new CitaResponseDTO(c.getId(), c.getUsuario().getId(), c.getUsuario().getNombreuser(),
                c.getFechahora(), c.getEstado(), c.getServicio());
    }
}
