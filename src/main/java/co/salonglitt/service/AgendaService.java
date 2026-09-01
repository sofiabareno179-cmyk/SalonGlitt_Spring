package co.salonglitt.service;

import co.salonglitt.dto.AgendaRequestDTO;
import co.salonglitt.dto.AgendaResponseDTO;
import co.salonglitt.entity.Agenda;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.AgendaRepository;
import co.salonglitt.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final UsuarioRepository usuarioRepository;

    public AgendaService(AgendaRepository agendaRepository, UsuarioRepository usuarioRepository) {
        this.agendaRepository = agendaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<AgendaResponseDTO> findAll() {
        return agendaRepository.findAll().stream().map(this::aDto).toList();
    }

    public AgendaResponseDTO findById(Integer id) {
        return aDto(obtener(id));
    }

    public List<AgendaResponseDTO> findByEstilista(Long estilistaId) {
        validarEstilista(estilistaId);
        return agendaRepository.findByEstilistaId(estilistaId).stream().map(this::aDto).toList();
    }

    public List<AgendaResponseDTO> findByEstilistaYFecha(Long estilistaId, LocalDate fecha) {
        validarEstilista(estilistaId);
        return agendaRepository.findByEstilistaIdAndFecha(estilistaId, fecha).stream().map(this::aDto).toList();
    }

    public AgendaResponseDTO create(AgendaRequestDTO dto) {
        if (!dto.horaFin().isAfter(dto.horaInicio())) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }
        var estilista = validarEstilista(dto.estilistaId());
        Agenda a = new Agenda(estilista, dto.fecha(), dto.horaInicio(), dto.horaFin(),
                dto.disponible() == null || dto.disponible());
        return aDto(agendaRepository.save(a));
    }

    public AgendaResponseDTO update(Long id, AgendaRequestDTO dto) {
        Agenda actual = obtener(id);
        if (!dto.horaFin().isAfter(dto.horaInicio())) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }
        var estilista = validarEstilista(dto.estilistaId());
        actual.setEstilista(estilista);
        actual.setFecha(dto.fecha());
        actual.setHoraInicio(dto.horaInicio());
        actual.setHoraFin(dto.horaFin());
        actual.setDisponible(dto.disponible() == null || dto.disponible());
        return aDto(agendaRepository.save(actual));
    }

    public void delete(Long id) {
        obtener(id);
        agendaRepository.deleteById(id);
    }

    private Agenda obtener(Long id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bloque de agenda no encontrado con id " + id));
    }

    private Usuario validarEstilista(Long estilistaId) {
        return usuarioRepository.findById(estilistaId)
                .orElseThrow(() -> new NotFoundException("Estilista no encontrado con id " + estilistaId));
    }

    private AgendaResponseDTO aDto(Agenda a) {
        return new AgendaResponseDTO(a.getId(), a.getEstilista().getId(), a.getEstilista().getNombre(),
                a.getFecha(), a.getHoraInicio(), a.getHoraFin(), a.isDisponible());
    }
}
