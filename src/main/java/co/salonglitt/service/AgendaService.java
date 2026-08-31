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

    public List<AgendaResponseDTO> findAll() {
        return agendaRepository.findAll().stream().map(this::aDto).toList();
    }

    public AgendaResponseDTO findById(Integer id) {
        return aDto(obtener(id));
    }

    public AgendaResponseDTO create(AgendaRequestDTO dto) {
        Usuario usuario = validarUsuario(dto.usuarioId());
        Agenda a = new Agenda(dto.diasemana().trim(), dto.horainicio().trim(), dto.horafin().trim(), usuario);
        return aDto(agendaRepository.save(a));
    }

    public AgendaResponseDTO update(Integer id, AgendaRequestDTO dto) {
        Agenda actual = obtener(id);
        Usuario usuario = validarUsuario(dto.usuarioId());
        actual.setDiasemana(dto.diasemana().trim());
        actual.setHorainicio(dto.horainicio().trim());
        actual.setHorafin(dto.horafin().trim());
        actual.setUsuario(usuario);
        return aDto(agendaRepository.save(actual));
    }

    public void delete(Integer id) {
        Agenda a = obtener(id);
        agendaRepository.delete(a);
    }

    private Agenda obtener(Integer id) {
        return agendaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bloque de agenda no encontrado con id " + id));
    }

    private Usuario validarUsuario(Integer usuarioId) {
        return usuarioService.obtener(usuarioId);
    }

    private AgendaResponseDTO aDto(Agenda a) {
        return new AgendaResponseDTO(a.getId(), a.getDiasemana(), a.getHorainicio(), a.getHorafin(),
                a.getUsuario().getId(), a.getUsuario().getNombreuser());
    }
}
