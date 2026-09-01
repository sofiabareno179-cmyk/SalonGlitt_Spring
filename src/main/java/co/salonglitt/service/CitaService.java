package co.salonglitt.service;

import co.salonglitt.dto.CitaRequestDTO;
import co.salonglitt.dto.CitaResponseDTO;
import co.salonglitt.entity.Cita;
import co.salonglitt.entity.Servicio;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.CitaRepository;
import co.salonglitt.repository.ServicioRepository;
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
    private final ServicioRepository servicioRepository;

    public CitaService(CitaRepository citaRepository, UsuarioRepository usuarioRepository,
                       ServicioRepository servicioRepository) {
        this.citaRepository = citaRepository;
        this.usuarioRepository = usuarioRepository;
        this.servicioRepository = servicioRepository;
    }

    public List<CitaResponseDTO> findAll() {
        return citaRepository.findAll().stream().map(this::aDto).toList();
    }

    public CitaResponseDTO findById(Long id) {
        return aDto(obtener(id));
    }

    public List<CitaResponseDTO> findByCliente(Long clienteId) {
        List<Cita> result = citaRepository.findByClienteId(clienteId);
        if (result.isEmpty() && !usuarioRepository.existsById(clienteId)) {
            throw new NotFoundException("Cliente no encontrado con id " + clienteId);
        }
        return result.stream().map(this::aDto).toList();
    }

    public List<CitaResponseDTO> findByEstado(String estado) {
        return citaRepository.findByEstadoIgnoreCase(estado).stream().map(this::aDto).toList();
    }

    public CitaResponseDTO create(CitaRequestDTO dto) {
        var cliente = obtenerCliente(dto.clienteId());
        var servicio = obtenerServicio(dto.servicioId());
        Cita c = new Cita(cliente, servicio, dto.fechaHora(), resolverEstado(dto.estado()));
        return aDto(citaRepository.save(c));
    }

    public CitaResponseDTO update(Long id, CitaRequestDTO dto) {
        Cita actual = obtener(id);
        var cliente = obtenerCliente(dto.clienteId());
        var servicio = obtenerServicio(dto.servicioId());
        actual.setCliente(cliente);
        actual.setServicio(servicio);
        actual.setFechaHora(dto.fechaHora());
        actual.setEstado(resolverEstado(dto.estado()));
        return aDto(citaRepository.save(actual));
    }

    public CitaResponseDTO cambiarEstado(Long id, String estado) {
        Cita actual = obtener(id);
        actual.setEstado(resolverEstado(estado));
        return aDto(citaRepository.save(actual));
    }

    public void delete(Long id) {
        obtener(id);
        citaRepository.deleteById(id);
    }

    private Cita obtener(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada con id " + id));
    }

    private Usuario obtenerCliente(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente no encontrado con id " + id));
    }

    private Servicio obtenerServicio(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Servicio no encontrado con id " + id));
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
        return new CitaResponseDTO(c.getId(), c.getCliente().getId(), c.getCliente().getNombre(),
                c.getServicio().getId(), c.getServicio().getNombre(), c.getFechaHora(), c.getEstado());
    }
}
