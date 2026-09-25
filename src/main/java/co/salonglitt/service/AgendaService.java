package co.salonglitt.service;

import co.salonglitt.dto.AgendaRequestDTO;
import co.salonglitt.dto.AgendaResponseDTO;
import co.salonglitt.entity.Agenda;
import co.salonglitt.entity.Usuario;
import co.salonglitt.exception.NotFoundException;
import co.salonglitt.repository.AgendaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaService {
    private final AgendaRepository agendaRepository;
    private final UsuarioService usuarioService;

    public AgendaService(AgendaRepository agendaRepository, UsuarioService usuarioService) {
        this.agendaRepository = agendaRepository;
        this.usuarioService = usuarioService;
    }

    public List<AgendaResponseDTO> findAll() { return agendaRepository.findAll().stream().map(this::aDto).toList(); }
    public AgendaResponseDTO findById(Integer id) { return aDto(obtener(id)); }

    public AgendaResponseDTO create(AgendaRequestDTO dto) {
        return aDto(agendaRepository.save(new Agenda(dto.diasemana(), dto.horainicio(), dto.horafin(), usuarioService.obtener(dto.usuarioId()))));
    }

    public AgendaResponseDTO update(Integer id, AgendaRequestDTO dto) {
        Agenda actual = obtener(id);
        actual.setDiasemana(dto.diasemana());
        actual.setHorainicio(dto.horainicio());
        actual.setHorafin(dto.horafin());
        actual.setUsuario(usuarioService.obtener(dto.usuarioId()));
        return aDto(agendaRepository.save(actual));
    }

    public void delete(Integer id) { agendaRepository.delete(obtener(id)); }

    private Agenda obtener(Integer id) {
        return agendaRepository.findById(id).orElseThrow(() -> new NotFoundException("Agenda no encontrada con id " + id));
    }

    private AgendaResponseDTO aDto(Agenda agenda) {
        return new AgendaResponseDTO(agenda.getId(), agenda.getDiasemana(), agenda.getHorainicio(), agenda.getHorafin(),
                Math.toIntExact(agenda.getUsuario().getId()), agenda.getUsuario().getNombre());
    }
}
