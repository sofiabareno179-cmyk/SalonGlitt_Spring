package co.salonglitt.service;

import co.salonglitt.dto.AgendaRequestDTO;
import co.salonglitt.dto.AgendaResponseDTO;
import co.salonglitt.dto.UsuarioResponseDTO;
import co.salonglitt.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AgendaService {

    private record Bloque(Long id, Long estilistaId, String estilistaNombre, LocalDate fecha,
                          LocalTime horaInicio, LocalTime horaFin, boolean disponible) {
    }

    private final Map<Long, Bloque> datos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong();
    private final UsuarioService usuarioService;

    public AgendaService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public List<AgendaResponseDTO> findAll() {
        return datos.values().stream().map(this::aDto).toList();
    }

    public AgendaResponseDTO findById(Long id) {
        return aDto(obtener(id));
    }

    public List<AgendaResponseDTO> findByEstilista(Long estilistaId) {
        validarEstilista(estilistaId);
        return datos.values().stream()
                .filter(b -> b.estilistaId().equals(estilistaId))
                .map(this::aDto)
                .toList();
    }

    public List<AgendaResponseDTO> findByEstilistaYFecha(Long estilistaId, LocalDate fecha) {
        validarEstilista(estilistaId);
        return datos.values().stream()
                .filter(b -> b.estilistaId().equals(estilistaId) && b.fecha().equals(fecha))
                .map(this::aDto)
                .toList();
    }

    public AgendaResponseDTO create(AgendaRequestDTO dto) {
        if (!dto.horaFin().isAfter(dto.horaInicio())) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }
        var estilista = validarEstilista(dto.estilistaId());
        long id = secuencia.incrementAndGet();
        Bloque b = new Bloque(id, estilista.id(), estilista.nombre(), dto.fecha(),
                dto.horaInicio(), dto.horaFin(), dto.disponible() == null || dto.disponible());
        datos.put(id, b);
        return aDto(b);
    }

    public AgendaResponseDTO update(Long id, AgendaRequestDTO dto) {
        obtener(id);
        if (!dto.horaFin().isAfter(dto.horaInicio())) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }
        var estilista = validarEstilista(dto.estilistaId());
        Bloque b = new Bloque(id, estilista.id(), estilista.nombre(), dto.fecha(),
                dto.horaInicio(), dto.horaFin(), dto.disponible() == null || dto.disponible());
        datos.put(id, b);
        return aDto(b);
    }

    public void delete(Long id) {
        obtener(id);
        datos.remove(id);
    }

    private Bloque obtener(Long id) {
        Bloque b = datos.get(id);
        if (b == null) {
            throw new NotFoundException("Bloque de agenda no encontrado con id " + id);
        }
        return b;
    }

    private UsuarioResponseDTO validarEstilista(Long estilistaId) {
        return usuarioService.findById(estilistaId);
    }

    private AgendaResponseDTO aDto(Bloque b) {
        return new AgendaResponseDTO(b.id(), b.estilistaId(), b.estilistaNombre(), b.fecha(),
                b.horaInicio(), b.horaFin(), b.disponible());
    }
}