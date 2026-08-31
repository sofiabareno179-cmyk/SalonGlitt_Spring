package co.salonglitt.service;

import co.salonglitt.dto.CitaRequestDTO;
import co.salonglitt.dto.CitaResponseDTO;
import co.salonglitt.dto.UsuarioResponseDTO;
import co.salonglitt.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CitaService {

    private record Cita(Long id, Long clienteId, String clienteNombre, Long servicioId,
                        String servicioNombre, LocalDateTime fechaHora, String estado) {
    }

    private static final String ESTADO_POR_DEFECTO = "PENDIENTE";

    private static final java.util.Set<String> ESTADOS_VALIDOS =
            java.util.Set.of("PENDIENTE", "CONFIRMADA", "COMPLETADA", "CANCELADA");

    private final Map<Long, Cita> datos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong();
    private final UsuarioService usuarioService;
    private final ServicioService servicioService;

    public CitaService(UsuarioService usuarioService, ServicioService servicioService) {
        this.usuarioService = usuarioService;
        this.servicioService = servicioService;
    }

    public List<CitaResponseDTO> findAll() {
        return datos.values().stream().map(this::aDto).toList();
    }

    public CitaResponseDTO findById(Long id) {
        return aDto(obtener(id));
    }

    public List<CitaResponseDTO> findByCliente(Long clienteId) {
        validarCliente(clienteId);
        return datos.values().stream()
                .filter(c -> c.clienteId().equals(clienteId))
                .map(this::aDto)
                .toList();
    }

    public List<CitaResponseDTO> findByEstado(String estado) {
        return datos.values().stream()
                .filter(c -> c.estado().equalsIgnoreCase(estado))
                .map(this::aDto)
                .toList();
    }

    public CitaResponseDTO create(CitaRequestDTO dto) {
        var cliente = validarCliente(dto.clienteId());
        var servicio = servicioService.findById(dto.servicioId());
        long id = secuencia.incrementAndGet();
        String estado = resolverEstado(dto.estado());
        Cita c = new Cita(id, cliente.id(), cliente.nombre(), servicio.id(), servicio.nombre(),
                dto.fechaHora(), estado);
        datos.put(id, c);
        return aDto(c);
    }

    public CitaResponseDTO update(Long id, CitaRequestDTO dto) {
        Cita actual = obtener(id);
        var cliente = validarCliente(dto.clienteId());
        var servicio = servicioService.findById(dto.servicioId());
        Cita c = new Cita(actual.id(), cliente.id(), cliente.nombre(), servicio.id(), servicio.nombre(),
                dto.fechaHora(), resolverEstado(dto.estado()));
        datos.put(id, c);
        return aDto(c);
    }

    public CitaResponseDTO cambiarEstado(Long id, String estado) {
        Cita actual = obtener(id);
        String nuevoEstado = resolverEstado(estado);
        Cita c = new Cita(actual.id(), actual.clienteId(), actual.clienteNombre(), actual.servicioId(),
                actual.servicioNombre(), actual.fechaHora(), nuevoEstado);
        datos.put(id, c);
        return aDto(c);
    }

    public void delete(Long id) {
        obtener(id);
        datos.remove(id);
    }

    private Cita obtener(Long id) {
        Cita c = datos.get(id);
        if (c == null) {
            throw new NotFoundException("Cita no encontrada con id " + id);
        }
        return c;
    }

    private UsuarioResponseDTO validarCliente(Long clienteId) {
        return usuarioService.findById(clienteId);
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
        return new CitaResponseDTO(c.id(), c.clienteId(), c.clienteNombre(), c.servicioId(),
                c.servicioNombre(), c.fechaHora(), c.estado);
    }
}