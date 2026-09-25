package co.salonglitt.service;

import co.salonglitt.dto.CitaRequestDTO;
import co.salonglitt.dto.CitaResponseDTO;
import co.salonglitt.entity.Cita;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.CitaRepository;
import co.salonglitt.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class CitaService {
    private static final Set<String> ESTADOS_VALIDOS = Set.of("PENDIENTE", "CONFIRMADA", "COMPLETADA", "CANCELADA", "ESPERA");
    private final CitaRepository citaRepository;
    private final UsuarioService usuarioService;
    private final ServicioRepository servicioRepository;

    public CitaService(CitaRepository citaRepository, UsuarioService usuarioService, ServicioRepository servicioRepository) {
        this.citaRepository = citaRepository;
        this.usuarioService = usuarioService;
        this.servicioRepository = servicioRepository;
    }

    public List<CitaResponseDTO> findAll() { return citaRepository.findAll().stream().map(this::aDto).toList(); }
    public CitaResponseDTO findById(Integer id) { return aDto(obtener(id)); }
    public List<CitaResponseDTO> findByUsuario(Integer usuarioId) { return findByCliente(usuarioId); }
    public List<CitaResponseDTO> findByCliente(Integer usuarioId) {
        usuarioService.obtener(usuarioId);
        return citaRepository.findByUsuarioId(usuarioId).stream().map(this::aDto).toList();
    }
    public List<CitaResponseDTO> findByEstado(String estado) {
        return citaRepository.findByEstadoIgnoreCase(estado).stream().map(this::aDto).toList();
    }

    public CitaResponseDTO create(CitaRequestDTO dto) {
        var usuario = usuarioService.obtener(dto.clienteId().intValue());
        if (dto.servicioId() == null || servicioRepository == null) {
            return aDto(citaRepository.save(new Cita(usuario, dto.fechaHora(), resolverEstado(dto.estado()), dto.servicio())));
        }
        Cita cita = new Cita(usuario, servicioRepository.findById(dto.servicioId()).orElseThrow(), dto.fechaHora(), resolverEstado(dto.estado()));
        return aDto(citaRepository.save(cita));
    }

    public CitaResponseDTO update(Integer id, CitaRequestDTO dto) {
        Cita actual = obtener(id);
        actual.setCliente(usuarioService.obtener(dto.clienteId().intValue()));
        if (dto.servicioId() != null && servicioRepository != null) {
            actual.setServicio(servicioRepository.findById(dto.servicioId()).orElseThrow());
        } else {
            actual.setServicioLegacy(dto.servicio());
        }
        actual.setFechaHora(dto.fechaHora());
        actual.setEstado(resolverEstado(dto.estado()));
        return aDto(citaRepository.save(actual));
    }

    public CitaResponseDTO cambiarEstado(Integer id, String estado) {
        Cita actual = obtener(id);
        actual.setEstado(resolverEstado(estado));
        return aDto(citaRepository.save(actual));
    }

    public void delete(Integer id) { citaRepository.delete(obtener(id)); }
    private Cita obtener(Integer id) { return citaRepository.findById(id).orElseThrow(() -> new NotFoundException("Cita no encontrada con id " + id)); }
    private String resolverEstado(String estado) {
        String normalizado = estado == null || estado.isBlank() ? "ESPERA" : estado.trim().toUpperCase();
        if (!ESTADOS_VALIDOS.contains(normalizado)) throw new IllegalArgumentException("Estado no válido: " + estado);
        return switch (normalizado) {
            case "PENDIENTE", "ESPERA" -> "Espera";
            case "CONFIRMADA" -> "Confirmada";
            case "COMPLETADA" -> "Completada";
            case "CANCELADA" -> "Cancelada";
            default -> normalizado;
        };
    }
    private CitaResponseDTO aDto(Cita cita) {
        return new CitaResponseDTO(Math.toIntExact(cita.getId()), Math.toIntExact(cita.getCliente().getId()), cita.getCliente().getNombre(),
                cita.getFechaHora(), cita.getEstado(), cita.getServicio() == null ? cita.getServicioLegacy() : cita.getServicio().getNombre());
    }
}
